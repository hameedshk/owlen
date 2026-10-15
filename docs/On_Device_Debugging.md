# Owlen — On-Device Debugging (for AI coding assistants)

How to drive the test device over ADB without drawing false conclusions.
Read this before touching app code in response to "the UI doesn't respond".

Test device: **Samsung SM-A736B, Android 15, 1080×2400, ADB over Wi-Fi.**

---

## Rule 1 — Never tap coordinates read off a screenshot

Screenshots delivered into an assistant conversation are **downscaled**. On this
device they arrive as 900×2000 from a real 1080×2400 panel, a factor of
**0.833**. Coordinates read off the image and passed to `adb shell input tap`
land at 0.833× the intended position.

The error is *proportional to the coordinate*, so it grows down the screen:

| Target | True centre | Scaled tap lands | Hit? |
|---|---|---|---|
| Hero "TAP TO START" `[315,577][765,1027]` | (540, 802) | (450, 668) | **Yes** — 450px-tall target absorbs the 134px error |
| Sound / Sensitivity cards `y 1594–1752` | (289, 1673) | (241, 1394) | No |
| Bottom nav tabs `y 2040–2265` | (539, 2152) | (449, 1793) | No — 359px error vs. a 225px-tall target |

That asymmetry is the trap: large centred controls still work while everything
in the lower third silently does nothing. It reads exactly like "touch events
never reach Compose" — and it is not.

**Always source coordinates from `uiautomator dump` bounds.** They are true
device pixels. If you must use an image, multiply by 1.20 first.

---

## Rule 2 — Prove injection is broken before blaming the app

Before changing a single line of UI code, check the input dispatcher:

```bash
adb shell dumpsys input | grep -A2 "com.owlen.app"
```

Injection is **fine** when the app window shows all of:

- `inputConfig=0x0` — no `NOT_TOUCHABLE` / `NOT_FOCUSABLE` flags
- `touchableRegion=[0,0][1080,2400]` — covers the screen
- `status=NORMAL, responsive=true` on its input channel
- `DispatchEnabled: true`, `DispatchFrozen: false`

Also confirm nothing occludes it. `[Gesture Monitor] secinputdev` and
`PalmMotion` are **normal Samsung SPY monitors** — they do not block input.

If all of the above hold, the fault is in your coordinates, not the app.
Confirm with one known-good tap sourced from `uiautomator` before proceeding.

---

## Rule 3 — Compose puts the click handler on a parent, not the label

In a `uiautomator` dump the visible text sits on a leaf `TextView` with
`clickable="false"`, while the actual handler is on an ancestor `android.view.View`
with `clickable="true"`. Tapping the leaf's own bounds often still works (it is
inside the parent), but resolving properly is more reliable:

1. Find the leaf node by `text` or `content-desc`.
2. Walk **up** to the nearest ancestor with `clickable="true"`.
3. Tap the centre of that ancestor's `bounds`.

Example — the Activity tab's label is a leaf at `[461,2176][617,2219]`, but the
clickable ancestor is `[367,2040][712,2265]`, centre **(539, 2152)**.

---

## Reusable helper

Copy to your scratchpad and dot-source it. Resolves targets by label, never by
pixel guesswork.

```powershell
function Get-UiDump {
    adb shell uiautomator dump /sdcard/_ui.xml 2>$null | Out-Null
    [xml](adb shell cat /sdcard/_ui.xml)
}

function Find-Node($xml, [string]$Label) {
    $xml.SelectNodes("//node") | Where-Object {
        $_.text -eq $Label -or $_.'content-desc' -eq $Label
    } | Select-Object -First 1
}

function Get-Center($node) {
    if ($node.bounds -match '\[(\d+),(\d+)\]\[(\d+),(\d+)\]') {
        @{ X = [int](([int]$Matches[1] + [int]$Matches[3]) / 2)
           Y = [int](([int]$Matches[2] + [int]$Matches[4]) / 2)
           Bounds = $node.bounds }
    }
}

# Compose puts the handler on a parent View while the label sits on a leaf.
function Get-ClickableAncestor($node) {
    $n = $node
    while ($n -ne $null -and $n.LocalName -eq 'node') {
        if ($n.clickable -eq 'true') { return $n }
        $n = $n.ParentNode
    }
}

function Tap-Label([string]$Label) {
    $xml = Get-UiDump
    $leaf = Find-Node $xml $Label
    if (-not $leaf) { Write-Output "NOT FOUND: $Label"; return }
    $target = Get-ClickableAncestor $leaf; if (-not $target) { $target = $leaf }
    $c = Get-Center $target
    Write-Output ("TAP '{0}' -> ({1},{2}) bounds={3}" -f $Label, $c.X, $c.Y, $c.Bounds)
    adb shell input tap $c.X $c.Y
    Start-Sleep -Milliseconds 1100
}

function Get-ScreenLabels {
    ((Get-UiDump).SelectNodes("//node") | ForEach-Object { $_.text } |
        Where-Object { $_ }) -join ' | '
}
```

Usage — assert the screen actually changed, don't assume:

```powershell
Tap-Label "Settings"
Get-ScreenLabels    # => PREFERENCES | Settings | PLAYBACK | Sleep Schedule | ...
```

---

## Standard verify loop after a UI change

```powershell
./gradlew :app:assembleDebug --console=plain -q
adb install -r app/build/outputs/apk/debug/app-debug.apk
adb shell am force-stop com.owlen.app
adb shell am start -n com.owlen.app/.presentation.MainActivity
```

Then exercise, checking `Get-ScreenLabels` after each step:

- Bottom nav: Home → Activity → Settings → Home
- Sub-nav: Settings → Sleep Timer, Settings → Sleep Schedule, `KEYCODE_BACK` from each
- Scroll: `adb shell input swipe 540 1700 540 900 300` on Settings, confirm a
  known label's `bounds` y-value moved. Home content fits the viewport
  (ends y≈1752, nav starts y=2040) so it will **not** scroll — that is correct,
  not a bug.
- Hero button: tap (540, 802), confirm ACTIVE ⇄ TAP TO START toggles

Clean up `/sdcard/_ui.xml` and leave the app in the state you found it.

---

## Case study — the 2026-10-15 phantom bug

A session read tap coordinates off scaled screenshots, saw the bottom nav never
respond, and concluded "touch events are not reaching Compose UI at all."
Two commits then changed real code to fix a bug that never existed:

- `694c3dd` stripped `verticalScroll` from the HomeScreen root `Column` and
  removed all `FadeSlideIn` / `AnimatedVisibility` entry animations
- `2935cc4` added `window.setDecorFitsSystemWindows(false)`, conflicting with
  `enableEdgeToEdge()`

Both reverted in `8c32ddb`. With coordinates taken from `uiautomator` bounds,
every tab, every sub-screen, scrolling, and the hero button's `pointerInput`
all worked on the unmodified app.

**The lesson:** when input "doesn't work" but manual touch does, suspect the
harness first. Verify with Rule 2, then one known-good tap. Never let a tooling
artifact drive an app-code change.
