# Adaptive Sleep Protector — Lean PRD
**Version 1.0 · MVP**

---

## 1. Product Goal

An offline Android application that monitors environmental audio during sleep and plays masking sounds reactively — only when a detected event is likely to disturb sleep. It does not play continuously.

Success is defined in the BRD (v2.0, Section 11). The three metrics that gate MVP release are:

- Event detection accuracy ≥ 90% (≥ 80% for Garbage Collection)
- False positive rate < 5%
- Battery drain ≤ 8% over 8 hours

---

## 2. Core User Flows

### Flow 1 — First-Time Setup
```
Install → Permissions → Configuration → Calibration → Ready
```
User grants microphone permission → sets Sleep Window, Wake Time, preferred masking sound, maximum volume, and sensitivity → app runs 60s ambient calibration → Sleep Protection Mode is available.

### Flow 2 — Active Night
```
Enable Sleep Mode → Monitoring → Event detected → Score calculated →
Policy decision → [Ignore | Mask] → Condition clears → Stop masking →
Continue monitoring
```
User enables Sleep Protection Mode before sleeping. App runs as a foreground service, screen off. All decisions are automatic. User does not interact until morning unless a Safety Event fires.

### Flow 3 — Morning Review
```
Wake → Dismiss mode → Review log → Adjust settings (optional)
```
User disables Sleep Protection Mode. App displays a session summary — events detected, masking episodes, any service interruptions. User optionally adjusts settings before next night.

---

## 3. Screen Inventory

### 3.1 Home Screen

**Purpose:** Single entry point. Shows current mode status and enables/disables Sleep Protection Mode.

**Key elements:**
- Mode toggle — OFF / ACTIVE
- Current configuration summary (Sleep Window, masking sound, sensitivity)
- Last session summary (tap to expand)
- Settings shortcut

**Behaviour:**
- Toggle to ACTIVE triggers the pre-sleep calibration flow (see 3.3) if not yet calibrated today
- If microphone permission is not granted, toggle is disabled with inline prompt
- While ACTIVE, screen dims to minimal UI — only mode indicator and emergency disable button visible
- If the foreground service was previously interrupted, a banner appears above the toggle: "Sleep Protection was interrupted at [time]. Tap to review."

---

### 3.2 Onboarding Screen (First Launch Only)

**Purpose:** Collect permissions and explain the core concept before setup.

**Key elements:**
- Single explanatory statement: what the app does and does not do (reactive, not continuous)
- Microphone permission request
- Battery optimisation exemption prompt — direct user to system settings to whitelist the app; show OEM-specific guidance for Xiaomi, Oppo, OnePlus

**Behaviour:**
- If microphone permission is denied, app cannot proceed — show persistent prompt, no workaround
- Battery optimisation exemption is recommended but not mandatory — user can skip with acknowledgement: "Sleep Protection may be interrupted on some devices without this setting."
- Onboarding does not repeat after completion; accessible via Settings > About if needed

---

### 3.3 Calibration Screen

**Purpose:** Sample ambient noise floor before Sleep Protection Mode activates.

**Key elements:**
- Progress indicator (60 second countdown)
- Live dB level indicator
- Skip option

**Behaviour:**
- Runs automatically when user activates Sleep Protection Mode for the first time each day
- Measures RMS audio level over 60 seconds and stores as the dynamic floor for that session
- If RMS stays below 20 dB for 10 consecutive seconds during calibration, show obstruction warning: "Microphone may be obstructed. Reposition your device and retry." Calibration pauses until user retries or dismisses
- If user skips calibration, static floor (default 50 dB, configurable) is used for that session
- After calibration completes, app transitions directly to active monitoring — no additional confirmation needed

---

### 3.4 Settings Screen

**Purpose:** All user-configurable parameters in one place.

**Sleep Settings**
- Sleep Window — start time and end time (time pickers); default 10:00 PM – 6:00 AM
- Wake Time — single time picker; used for masking fade-out before alarm; default matches Sleep Window end

**Audio Settings**
- Preferred Masking Sound — selector: Brown / Pink / White Noise
- Maximum Volume — slider 0–100%; default 70%
- Sound Level Floor — slider 30–70 dB; default 50 dB; label: "Adjust for your environment"

**Detection Settings**
- Sensitivity — selector: Low / Medium / High; maps to threshold offset of +15 / 0 / -15 points
- Rain Behaviour — toggle: "Treat rain as soothing — never trigger masking"; default ON

**Session Settings**
- Auto Stop Duration — slider 30–120 seconds; default 60 seconds
- Minimum Masking Duration — fixed at 30 seconds; not user-configurable in MVP

**Behaviour:**
- All settings take effect from the next Sleep Protection session; changes mid-session are ignored except Maximum Volume which applies immediately
- No save button — settings persist on change
- Settings screen is accessible from Home Screen only when Sleep Protection Mode is OFF; if user navigates here while ACTIVE, show inline notice: "Changes will apply from your next session."

---

### 3.5 Active Mode Screen

**Purpose:** Minimal UI during active monitoring. User should not be interacting with this screen.

**Key elements:**
- Mode indicator: "Protecting sleep" with subtle pulse animation
- Time elapsed
- Emergency disable button — large, accessible in the dark
- Current masking status: "Silent" or "Masking: [sound name]"

**Behaviour:**
- Screen should be off during normal operation; this screen is what the user sees if they pick up the phone
- If a Safety Event fires (Smoke Alarm or Baby Cry), screen activates regardless of lock state and displays full-screen alert: "Alert: [Event Name] detected. Masking stopped." with dismiss button
- If Wake Time is within 90 seconds, masking fades out automatically; status updates to "Masking paused — wake time approaching"
- Emergency disable immediately stops masking, stops foreground service, and returns user to Home Screen

---

### 3.6 Session Summary Screen

**Purpose:** Post-session review of what happened overnight.

**Key elements:**
- Session duration
- Events detected — list with event name, time, confidence, and action taken (Ignored / Masked)
- Masking episodes — count and total duration
- Service interruptions — if any, listed with time and duration
- Prompt: "Adjust sensitivity?" with shortcut to Settings if false positive rate seems high (> 3 masking episodes with score < 40)

**Behaviour:**
- Accessible from Home Screen after a session ends
- Data persists for 7 days; older sessions are purged automatically
- If no events were detected, show: "Quiet night. No masking required."
- If service was interrupted, highlight the interruption prominently with duration: "Protection was off for 43 minutes (1:12 AM – 1:55 AM)."

---

## 4. Notification & Alert Copy

| Trigger | Copy |
|---|---|
| Foreground service active | "Sleep Protection is active. Tap to view." |
| Masking started | "Masking started — [sound name]." |
| Masking stopped | "Masking stopped." |
| Safety Event — Smoke Alarm | "Alert: Smoke alarm detected. Masking stopped." |
| Safety Event — Baby Cry | "Alert: Baby cry detected. Masking stopped." |
| Wake time approaching | "Masking paused — wake time approaching." |
| Service interrupted | "Sleep Protection was interrupted at [time]. Tap to review." |
| Microphone obstructed | "Microphone may be obstructed. Reposition your device and retry." |
| Battery optimisation warning | "Sleep Protection may be interrupted on some devices without this setting." |
| Mid-session settings change | "Changes will apply from your next session." |

---

## 5. Configuration Logic

How FR-8 settings interact with the scoring and policy system:

**Sleep Window** defines the time band where TimeWeight = 100. Outside the window, TimeWeight = 30. Binary — no gradual transition.

**Wake Time** triggers a masking fade-out 90 seconds before the set time. Policy Engine suspends until 120 seconds after Wake Time. If Wake Time is not set, this feature is inactive.

**Sensitivity** shifts all Policy Engine thresholds by a fixed offset:
- High: thresholds − 15 (triggers masking more readily)
- Medium: no offset (default)
- Low: thresholds + 15 (requires stronger signal to trigger)

**Maximum Volume** caps the output of all masking audio regardless of policy action. If the policy engine calls for max volume but Maximum Volume is set to 50%, output is capped at 50%.

**Sound Level Floor** sets the dB value that maps to 0 in sound level normalisation. Calibration overrides this for the session if run. If calibration is skipped, this value is used directly.

**Rain Behaviour toggle ON** removes Rain from the event classes that feed into the Policy Engine. Rain is still detected and logged but action is always Ignore.

**Auto Stop Duration** defines how long the score must remain below 20 before masking stops. Default 60 seconds.

---

## 6. Open Questions

Decisions deliberately deferred — dev lead to resolve or escalate:

| # | Question | Impact |
|---|---|---|
| 1 | Which Android API level is the minimum target? | Affects foreground service type declaration and broadcast availability |
| 2 | Does the MLP classifier run on CPU or NPU? | Affects latency budget and battery drain |
| 3 | Are audio samples for Garbage Collection collected internally or sourced from FreeSound? | Affects model readiness timeline |
| 4 | Is the 7-day session log stored in Room DB or flat file? | Minor; flat file is simpler for MVP |
| 5 | Does the Safety Event full-screen alert bypass the lock screen? | Requires `USE_FULL_SCREEN_INTENT` permission; behaviour varies by OEM |
| 6 | What is the fallback if `ACTION_ALARM_CHANGED` is unavailable and user has not set Wake Time? | Currently no alarm protection — needs a default UX decision |
