# Owlen — Features

A reference list of what Owlen actually does, based on reading the current source code (not just the original spec docs). See `CLAUDE.md` for the architecture/stack rules this code follows.

## What it is

Owlen is an offline Android sleep-protection app. It monitors ambient audio while you sleep, classifies what it hears on-device, and reactively plays a masking sound only when a detected event is likely to disturb your sleep. It does not play masking sound continuously, and it never makes a network call.

## Core reactive protection

- Continuous background audio monitoring via a foreground service — but masking only plays when a disturbance is actually detected.
- On-device YAMNet (TensorFlow Lite) audio classification, mapped to 24 event classes: speech, shouting, baby cry, snoring, dog, cat, bird, music, wind, thunder, rain, aircraft, traffic, garbage collection, siren, car horn, motorcycle, door knock, glass break, smoke alarm, car alarm, alarm clock, construction, fireworks, plus custom/unknown.
- Disturbance scoring: a 0–100 score combining event type, measured ambient sound level (dB), and time-of-night (inside vs. outside the configured sleep window).
- Policy engine: decides to ignore, start masking, adjust masking volume, or stop masking, based on the score and the user's sensitivity setting.
- A 5-second "detector gate" after any action, so the app doesn't hear its own masking sound and re-trigger on itself.

## Masking sounds

- 6 masking sounds, all procedurally synthesized on-device (no bundled audio files): White Noise, Pink Noise, Brown Noise, Fan, Rain, Ocean Waves.
- Fade-in / fade-out playback, with a minimum 30-second play duration to avoid flicker.
- Auto-stop once the disturbance score drops and stays low.
- Wake-time guard: masking stops near the user's configured wake time so it never masks their alarm.
- Optional rain-specific behavior toggle, configurable sound-level floor, and sensitivity offset.

## Custom sound enrollment

- Users can enroll their own sounds (e.g. a specific pet, appliance, or person) by recording 3 takes.
- The app builds an averaged, L2-normalized embedding "prototype" from those takes, with a consistency check across takes.
- Live audio is matched against enrolled prototypes via cosine similarity; a custom match can override the generic YAMNet classification — but safety events always win regardless of any custom match.
- **Note:** this feature is fully built and tested in code, but it contradicts `UX_Spec_v2.md`, which explicitly lists "user-imported/custom sounds" as out of MVP scope. Docs haven't caught up to this feature yet.

## Safety system (non-negotiable)

- Smoke Alarm and Baby Cry detections bypass scoring/policy entirely and never trigger masking.
- Either event immediately stops any masking currently playing.
- Triggers a full-screen, high-priority alert that displays even over the lock screen, using its own notification channel (lights + vibration).

## Session logging & history

- Every event, action, masking session, safety alert, and interruption is logged to a flat JSON file per sleep session (no database).
- Detects "interrupted" sessions (e.g. the app/service was killed unexpectedly by the OS) and recovers gracefully on next launch.
- 7-day automatic log retention/purge.
- Morning-summary and session-details screens are built from this log data.

## Calibration & setup

- A calibration flow that measures the room's ambient noise floor and detects microphone obstruction.
- Onboarding, permissions request (microphone, notifications), and initial setup screens.

## Settings

Persisted via DataStore Preferences:
- Sleep window (start/end time)
- Wake time
- Sensitivity (Low / Medium / High)
- Max masking volume
- Preferred default masking sound
- Auto-stop duration
- Rain behavior toggle
- Sound-level floor
- Last-calibration tracking

## UI

Full Jetpack Compose UI (dark theme, amber/green/red design tokens per `CLAUDE.md`):
Splash → Onboarding → Permissions → Setup → Calibration → Home/Dashboard → Active Monitoring → Event-Detected Overlay → Safety Alert → Morning Summary → Session Details/History → Settings → Sound Enrollment.
