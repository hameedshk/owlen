# Owlen — Manual Test Guide

## Setup (do once)

1. Build & install release APK on **Samsung Galaxy A32**
2. Grant microphone permission when prompted
3. Go to **Settings > Battery > Owlen** → disable battery optimization (or use the in-app prompt)
4. Complete onboarding: Splash → Onboarding → Permissions → Setup → Calibration → Home

---

## TC-09 — Ambient Calibration

**Goal:** Captured baseline matches real room level ±3 dB

1. Get a dB meter app on a second phone (or physical meter) — measure room ambient
2. On Owlen: tap the **Start Protection** toggle on Home Screen
3. If calibration is needed (first time today), it routes to **CalibrationScreen**
4. Wait for calibration to complete (~10–15 s)
5. Note the dB value shown on screen
6. **Pass:** value is within ±3 dB of reference meter

---

## TC-10 — Microphone Obstruction Warning

**Goal:** Warning appears when mic is blocked during calibration

1. Cover the mic port (bottom of phone) tightly with foam or cloth
2. Tap **Start Protection** on Home Screen to trigger CalibrationScreen
3. Wait 10–15 s
4. **Pass:** warning banner appears: *"Microphone may be obstructed. Reposition your device and retry."* and calibration does NOT complete

---

## TC-04 — Smoke Alarm Safety ⛔ Blocker

**Goal:** Masking stops instantly + full-screen alert appears

1. Start Sleep Protection and wait until **ActiveModeScreen** is showing
2. Let it run until a disturbance triggers masking (or wait ~1 min to confirm masking is active via the status indicator)
3. On a second device, play a smoke alarm WAV at loud volume ~1 m from the test phone
4. **Pass (all must be true):**
   - Masking stops within 500 ms (no fade-out)
   - **SafetyAlertScreen** appears: *"Alert: Smoke alarm detected. Masking stopped."* (must show even from lock screen)
   - Masking does NOT restart until you dismiss the alert AND the sound has been absent for 30 s
   - Event appears in **SessionDetailsScreen** with correct timestamp

---

## TC-05 — Baby Cry Safety ⛔ Blocker

**Goal:** Same as TC-04 but for baby cry

1. Start Sleep Protection and wait until **ActiveModeScreen** is showing
2. Let it run until masking is active
3. On a second device, play a baby cry WAV at ~70 dB at 1 m from the test phone
4. **Pass (all must be true):**
   - Masking stops within 500 ms (no fade-out)
   - **SafetyAlertScreen** appears: *"Alert: Baby cry detected. Masking stopped."* (must show even from lock screen)
   - Masking does NOT restart until you dismiss the alert AND the sound has been absent for 30 s
   - Event appears in **SessionDetailsScreen** with correct timestamp

---

## TC-06 — Detector Gate (No Feedback Loop)

**Goal:** Masking noise itself doesn't trigger more events

1. Go to **SettingsScreen** → set masking sound to **Brown Noise**, volume to ~70%
2. Start Sleep Protection → get to **ActiveModeScreen** with masking actively playing
3. Let it run for **30 minutes** undisturbed
4. Afterwards go to **SessionDetailsScreen** (Activity tab)
5. **Pass:** no events logged during the first 5 s after masking started; no cascading volume escalation pattern in the log

---

## TC-08 — Wake Time Fade-Out

**Goal:** Masking fades before wake time

1. Go to **SettingsScreen** → set Wake Time to **3 minutes from now**
2. Start Sleep Protection → confirm masking is active on **ActiveModeScreen**
3. Watch at T-90 s before wake time
4. **Pass (all must be true):**
   - Volume fades to zero over ~10 s starting at T-90 s
   - Status text updates to: *"Masking paused — wake time approaching."*
   - Notification bar also updates with same message
   - No new masking triggers until 2 min after wake time

---

## TC-07 — OEM Service Kill

**Test device:** Xiaomi Redmi Note 11 (do NOT whitelist from battery optimiser)

**Goal:** Interruption is detected, logged, and shown on relaunch

1. Install APK on Xiaomi Redmi Note 11
2. Start Sleep Protection, let it run 10 min
3. Go to **Settings > Battery > Manage apps** → force-stop Owlen
4. Wait 30 s, then relaunch Owlen manually
5. **Pass (all must be true):**
   - **HomeScreen** shows banner: *"Sleep Protection was interrupted at [time]. Tap to review."*
   - Tap banner → **MorningSummaryScreen** or **SessionDetailsScreen** shows interruption with correct start/end/duration
   - Log entry is still visible 7 days later

---

## TC-01 — Detection Accuracy

**Goal:** Classifier hits ≥ 90% accuracy (≥ 80% for Garbage Collection)

**Audio files needed:** 100 WAV samples per event class from FreeSound / ESC-50 / Google AudioSet (16 kHz mono, 3–5 s, no overlap with training data)

1. Play each sample through a speaker at 1 m from the test device
2. Watch **ActiveModeScreen** — the **EventDetectedOverlay** shows the detected class and confidence score
3. Log predicted class vs actual class for each sample in a spreadsheet
4. Calculate accuracy per class

**Pass criteria:**

| Class | Minimum Accuracy |
|---|---|
| Garbage Collection | 80% |
| All other classes | 90% |
| Overall | 90% |

**If failing:** review confusion matrix, identify misclassified pairs, adjust YAMNet score mapping thresholds, re-run.

---

## TC-02 — False Positive Rate

**Goal:** Fewer than 720 spurious masking triggers over 4 hours

**Audio needed:** 4-hour ambient night recording (quiet room, fan/AC hum — no labelled event sounds)

1. Play recording through a speaker at 1 m from the test device
2. Start Sleep Protection → set sensitivity to **Medium**, sleep window covering the full 4 hours
3. Let it run for 4 hours
4. Afterwards go to **SessionDetailsScreen** — count total masking events logged
5. **Pass:** ≤ 720 triggers

> Calculation: 4 h × 3,600 s / 1 s per window = 14,400 windows × 5% = 720

**If failing:** check which class is over-triggering in the log, adjust confidence threshold for that class, re-run.

---

## TC-03 — Battery Drain

**Goal:** ≤ 8% drain over 8 hours

1. Charge device to **100%**, then unplug
2. Start Sleep Protection (Sensitivity: Medium, no audio playing nearby)
3. Lock screen — leave undisturbed for **8 hours**
4. Check battery level
5. **Pass:** battery ≥ 92%

> Also check **SessionDetailsScreen** — if the service was killed mid-session, the test is invalid. Whitelist the app from battery optimisation and re-run.

---

## Test Execution Order

Run in this sequence. Fix blocker failures (TC-04, TC-05) before continuing.

| Order | Test | Screen | Audio Needed | Device | Blocker |
|---|---|---|---|---|---|
| 1 | TC-09 Calibration | CalibrationScreen | None | A32 | No |
| 2 | TC-10 Obstruction Warning | CalibrationScreen | None | A32 | No |
| 3 | TC-04 Smoke Alarm | ActiveMode → SafetyAlertScreen | Smoke alarm WAV | A32 | **Yes** |
| 4 | TC-05 Baby Cry | ActiveMode → SafetyAlertScreen | Baby cry WAV | A32 | **Yes** |
| 5 | TC-06 Detector Gate | ActiveMode → SessionDetails | None | A32 | No |
| 6 | TC-08 Wake Time Fade-Out | ActiveModeScreen | None | A32 | No |
| 7 | TC-07 OEM Service Kill | HomeScreen banner | None | Xiaomi Note 11 | No |
| 8 | TC-01 Detection Accuracy | EventDetectedOverlay | 100 samples/class | A32 | No |
| 9 | TC-02 False Positive Rate | SessionDetailsScreen | 4 h ambient recording | A32 | No |
| 10 | TC-03 Battery Drain | SessionDetailsScreen | None | A32 | No |

---

## MVP Release Gates

All three must pass before shipping.

| Gate | Criterion | Test |
|---|---|---|
| G1 | Detection accuracy ≥ 90% overall; ≥ 80% for Garbage Collection | TC-01 |
| G2 | False positive rate < 5% | TC-02 |
| G3 | Battery drain ≤ 8% over 8 hours | TC-03 |
| Safety (blocker, not a gate) | Smoke Alarm and Baby Cry always stop masking immediately | TC-04, TC-05 |
