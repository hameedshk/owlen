# Owlen — MVP Test Plan
**Version 1.0 · Android Only · Offline**

---

## 1. Purpose

Define the test cases, measurement methods, and pass/fail criteria that gate MVP release. All three release gates must pass before Owlen ships.

**MVP Release Gates:**

| Gate | Criterion |
|---|---|
| G1 | Event detection accuracy ≥ 90% overall; ≥ 80% for Garbage Collection |
| G2 | False positive rate < 5% |
| G3 | Battery drain ≤ 8% over 8 hours |

---

## 2. Test Environment

**Primary test device:** Samsung Galaxy A32 (Snapdragon 680, 4,000 mAh, Android 12)

**Secondary test device:** Xiaomi Redmi Note 11 (Snapdragon 680, Android 12) — used for OEM-specific tests only

**Conditions for all tests unless stated otherwise:**
- App installed as release APK (not debug build)
- Screen off
- No other apps active in foreground
- Wi-Fi off
- Mobile data off
- Device unplugged (battery tests only)
- Device plugged in (accuracy and safety tests)

---

## 3. Test Cases

---

### TC-01 — Event Detection Accuracy

**Release gate:** G1

**Objective:** Verify the classifier meets per-class accuracy targets on a held-out audio dataset.

**Test dataset:**
- 100 audio samples per event class
- Samples sourced from FreeSound, ESC-50, and Google AudioSet
- Garbage Collection samples: composite recordings (truck engine + hydraulic + metal impact)
- All samples trimmed to 3–5 seconds, 16kHz mono WAV
- Dataset must not overlap with training data

**Procedure:**
1. Feed each sample through the Feature Extraction + Event Detector pipeline in isolation (not via live microphone)
2. Record predicted event label and confidence for each sample
3. Compare predicted label against ground truth label
4. Calculate per-class accuracy and overall accuracy

**Measurement:**

```
Per-class accuracy = Correct predictions / Total samples × 100
Overall accuracy = Total correct predictions / Total samples × 100
```

**Pass criteria:**

| Event | Minimum Accuracy |
|---|---|
| Garbage Collection | ≥ 80% |
| All other classes | ≥ 90% |
| Overall | ≥ 90% |

**Fail action:** Review confusion matrix. If a class is consistently misclassified as another specific class, retrain MLP with additional samples for that pair. Re-run TC-01 after retraining.

---

### TC-02 — False Positive Rate

**Release gate:** G2

**Objective:** Verify the system does not trigger masking on non-event audio.

**Test dataset:**
- 4 hours of ambient audio recordings from a quiet Indian residential environment at night (street ambience, distant traffic, fan noise, AC hum)
- Recordings must contain no labelled event-class sounds

**Procedure:**
1. Play the 4-hour recording through a speaker placed 1 metre from the test device
2. Run Owlen in Sleep Protection Mode (Sensitivity: Medium, Sleep Window covering full 4 hours)
3. Record every masking episode triggered during playback
4. Count total masking triggers

**Measurement:**

```
False positive rate = Masking triggers / Total inference windows × 100
Inference windows = 4 hours × 3600s / 1s per window = 14,400 windows
```

**Pass criteria:** False positive rate < 5% (fewer than 720 spurious triggers over 4 hours)

**Fail action:** Review event log for which class is triggering false positives. Adjust event weight or confidence threshold for that class. Re-run TC-02.

---

### TC-03 — Battery Drain

**Release gate:** G3

**Objective:** Verify Owlen consumes ≤ 8% battery over 8 hours of continuous Sleep Protection Mode.

**Procedure:**
1. Charge device to 100%
2. Disconnect charger
3. Enable Sleep Protection Mode (Sensitivity: Medium, no masking audio playing)
4. Leave device screen off for 8 hours
5. Record battery level at end of 8 hours

**Pass criteria:** Battery level ≥ 92% after 8 hours

**Additional check:** Confirm the foreground service was not killed during the 8 hours by reviewing the interruption log (FR-7.4). If the service was killed and restarted, the test is invalid — investigate OEM battery optimiser interference and re-run after whitelisting.

**Fail action:** Profile battery usage via Android Battery Historian. Identify top drain source (microphone, inference, wake lock). Optimise and re-run TC-03.

---

### TC-04 — Safety Rules — Smoke Alarm

**Objective:** Verify Smoke Alarm detection immediately stops masking with no exception.

**Procedure:**
1. Start Owlen in Sleep Protection Mode with masking audio actively playing
2. Play a Smoke Alarm audio sample at 75 dB SPL from a speaker 1 metre from device
3. Observe app behaviour within 500ms of playback start

**Pass criteria:**
- Masking audio stops immediately (no fade out)
- Full-screen alert displays: "Alert: Smoke alarm detected. Masking stopped."
- Policy Engine does not restart masking until alert is dismissed and confidence drops below 50% for 30 seconds
- Event is logged with timestamp

**Fail action:** Blocker — do not ship until resolved.

---

### TC-05 — Safety Rules — Baby Cry

**Objective:** Verify Baby Cry detection immediately stops masking with no exception.

**Procedure:**
1. Start Owlen in Sleep Protection Mode with masking audio actively playing
2. Play a Baby Cry audio sample at 70 dB SPL from a speaker 1 metre from device
3. Observe app behaviour within 500ms of playback start

**Pass criteria:**
- Masking audio stops immediately (no fade out)
- Full-screen alert displays: "Alert: Baby cry detected. Masking stopped."
- Policy Engine does not restart masking until alert is dismissed and confidence drops below 50% for 30 seconds
- Event is logged with timestamp

**Fail action:** Blocker — do not ship until resolved.

---

### TC-06 — Detector Gate (Feedback Loop Prevention)

**Objective:** Verify masking audio does not trigger false event detection.

**Procedure:**
1. Start Owlen in Sleep Protection Mode, masking audio playing at 70% maximum volume (Brown Noise)
2. Monitor event detection log for 30 minutes
3. Record any events detected while masking is active

**Pass criteria:** Zero events detected during the 5-second detector gate window after masking starts. No masking-triggered volume escalation occurs during the 30-minute window.

**Fail action:** Verify FR-3.4 detector gate is implemented. Check gate duration is correctly suppressing inference. Re-run after fix.

---

### TC-07 — Foreground Service Interruption Logging (OEM)

**Test device:** Xiaomi Redmi Note 11 (battery optimiser aggressive mode, app NOT whitelisted)

**Objective:** Verify service interruption is detected, logged, and surfaced to user on restart.

**Procedure:**
1. Enable Sleep Protection Mode
2. Force-stop the app via system battery optimiser after 10 minutes (simulate OEM kill)
3. Relaunch the app manually

**Pass criteria:**
- Home Screen displays banner: "Sleep Protection was interrupted at [time]. Tap to review."
- Session Summary shows interruption with correct start time, end time, and duration
- Log entry persists for 7 days

**Fail action:** Review foreground service declaration and wake lock implementation. Re-run after fix.

---

### TC-08 — Wake Time Masking Fade-Out

**Objective:** Verify masking fades out correctly before the user-declared Wake Time.

**Procedure:**
1. Set Wake Time to 3 minutes from current time
2. Start Owlen in Sleep Protection Mode with masking audio actively playing
3. Observe app behaviour at T-90 seconds before Wake Time

**Pass criteria:**
- Masking volume fades to zero over 10 seconds starting at T-90s
- Active Mode Screen status updates to: "Masking paused — wake time approaching."
- Notification displays: "Masking paused — wake time approaching."
- Policy Engine remains suspended until T+120s after Wake Time

**Fail action:** Review Wake Time fade-out logic and Policy Engine suspension timer. Re-run after fix.

---

### TC-09 — Ambient Baseline Calibration

**Objective:** Verify calibration correctly measures ambient floor and overrides static default.

**Procedure:**
1. Place device in a room with known ambient noise level (measure independently with a calibrated dB meter)
2. Run calibration flow
3. Verify the captured baseline in app debug log matches the independently measured level within ±3 dB

**Pass criteria:** Captured baseline within ±3 dB of independently measured ambient level. Static floor default (50 dB) is not used when calibration completes successfully.

**Fail action:** Review RMS calculation in Feature Extraction pipeline. Re-run after fix.

---

### TC-10 — Microphone Obstruction Warning

**Objective:** Verify obstruction warning fires when microphone is covered during calibration.

**Procedure:**
1. Cover the device microphone completely with foam or cloth
2. Start calibration flow
3. Observe app behaviour after 10 seconds

**Pass criteria:**
- Warning displays within 10–15 seconds: "Microphone may be obstructed. Reposition your device and retry."
- Calibration pauses and does not complete
- Sleep Protection Mode does not activate until user retries or dismisses

**Fail action:** Review RMS threshold detection in FR-3.5. Re-run after fix.

---

## 4. Test Execution Order

Run in this sequence. Blocker failures (TC-04, TC-05) stop execution until resolved.

| Order | Test | Type | Blocker |
|---|---|---|---|
| 1 | TC-09 Calibration | Functional | No |
| 2 | TC-10 Obstruction Warning | Functional | No |
| 3 | TC-04 Safety — Smoke Alarm | Safety | **Yes** |
| 4 | TC-05 Safety — Baby Cry | Safety | **Yes** |
| 5 | TC-06 Detector Gate | Functional | No |
| 6 | TC-08 Wake Time Fade-Out | Functional | No |
| 7 | TC-07 Service Interruption (OEM) | Reliability | No |
| 8 | TC-01 Detection Accuracy | Model | No |
| 9 | TC-02 False Positive Rate | Model | No |
| 10 | TC-03 Battery Drain | Performance | No |

---

## 5. Pass/Fail Summary

All three release gates must pass for MVP ship decision.

| Gate | Test Cases | Status |
|---|---|---|
| G1 — Detection Accuracy | TC-01 | — |
| G2 — False Positive Rate | TC-02 | — |
| G3 — Battery Drain | TC-03 | — |
| Safety (non-gate but blocker) | TC-04, TC-05 | — |

---

## 6. Out of Scope for MVP Testing

- Multi-device or multi-room scenarios
- iOS
- Cloud connectivity or latency
- Accessibility testing
- Localisation
- Performance under concurrent app load
- Gradual ambient noise creep detection
