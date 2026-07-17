# Owlen — UX Specification
**Version 2.0 · MVP · Android · Kotlin + Jetpack Compose**
**Source of truth: Design image 23465.png**

---

## 1. UX Principles

### 1.1 One Tap to Protect
The entire protection experience starts and stops with a single toggle on the Home screen. No multi-step activation at night.

### 1.2 Works in the Background
Once active, Owlen operates entirely with the screen off. The user should never need to interact with the app during the night.

### 1.3 Only Reacts When Needed
Masking plays only when a disturbance is detected. Never continuous. The app is silent until it is needed.

### 1.4 Critical Alerts Always Break Through
Smoke Alarm and Baby Cry override everything — full-screen alerts activate even from lock screen, masking stops immediately.

### 1.5 Morning Summary in Under 10 Seconds
The morning review is a glance, not a report. Protected time, event count, masking duration. One tap to details if needed.

### 1.6 Daily Interaction Time Under 30 Seconds
Outside of first-time setup, the user should spend less than 30 seconds per day interacting with Owlen. Every screen is designed around this constraint.

### 1.7 Dark by Default
No light mode in MVP. Dark navy background throughout. Amber/orange primary accent. Text is off-white, never pure white.

---

## 2. Design System

### 2.1 Colour Palette

| Token | Hex | Usage |
|---|---|---|
| `background` | #0F1117 | All screen backgrounds — dark navy |
| `surface` | #1A1D27 | Cards, bottom sheets |
| `surfaceVar` | #252836 | Elevated cards, input rows |
| `primary` | #F5A623 | CTA buttons, active states, scores, accents — amber/orange |
| `primaryDim` | #C4841C | Primary pressed state |
| `onPrimary` | #0F1117 | Text on primary colour |
| `green` | #4CAF50 | Active protection state, checkmarks, success |
| `greenDim` | #2E7D32 | Green pressed state |
| `safety` | #FF5252 | Safety Event alerts — Baby Cry, Smoke Alarm |
| `safetyDim` | #B71C1C | Safety alert background |
| `textPrimary` | #E8E8E8 | Body text, labels |
| `textSecondary` | #8A8FA8 | Captions, hints, metadata |
| `textDisabled` | #3D4055 | Disabled controls |
| `divider` | #252836 | Separators |
| `warning` | #FF9800 | Interruption banners |
| `scoreHigh` | #F5A623 | Disturbance score display |

**Never use:**
- Pure white (#FFFFFF)
- Blue as primary accent (replaced by amber)
- Any colour not in this palette

### 2.2 Typography

Font: **Inter** (Google Fonts)

| Style | Size | Weight | Usage |
|---|---|---|---|
| `displayLarge` | 48sp | Light 300 | Active mode "Protecting Sleep" |
| `headlineLarge` | 32sp | Bold 700 | Splash app name "OWLEN" |
| `headlineMedium` | 24sp | SemiBold 600 | Screen titles, alert headings |
| `titleLarge` | 20sp | SemiBold 600 | Card titles, section headers |
| `titleMedium` | 18sp | Medium 500 | Setup step labels |
| `bodyLarge` | 16sp | Regular 400 | Primary body text |
| `bodyMedium` | 14sp | Regular 400 | Secondary body, list items |
| `labelLarge` | 14sp | SemiBold 600 | Button labels |
| `labelSmall` | 11sp | Medium 500 | Captions, timestamps, badges |
| `scoreDisplay` | 40sp | Bold 700 | Disturbance score number |

### 2.3 Spacing Scale

Multiples of 4dp only. Screen horizontal padding: **20dp**.

| Token | Value |
|---|---|
| `xs` | 4dp |
| `sm` | 8dp |
| `md` | 16dp |
| `lg` | 24dp |
| `xl` | 32dp |
| `xxl` | 48dp |

### 2.4 Shape

| Component | Shape |
|---|---|
| Cards | RoundedCorner 12dp |
| Buttons (primary) | RoundedCorner 12dp |
| Bottom tab bar | Top corners 0dp (flush) |
| Input time pickers | RoundedCorner 8dp |
| Chips / noise options | RoundedCorner 24dp (pill) |
| Score display | Circle |
| Event overlay | RoundedCorner 16dp top sheet |

### 2.5 Owl Mascot
- Used on: Splash screen, Onboarding screen 1
- Style: Stylised owl icon with large eyes, dark feathers, amber/white colour
- Do not use on any other screen
- Supplied as vector drawable `ic_owlen_mascot`

### 2.6 Iconography
Material Symbols Rounded, weight 300, size 24dp standard.
- Bottom nav icons: Home (house), Activity (waveform/bar chart), Settings (gear)
- Active nav icon: `primary` amber colour
- Inactive nav icon: `textSecondary`

### 2.7 Bottom Navigation — 3 Tab Structure

| Tab | Icon | Label | Content |
|---|---|---|---|
| Home | House | Home | Control & Status |
| Activity | Bar chart | Activity | Events & History |
| Settings | Gear | Settings | Preferences |

Bottom nav background: `surface`. Selected tab indicator: `primary` amber dot or underline.

### 2.8 Animation

| Interaction | Duration | Easing |
|---|---|---|
| Screen transitions | 300ms | LinearOutSlowIn |
| Protection toggle ON | 400ms | FastOutSlowIn |
| Shield pulse (active) | 3000ms loop | EaseInOut |
| Calibration countdown | 1000ms per second | Linear |
| Score reveal | 500ms | FastOutSlowIn |
| Safety alert appearance | 200ms | FastOutSlowIn |
| Event overlay appearance | 300ms slide up | FastOutSlowIn |
| Masking volume bar | Continuous | Linear |

---

## 3. Navigation Structure

### 3.1 Onboarding Flow (First Launch Only)
Single stack, no bottom nav shown:
```
Splash
  ↓
Onboarding Screen 1 (Sleep peacefully)
  ↓
Onboarding Screen 2
  ↓
Onboarding Screen 3
  ↓
Permissions Screen
  ↓
Setup Step 1 — Sleep Time & Wake Time
  ↓
Setup Step 2 — Choose Protection Sound
  ↓
Setup Step 3 — Sensitivity
  ↓
Calibration (60s)
  ↓
Home (bottom nav appears)
```

### 3.2 Main App Flow (After Onboarding)
3-tab bottom navigation:
```
Home Tab          Activity Tab        Settings Tab
─────────         ────────────        ────────────
Home Screen  →    Session List   →    Settings Screen
(toggle ON)       Session Details
     ↓
Protection Active Screen
(screen off — foreground service)
     ↓
Event Detected Overlay (auto-dismiss 5s)
     ↓
Safety Alert (if critical — full screen)
     ↓
Morning Summary (on wake)
     ↓
Home (toggle OFF)
```

---

## 4. Screen Specifications

---

### Screen 1 — Splash

**Duration:** 2 seconds, then auto-navigate to Onboarding (or Home if returning user)

**Layout:**
```
┌─────────────────────────────────┐
│                                 │
│                                 │
│         [Owl mascot icon]       │  ← centred, 120dp
│                                 │
│              OWLEN              │  ← headlineLarge, textPrimary
│      Protecting peaceful sleep  │  ← bodyMedium, textSecondary
│                                 │
│                                 │
└─────────────────────────────────┘
```

Background: gradient from `#0F1117` (top) to `#1A1D27` (bottom).
No interactive elements. No skip button.

---

### Screen 2 — Onboarding (3 Screens)

**Progress indicator:** 3 dots at bottom, active dot `primary` amber.
**CTA:** "Next →" button bottom, `primary` fill. Final screen CTA: "Get Started".

**Screen 1:**
- Owl mascot centred, 100dp
- Headline: "Sleep peacefully." — headlineMedium, textPrimary
- Body: "Owlen listens for sleep-disrupting sounds and reacts automatically." — bodyLarge, textSecondary, centred
- Dot indicator: position 1 of 3 active

**Screen 2 & 3:** Content TBD by product — same layout pattern. Focus on: how it works, privacy guarantee.

---

### Screen 3 — Permissions

**Back arrow:** top left
**Progress bar:** thin green bar at top, partial fill

**Layout:**
```
┌─────────────────────────────────┐
│  ←  [progress bar]              │
│                                 │
│  Permissions                    │  ← titleLarge
│  To protect your sleep,         │  ← bodyMedium, textSecondary
│  Owlen needs a few things.      │
│                                 │
│  ┌─────────────────────────┐    │
│  │ 🎤 Microphone        ✓  │    │  ← granted = green checkmark
│  │ Listen for environmental│    │
│  │ sounds.                 │    │
│  └─────────────────────────┘    │
│                                 │
│  ┌─────────────────────────┐    │
│  │ 🔋 Ignore Battery    ✓  │    │  ← granted = green checkmark
│  │ Optimisation            │    │
│  │ Keep Owlen running      │    │
│  │ through the night.      │    │
│  └─────────────────────────┘    │
│                                 │
│  [        Continue        ]     │  ← primary button, enabled when both granted
└─────────────────────────────────┘
```

**Permission rows:**
- Background: `surfaceVar`
- Icon: left, 24dp
- Title: `bodyLarge` `textPrimary`
- Description: `bodyMedium` `textSecondary`
- Checkmark: right, `green`, appears after permission granted
- Before grant: row has no checkmark, tapping requests permission

**Continue button:** Disabled until both permissions granted.

---

### Screen 4 — Setup (3 Steps)

**Progress bar:** thin bar at top, fills across 3 steps. Green fill.
**Back arrow:** top left on steps 2 and 3.

#### Step 1 — Sleep Time

```
┌─────────────────────────────────┐
│  ←  [████░░░░░░░] progress      │
│                                 │
│  Sleep Time                     │  ← titleLarge
│                                 │
│  ┌──────────────────────────┐   │
│  │   10   :   00   PM       │   │  ← time picker, large digits
│  └──────────────────────────┘   │
│                                 │
│  Wake Time                      │  ← titleLarge
│                                 │
│  ┌──────────────────────────┐   │
│  │    7   :   00   AM       │   │  ← time picker
│  └──────────────────────────┘   │
│                                 │
│  [           Next          ]    │  ← primary button
└─────────────────────────────────┘
```

Time picker style: Large digit display in `surfaceVar` card. Scrollable drum/spinner style. Digits in `textPrimary` 32sp bold. AM/PM toggle inline.

#### Step 2 — Choose Protection Sound

```
┌─────────────────────────────────┐
│  ←  [████████░░░] progress      │
│                                 │
│  Choose Protection Sound        │  ← titleLarge
│  This will be played when       │  ← bodyMedium, textSecondary
│  needed.                        │
│                                 │
│  ○  Brown Noise                 │  ← radio option
│  ○  Pink Noise                  │
│  ○  White Noise                 │
│  ○  Fan                         │
│  ○  Rain                        │
│  ○  Ocean Waves                 │
│                                 │
│  [         Continue        ]    │
└─────────────────────────────────┘
```

Selected option: filled `primary` amber circle + `primary` text + `primary` left border on row. Default: Brown Noise selected.

#### Step 3 — Sensitivity

```
┌─────────────────────────────────┐
│  ←  [████████████] progress     │
│                                 │
│  Sensitivity                    │  ← titleLarge
│  Controls how easily Owlen      │  ← bodyMedium, textSecondary
│  responds.                      │
│                                 │
│  ○  Low                         │
│  ●  Medium                      │  ← default selected, primary fill
│  ○  High                        │
│                                 │
│  [         Continue        ]    │
└─────────────────────────────────┘
```

---

### Screen 5 — Calibration

```
┌─────────────────────────────────┐
│                                 │
│         Preparing...            │  ← titleLarge, centred
│    Listening to your room       │  ← bodyMedium, textSecondary
│                                 │
│       ┌──────────────┐          │
│       │              │          │
│       │      45      │          │  ← scoreDisplay (40sp bold), countdown
│       │  sec remaining│         │  ← labelSmall, textSecondary
│       │              │          │
│       └──────────────┘          │  ← circular progress ring, green
│                                 │
│  Current Noise                  │  ← labelSmall, textSecondary
│        41 dB                    │  ← titleLarge, textPrimary
│                                 │
│  Keep your phone nearby         │  ← bodyMedium, textSecondary, centred
│  and stay quiet.                │
│                                 │
└─────────────────────────────────┘
```

Circular progress ring: green `#4CAF50`, 6dp stroke, 120dp diameter, drains anticlockwise over 60s.
On completion: auto-navigate to Home. No confirmation.

---

### Screen 6 — Home (Protection OFF)

**Tab:** Home
**Bottom nav:** visible

```
┌─────────────────────────────────┐
│  🦉 Owlen              ≡        │  ← owl icon small + app name + menu
│                                 │
│  Good Evening                   │  ← bodyLarge, textSecondary (time-based greeting)
│                                 │
│  Protection                     │  ← labelSmall, textSecondary
│  [●●○○○○○] OFF                  │  ← toggle, OFF state, textDisabled track
│                                 │
│  Sleep    Wake                  │
│  10:00 PM  7:00 AM              │  ← bodyMedium, textPrimary
│                                 │
│  🎵 Brown Noise                 │  ← current sound setting
│  ⚙  Medium Sensitivity         │  ← current sensitivity
│                                 │
│  [⚙ Settings]                   │  ← secondary button or text link
│                                 │
│  ─────────────────────────      │
│  [Home]   [Activity]  [Settings]│  ← bottom nav
└─────────────────────────────────┘
```

**Greeting:** "Good Morning", "Good Afternoon", "Good Evening" based on time of day.
**Toggle:** Large pill toggle. OFF = `textDisabled` track, thumb left. ON = `green` track, thumb right.
**Settings link:** text link or icon button, not a full primary button.

---

### Screen 7 — Protection Active

**Shown:** When user enables protection and picks up phone during the night.
**Screen should normally be OFF** — this is what they see if they wake and check.

```
┌─────────────────────────────────┐
│                                 │
│         [Green shield ✓]        │  ← 80dp, green, pulsing slowly
│                                 │
│      Protecting Sleep           │  ← headlineMedium, textPrimary
│         Monitoring...           │  ← bodyMedium, textSecondary
│                                 │
│  Microphone Active  ●           │  ← bodyMedium + green dot indicator
│                                 │
│  Started        Battery         │
│  10:05 PM        98%            │  ← two column stats, bodyLarge textPrimary
│                                 │
│                                 │
│  [           STOP            ]  │  ← safety red button, full width, 64dp height
│                                 │
└─────────────────────────────────┘
```

**Green shield:** Filled checkmark shield, `green` colour, gentle pulse animation (scale 1.0→1.06→1.0, 3s loop).
**Microphone Active:** Green dot `●` blinking every 2 seconds.
**STOP button:** `safety` red background `#FF5252`, white text, prominent. Not amber — this is a stop action.
**No bottom nav** on this screen — full focus on active state.

---

### Screen 8 — Event Detected Overlay

**Shown:** When an event is detected and masking starts. Auto-dismisses after 5 seconds.
**Displayed as:** Bottom sheet overlay over the Active screen (or notification if screen is off).

```
┌─────────────────────────────────┐
│                                 │
│  ┌─────────────────────────┐    │
│  │  [Truck/event icon]     │    │  ← event-specific icon, amber
│  │                         │    │
│  │  Garbage Collection     │    │  ← titleLarge, textPrimary
│  │  Detected               │    │
│  │                         │    │
│  │  Disturbance Score      │    │  ← labelSmall, textSecondary
│  │        89               │    │  ← scoreDisplay, primary amber
│  │                         │    │
│  │  Playing Brown Noise    │    │  ← bodyMedium, textSecondary
│  │  [████░░░░░░░░] 25%     │    │  ← volume bar, primary amber fill
│  │                         │    │
│  │  Auto dismiss after     │    │  ← labelSmall, textSecondary
│  │  5 seconds              │    │
│  └─────────────────────────┘    │
└─────────────────────────────────┘
```

**Card:** `surfaceVar` background, 16dp top radius, amber left border 4dp.
**Event icon:** Event-specific — truck for Garbage Collection, motorcycle, dog, etc.
**Score:** Large amber number, prominent.
**Volume bar:** `primary` amber fill, shows current masking volume %.
**Auto-dismiss:** 5 second countdown, no user action required.
**If screen is off:** Show as heads-up notification only, do not wake screen.

---

### Screen 9 — Safety Alert

**Shown:** On Smoke Alarm or Baby Cry detection. Full screen. Wakes screen from lock.

**Baby Cry variant:**
```
┌─────────────────────────────────┐
│  Background: safetyDim #B71C1C  │
│                                 │
│         [Bell/baby icon]        │  ← 80dp, safety red
│                                 │
│      BABY CRY DETECTED          │  ← headlineMedium, safety red, uppercase
│                                 │
│      Masking Stopped            │  ← bodyLarge, textPrimary
│                                 │
│                                 │
│  [          Dismiss          ]  │  ← safety red button
└─────────────────────────────────┘
```

**Smoke Alarm variant:** Same layout. Icon changes to flame/alarm. Text: "SMOKE ALARM DETECTED".

**Rules:**
- Wakes screen from lock state (`USE_FULL_SCREEN_INTENT`)
- No auto-dismiss — user must tap Dismiss
- Masking does not resume until dismissed + confidence drops below 50% for 30s
- Logged with timestamp

---

### Screen 10 — Morning Summary

**Shown:** First time user opens app after a sleep session ends (or after Wake Time).

```
┌─────────────────────────────────┐
│                                 │
│  Good Morning ☀️                │  ← headlineMedium, textPrimary
│  Last night at a glance         │  ← bodyMedium, textSecondary
│                                 │
│  ┌─────────────────────────┐    │
│  │  Protected    7h 48m    │    │  ← labelSmall + titleLarge, green
│  ├─────────────────────────┤    │
│  │  Events    Masking      │    │
│  │    4         3  18 min  │    │  ← numbers prominent
│  ├─────────────────────────┤    │
│  │  Interruptions   0      │    │  ← 0 = good, green; >0 = warning amber
│  └─────────────────────────┘    │
│                                 │
│  [        View Details      ]   │  ← primary button → Session Details
│                                 │
└─────────────────────────────────┘
```

**Protected time:** `green` colour — positive metric.
**Interruptions = 0:** Show in `green`. Interruptions > 0: show in `warning` amber.
**View Details:** Primary amber button → navigates to Session Details screen.
**Shown once per session** — dismissed after viewing or tapping View Details.

---

### Screen 11 — Session Details

**Tab:** Activity
**Back arrow:** top left → Activity list

```
┌─────────────────────────────────┐
│  ←  Session Details             │  ← titleLarge
│                                 │
│  06:03  Garbage Collection      │  ← time + event name
│         Score 88                │  ← score, primary amber
│         🟢 Brown Noise  18%     │  ← sound + volume bar
│         Duration 2m 20s         │  ← duration, textSecondary
│                                 │
│  06:11  Motorcycle              │
│         Ignored                 │  ← below threshold, no masking
│                                 │
│  06:27  Dog Barking             │
│         Masked                  │
│         Duration 1m 05s         │
│                                 │
└─────────────────────────────────┘
```

**Event rows:**
- Time: `labelSmall` `textSecondary`
- Event name: `bodyLarge` `textPrimary`
- Score: `labelSmall` `primary` amber
- Action — Masked: green indicator + sound name + volume bar + duration
- Action — Ignored: `textSecondary` "Ignored", no volume bar

**Volume bar:** Mini inline bar, `primary` amber fill, shows masking volume %.

---

### Screen 12 — Settings

**Tab:** Settings

```
┌─────────────────────────────────┐
│  Settings                       │  ← titleLarge
│                                 │
│  Sleep Schedule          >      │  ← row → sleep/wake time picker
│  Protection Sound        >      │  ← row → sound selector
│  Maximum Volume        70% >    │  ← row → volume slider
│  Sensitivity        Medium >    │  ← row → Low/Medium/High selector
│  Auto Stop          60 sec >    │  ← row → duration slider
│  Battery Optimisation Allowed > │  ← row → system settings
│  About Owlen          v1.0.0 >  │  ← row → about screen
│                                 │
│  ─────────────────────────      │
│  [Home]   [Activity]  [Settings]│
└─────────────────────────────────┘
```

**Row style:** `bodyLarge` `textPrimary` label + value/chevron trailing. `surfaceVar` background rows separated by `divider`. Each row taps into a detail screen or bottom sheet.

**No inline controls on this screen** — all editing happens in sub-screens or bottom sheets. Settings screen is a menu, not a form.

---

## 5. Key Principles Panel (Reference)

From the design — these are the product principles visible in the design image:

| Principle | Detail |
|---|---|
| One tap to protect | Single toggle on Home |
| Works in the background | Foreground service, screen off |
| Only reacts when needed | Reactive masking, never continuous |
| Critical alerts always break through | Full-screen Safety Alerts |
| Morning summary in < 10 seconds | Glanceable Morning Summary screen |
| Daily interaction time < 30 seconds | Design constraint for all screens |

---

## 6. Navigation Rules

- Bottom nav (3 tabs) appears **only after onboarding is complete**
- Onboarding, Permissions, Setup, and Calibration are **full-screen, no bottom nav**
- Protection Active screen **hides bottom nav** — full focus on active state
- Morning Summary appears **over Home** as a modal on first open after session
- Safety Alerts appear **over everything** including lock screen
- Event Detected overlay appears **over Active screen** for 5 seconds then auto-dismisses

---

## 7. Empty & Error States

| State | Screen | Handling |
|---|---|---|
| No session history | Activity tab | "No sessions yet. Start your first night of protection." |
| Microphone denied | Permissions | Row stays uncheckmarked, Continue disabled |
| Calibration obstructed | Calibration | Warning text below dB reading, retry prompt |
| Service interrupted | Home | Banner above toggle, amber, tap → Activity |
| Zero events detected | Morning Summary | "Quiet night — no masking needed." Protected time still shown in green |
| Battery below 20% | Protection Active | Warning inline next to battery stat |

---

## 8. What Is Explicitly Out of Scope for MVP

- Light mode
- Tablet layout
- Landscape orientation (lock to portrait)
- Widgets
- Dynamic colour (Material You)
- User-imported/custom sound files (built-in set is Brown/Pink/White Noise, Fan, Rain, Ocean Waves)
- Sleep tracking or wearable integration
- Cloud sync or accounts
- Language localisation
