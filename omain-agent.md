Read .claude/agents/ui-agent.md fully before doing anything.

Then read:
1. CLAUDE.md — design tokens and UI rules
2. docs/UX_Spec_v2.md — all 12 screens
3. service/ServiceRepository.kt — StateFlow your screens observe
4. data/settings/SettingsRepository.kt — settings Flow your screens observe
5. presentation/ui/theme/Color.kt — confirm design tokens

Build all 12 screens in this exact order.
After every screen: ./gradlew assembleDebug — fix before moving on.

─────────────────────────────────────────
BUILD ORDER
─────────────────────────────────────────

1.  SplashScreen.kt
2.  OnboardingScreen.kt      (3-step pager)
3.  PermissionsScreen.kt
4.  SetupScreen.kt           (3-step: SleepTime → Sound → Sensitivity)
5.  CalibrationScreen.kt     (countdown + live dB bar)
6.  HomeScreen.kt            (OFF state + ON state)
7.  ActiveModeScreen.kt
8.  EventDetectedOverlay.kt  (bottom sheet, 5s auto-dismiss)
9.  SafetyAlertScreen.kt     (full screen, USE_FULL_SCREEN_INTENT)
10. MorningSummaryScreen.kt
11. SessionDetailsScreen.kt
12. SettingsScreen.kt

─────────────────────────────────────────
NAVIGATION
─────────────────────────────────────────

Two navigation graphs:

Onboarding graph (no bottom nav):
splash → onboarding → permissions → setup → calibration → home

Main graph (3-tab bottom nav):
  Tab Home:     home ↔ active_mode ↔ event_overlay ↔ safety_alert ↔ morning_summary
  Tab Activity: session_list → session_details
  Tab Settings: settings

Store onboarding completion in DataStore.
Check on every app launch — skip onboarding graph if already complete.

─────────────────────────────────────────
GLOBAL RULES — APPLY TO EVERY SCREEN
─────────────────────────────────────────

- Background: #0F1117 on every screen, no exceptions
- Font: Inter (Google Fonts) everywhere — never system default
- Minimum tap target: 48×48dp on every interactive element
- Portrait only — set in AndroidManifest activity declaration
- No light mode — single dark theme only
- Primary CTA buttons: #F5A623 amber, #0F1117 text, 12dp radius, 64dp height
- STOP button (Active Mode only): #FF5252 red — not amber
- Active protection state: #4CAF50 green shield with pulse animation
  (scale 1.0→1.06→1.0, 3s loop, EaseInOut)
- Safety alert screens: #B71C1C background, full screen, bypasses lock screen
- Bottom nav hidden on: Onboarding, Permissions, Setup, Calibration, Active Mode
- Bottom nav shown on: Home, Activity, Settings

─────────────────────────────────────────
SCREEN DETAILS
─────────────────────────────────────────

SplashScreen:
- Owl mascot emoji/icon centred, 100dp
- "OWLEN" headlineLarge, #E8E8E8
- "Protecting peaceful sleep" bodyMedium, #8A8FA8
- Auto-navigate after 2s — onboarding if first launch, home if returning
- Background gradient: #0F1117 top → #1A1D27 bottom

OnboardingScreen:
- 3-step horizontal pager
- Dot indicator: active dot #F5A623, inactive #252836
- CTA: "Next →" on steps 1-2, "Get Started" on step 3
- Step 1: owl mascot + "Sleep peacefully." headline + description body
- Steps 2-3: same layout, different content

PermissionsScreen:
- Progress bar at top (green fill, step 1 of onboarding flow)
- Two permission rows in surface cards:
  Microphone + Battery Optimisation
- Each row: icon left + title + description + green checkmark when granted
- Continue button: disabled until both granted

SetupScreen:
- 3-step pager with progress bar
- Step 1 Sleep Time: two time pickers (Sleep + Wake), large digit display in surfaceVar cards
- Step 2 Sound: radio options Brown/Pink/White Noise, selected = amber fill + amber border
- Step 3 Sensitivity: radio options Low/Medium/High, default Medium selected
- CTA: "Next" steps 1-2, "Continue" step 3

CalibrationScreen:
- "Preparing..." title, "Listening to your room" subtitle
- Circular progress ring: #4CAF50 green, 6dp stroke, 120dp diameter, anticlockwise drain over 60s
- Countdown number inside ring: 40sp bold
- "Current Noise" label + live dB value updating every 500ms
- "Keep your phone nearby and stay quiet." hint
- Skip link at bottom

HomeScreen — OFF state:
- Top bar: owl icon + "Owlen" + menu icon
- Time-based greeting: "Good Morning/Afternoon/Evening"
- "Protection" label + large pill toggle (OFF = #3D4055 track)
- Sleep and Wake times displayed
- Current sound and sensitivity shown
- Settings text link

HomeScreen — ON state:
- Toggle: #4CAF50 green track
- Shield icon replaced with pulsing green shield
- "Protecting Sleep" in green
- All other elements same layout

ActiveModeScreen:
- No bottom nav
- Green shield with checkmark, 80dp, pulsing
- "Protecting Sleep" headlineMedium
- "Monitoring..." subtitle
- Blinking green dot + "Microphone Active"
- Started time + Battery % in two columns
- STOP button: #FF5252 red, full width, 64dp height, bottom of screen

EventDetectedOverlay:
- Bottom sheet over Active screen
- Amber left border 4dp
- Event-specific icon (truck, motorcycle, dog etc.) in amber
- Event name + "Detected"
- "Disturbance Score" label + score number in amber 40sp bold
- "Playing Brown/Pink/White Noise" + volume progress bar in amber
- "Auto dismiss after X seconds" countdown
- Auto-dismisses after 5 seconds — no user action required
- If screen is off: heads-up notification only, do not wake screen

SafetyAlertScreen:
- Full screen, #B71C1C background
- Alert icon 80dp in #FF5252
- Event name in uppercase, #FF5252, headlineMedium
- "Masking Stopped" body text
- Dismiss button: #FF5252
- Wakes screen from lock (USE_FULL_SCREEN_INTENT)
- No auto-dismiss — user must tap
- Baby Cry variant: bell icon
- Smoke Alarm variant: flame icon

MorningSummaryScreen:
- "Good Morning ☀" heading
- "Last night at a glance" subtitle
- Stats card:
  Protected time in #4CAF50 green
  Events / Masking count / Duration
  Interruptions: green if 0, amber if >0
- "View Details" primary amber button → SessionDetailsScreen

SessionDetailsScreen:
- Back arrow → Activity tab
- Event list: time + name + score + action taken
- Masked events: green dot + sound name + inline volume bar + duration
- Ignored events: "Ignored" in #8A8FA8
- Volume bar: amber fill, shows masking %

SettingsScreen:
- Title "Settings"
- Menu rows only — no inline controls
- Each row: label left + value/chevron right
- Rows: Sleep Schedule, Protection Sound, Maximum Volume,
  Sensitivity, Auto Stop, Battery Optimisation, About Owlen v1.0.0
- All editing in sub-screens or bottom sheets

─────────────────────────────────────────
VIEWMODELS
─────────────────────────────────────────

After all screens are built, implement:

HomeViewModel:
  Observes ServiceRepository.state: StateFlow<ServiceState>
  Exposes: isProtectionActive: Boolean, sessionStartTime: Long?, lastSession: SessionSummary?
  Functions: startProtection(), stopProtection()
  startProtection() calls SleepProtectionService.start(context)
  stopProtection() calls SleepProtectionService.stop(context)

SettingsViewModel:
  Observes SettingsRepository.settings: Flow<SleepSettings>
  One suspend update function per SleepSettings field
  Called directly from Settings sub-screens

SessionViewModel:
  suspend fun loadSessions() → calls SessionLogger.getRecentSessions()
  suspend fun loadSession(id: String) → calls SessionLogger.getSessionDetails()
  Exposes: sessions: List<SessionSummary>, selectedSession: SessionLog?

─────────────────────────────────────────
FINAL CHECK
─────────────────────────────────────────

Before finishing, verify:

1. ./gradlew assembleDebug — build is clean

2. Install on physical device and verify:
   - All 12 screens render correctly
   - Bottom nav appears only after onboarding completes
   - Home toggle starts and stops SleepProtectionService
   - Safety alert wakes screen from lock state
   - Event overlay auto-dismisses after exactly 5 seconds
   - Morning Summary appears on first open after session ends
   - Pulse animation plays correctly on Active Mode screen

3. grep -r "import android\." app/src/main/java/com/owlen/app/domain/
   Must still be empty — UI work must not pollute domain layer.

4. ./gradlew test — all existing tests still pass.

Report the result of all four checks.