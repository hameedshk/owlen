package com.owlen.app.presentation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.BatteryManager
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.owlen.app.domain.model.EventClass
import com.owlen.app.presentation.calibration.CalibrationViewModel
import com.owlen.app.presentation.enrollment.EnrollmentViewModel
import com.owlen.app.presentation.home.HomeViewModel
import com.owlen.app.presentation.session.SessionViewModel
import com.owlen.app.presentation.settings.SettingsViewModel
import com.owlen.app.presentation.ui.ActiveModeScreen
import com.owlen.app.presentation.ui.CalibrationScreen
import com.owlen.app.presentation.ui.EnrollmentScreen
import com.owlen.app.presentation.ui.EventDetectedOverlay
import com.owlen.app.presentation.ui.HomeScreen
import com.owlen.app.presentation.ui.MorningSummaryScreen
import com.owlen.app.presentation.ui.OnboardingScreen
import com.owlen.app.presentation.ui.PermissionsScreen
import com.owlen.app.presentation.ui.SafetyAlertScreen
import com.owlen.app.presentation.ui.SessionDetailsScreen
import com.owlen.app.presentation.ui.SettingsScreen
import com.owlen.app.presentation.ui.SetupScreen
import com.owlen.app.presentation.ui.SplashScreen
import com.owlen.app.presentation.ui.theme.Background
import com.owlen.app.presentation.ui.theme.MatteBackground
import com.owlen.app.presentation.ui.theme.Primary
import com.owlen.app.presentation.ui.theme.Stroke
import com.owlen.app.presentation.ui.theme.TextSecondary
import com.owlen.app.presentation.util.BatteryOptimization
import com.owlen.app.service.ServiceState

// Route constants
private object Routes {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val PERMISSIONS = "permissions"
    const val SETUP = "setup"
    const val CALIBRATION = "calibration"
    const val HOME = "home"
    const val ACTIVE_MODE = "active_mode"
    const val EVENT_OVERLAY = "event_overlay"
    const val SAFETY_ALERT = "safety_alert"
    const val MORNING_SUMMARY = "morning_summary"
    const val SESSION_DETAILS = "session_details"
    const val SETTINGS = "settings"
    const val ENROLLMENT = "enrollment"
}

private sealed class BottomTab(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Home : BottomTab(Routes.HOME, "Home", Icons.Rounded.Home)
    object Activity : BottomTab(Routes.SESSION_DETAILS, "Activity", Icons.Rounded.BarChart)
    object Settings : BottomTab(Routes.SETTINGS, "Settings", Icons.Rounded.Settings)
}

private val bottomTabs = listOf(BottomTab.Home, BottomTab.Activity, BottomTab.Settings)

private val routesWithBottomNav = setOf(
    Routes.HOME,
    Routes.SESSION_DETAILS,
    Routes.SETTINGS
)

private fun hasMicPermission(context: Context): Boolean =
    ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) ==
        PackageManager.PERMISSION_GRANTED

@Composable
fun OwlenNavGraph(
    onboardingCompleted: Boolean,
    onOnboardingComplete: () -> Unit = {}
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val context = LocalContext.current

    val showBottomNav = currentRoute in routesWithBottomNav

    val startDestination = if (onboardingCompleted) Routes.HOME else Routes.SPLASH

    // App-level view model: drives safety-alert navigation from any screen
    val appViewModel: HomeViewModel = hiltViewModel()
    val serviceState by appViewModel.serviceState.collectAsState()

    LaunchedEffect(serviceState) {
        if (serviceState is ServiceState.SafetyAlertActive && currentRoute != Routes.SAFETY_ALERT) {
            navController.navigate(Routes.SAFETY_ALERT) { launchSingleTop = true }
        }
    }

    // No grid texture during overnight monitoring — keep the panel truly black
    MatteBackground(showGrid = currentRoute != Routes.ACTIVE_MODE) {
        Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            if (showBottomNav) {
                NavigationBar(
                    containerColor = Color.Transparent,
                    tonalElevation = 0.dp,
                    modifier = Modifier
                        .background(Background)
                        .drawBehind {
                            drawLine(
                                color = Stroke,
                                start = Offset.Zero,
                                end = Offset(size.width, 0f),
                                strokeWidth = 1.dp.toPx()
                            )
                        }
                ) {
                    bottomTabs.forEach { tab ->
                        val selected = navBackStackEntry?.destination?.hierarchy?.any {
                            it.route == tab.route
                        } == true

                        // Amber indicator bar along the top edge of the active tab
                        val indicatorAlpha by animateFloatAsState(
                            targetValue = if (selected) 1f else 0f,
                            animationSpec = tween(200),
                            label = "navIndicator"
                        )

                        NavigationBarItem(
                            modifier = Modifier.drawBehind {
                                if (indicatorAlpha > 0f) {
                                    val barWidth = size.width * 0.5f
                                    drawRect(
                                        color = Primary.copy(alpha = indicatorAlpha),
                                        topLeft = Offset((size.width - barWidth) / 2f, 0f),
                                        size = Size(barWidth, 2.dp.toPx())
                                    )
                                }
                            },
                            selected = selected,
                            onClick = {
                                // Tabs are single screens — no per-tab stack to save/restore.
                                // (restoreState here used to resurrect screens pushed on top of
                                // Home, so tapping the Home tab never actually showed Home.)
                                navController.navigate(tab.route) {
                                    popUpTo(navController.graph.findStartDestination().id)
                                    launchSingleTop = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = tab.label
                                )
                            },
                            label = {
                                Text(
                                    text = tab.label.uppercase(),
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Primary,
                                selectedTextColor = Primary,
                                unselectedIconColor = TextSecondary,
                                unselectedTextColor = TextSecondary,
                                indicatorColor = Color.Transparent
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.SPLASH) {
                SplashScreen(
                    onNavigateNext = {
                        navController.navigate(Routes.ONBOARDING) {
                            popUpTo(Routes.SPLASH) { inclusive = true }
                        }
                    }
                )
            }

            composable(Routes.ONBOARDING) {
                OnboardingScreen(
                    onNavigateToPermissions = {
                        navController.navigate(Routes.PERMISSIONS)
                    }
                )
            }

            composable(Routes.PERMISSIONS) {
                PermissionsScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToSetup = {
                        navController.navigate(Routes.SETUP)
                    }
                )
            }

            composable(Routes.SETUP) {
                val settingsViewModel: SettingsViewModel = hiltViewModel()
                val settings by settingsViewModel.settings.collectAsState()
                SetupScreen(
                    initialSleepHour = settings.sleepWindowStartHour,
                    initialWakeHour = settings.wakeTimeHour,
                    initialWakeMinute = settings.wakeTimeMinute,
                    initialSound = settings.preferredSound,
                    initialSensitivity = settings.sensitivity,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToCalibration = {
                        navController.navigate(Routes.CALIBRATION)
                    },
                    onSaveSettings = { sleepHour, wakeHour, wakeMinute, sound, sensitivity ->
                        settingsViewModel.saveSetup(sleepHour, wakeHour, wakeMinute, sound, sensitivity)
                    }
                )
            }

            composable(
                route = "${Routes.CALIBRATION}?autoStart={autoStart}",
                arguments = listOf(
                    navArgument("autoStart") {
                        type = NavType.BoolType
                        defaultValue = false
                    }
                )
            ) { backStackEntry ->
                val autoStart = backStackEntry.arguments?.getBoolean("autoStart") ?: false
                val calibrationViewModel: CalibrationViewModel = hiltViewModel()
                val currentDb by calibrationViewModel.currentDbLevel.collectAsState()
                val obstructed by calibrationViewModel.obstructed.collectAsState()

                LaunchedEffect(Unit) {
                    if (hasMicPermission(context)) {
                        calibrationViewModel.startMeasuring()
                    }
                }

                // After a pre-sleep calibration, go straight into active monitoring
                val proceed: () -> Unit = {
                    onOnboardingComplete()
                    if (autoStart) {
                        appViewModel.setProtectionEnabled(true)
                        navController.navigate(Routes.ACTIVE_MODE) {
                            popUpTo(Routes.HOME)
                        }
                    } else {
                        navController.navigate(Routes.HOME) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }

                CalibrationScreen(
                    currentDbLevel = currentDb,
                    obstructed = obstructed,
                    onRetry = { calibrationViewModel.retry() },
                    onSkip = {
                        calibrationViewModel.skip()
                        proceed()
                    },
                    onCalibrationComplete = {
                        calibrationViewModel.finishMeasuring()
                        proceed()
                    }
                )
            }

            composable(Routes.HOME) {
                val homeViewModel: HomeViewModel = hiltViewModel()
                val isProtectionActive by homeViewModel.isProtectionActive.collectAsState()
                val settings by homeViewModel.settings.collectAsState()
                val interruption by homeViewModel.interruption.collectAsState()

                HomeScreen(
                    isProtectionActive = isProtectionActive,
                    sleepHour = settings.sleepWindowStartHour,
                    wakeHour = settings.wakeTimeHour,
                    preferredSound = settings.preferredSound,
                    sensitivity = settings.sensitivity,
                    interruptedAtMs = interruption?.interruptedAtMs,
                    onInterruptionClick = {
                        homeViewModel.dismissInterruption()
                        navController.navigate(Routes.SESSION_DETAILS)
                    },
                    onInterruptionDismiss = { homeViewModel.dismissInterruption() },
                    onProtectionToggle = { active ->
                        if (active) {
                            when {
                                !hasMicPermission(context) ->
                                    navController.navigate(Routes.PERMISSIONS)

                                // Spec: re-run ambient calibration on first activation each day
                                homeViewModel.needsCalibrationToday() ->
                                    navController.navigate("${Routes.CALIBRATION}?autoStart=true")

                                else -> {
                                    homeViewModel.setProtectionEnabled(true)
                                    navController.navigate(Routes.ACTIVE_MODE)
                                }
                            }
                        } else {
                            homeViewModel.setProtectionEnabled(false)
                        }
                    },
                    onNavigateToSettings = {
                        navController.navigate(Routes.SETTINGS)
                    },
                    onNavigateToActive = { /* handled in onProtectionToggle */ }
                )
            }

            composable(Routes.ACTIVE_MODE) {
                val homeViewModel: HomeViewModel = hiltViewModel()
                val sessionStartTime by homeViewModel.sessionStartTime.collectAsState()
                val lastDetection by homeViewModel.lastDetection.collectAsState()
                val activeSettings by homeViewModel.settings.collectAsState()
                val batteryPercent = remember {
                    val batteryManager =
                        context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
                    batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
                        .takeIf { it in 0..100 } ?: 100
                }

                val maskingState = serviceState as? ServiceState.MaskingActive

                Box(modifier = Modifier.fillMaxSize()) {
                    ActiveModeScreen(
                        sessionStartTimeMs = sessionStartTime ?: System.currentTimeMillis(),
                        batteryPercent = batteryPercent,
                        wakeTimeHour = activeSettings.wakeTimeHour,
                        wakeTimeMinute = activeSettings.wakeTimeMinute,
                        statusText = if (maskingState != null) {
                            "Masking with ${maskingState.sound.displayName}"
                        } else {
                            "Monitoring..."
                        },
                        onStopProtection = {
                            homeViewModel.setProtectionEnabled(false)
                            navController.navigate(Routes.MORNING_SUMMARY) {
                                popUpTo(Routes.HOME)
                            }
                        }
                    )

                    if (maskingState != null) {
                        val detection = lastDetection
                        EventDetectedOverlay(
                            eventClass = detection?.first?.eventClass ?: EventClass.UNKNOWN,
                            disturbanceScore = detection?.second?.score ?: 0,
                            maskingSound = maskingState.sound,
                            maskingVolume = maskingState.volume,
                            isVisible = true,
                            onDismiss = { }
                        )
                    }
                }
            }

            composable(Routes.EVENT_OVERLAY) {
                EventDetectedOverlay(
                    onDismiss = { navController.popBackStack() }
                )
            }

            composable(Routes.SAFETY_ALERT) {
                val alertEvent =
                    (serviceState as? ServiceState.SafetyAlertActive)?.event ?: EventClass.SMOKE_ALARM
                SafetyAlertScreen(
                    eventClass = alertEvent,
                    onDismiss = {
                        appViewModel.acknowledgeSafetyAlert()
                        navController.popBackStack()
                    }
                )
            }

            composable(Routes.MORNING_SUMMARY) {
                val sessionViewModel: SessionViewModel = hiltViewModel()
                val summary by sessionViewModel.latestSummary.collectAsState()
                val adjustSensitivity by sessionViewModel.adjustSensitivitySuggested.collectAsState()

                LaunchedEffect(Unit) { sessionViewModel.refresh() }

                val protectedMs = summary?.protectedDurationMs ?: 0L
                MorningSummaryScreen(
                    protectedHours = (protectedMs / 3_600_000L).toInt(),
                    protectedMinutes = ((protectedMs % 3_600_000L) / 60_000L).toInt(),
                    eventCount = summary?.eventCount ?: 0,
                    maskingCount = summary?.maskedCount ?: 0,
                    maskingDurationMinutes = ((summary?.totalMaskingDurationMs ?: 0L) / 60_000L).toInt(),
                    interruptionCount = summary?.interruptionCount ?: 0,
                    adjustSensitivitySuggested = adjustSensitivity,
                    onAdjustSensitivity = { navController.navigate(Routes.SETTINGS) },
                    onViewDetails = {
                        navController.navigate(Routes.SESSION_DETAILS)
                    },
                    onDismiss = {
                        navController.popBackStack(Routes.HOME, inclusive = false)
                    }
                )
            }

            composable(Routes.SESSION_DETAILS) {
                val sessionViewModel: SessionViewModel = hiltViewModel()
                val events by sessionViewModel.latestSessionEvents.collectAsState()

                LaunchedEffect(Unit) { sessionViewModel.refresh() }

                SessionDetailsScreen(
                    events = events,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Routes.SETTINGS) {
                val settingsViewModel: SettingsViewModel = hiltViewModel()
                val settings by settingsViewModel.settings.collectAsState()
                val lifecycleOwner = LocalLifecycleOwner.current
                var batteryExempt by remember {
                    mutableStateOf(BatteryOptimization.isExempt(context))
                }

                // Re-check when returning from the system battery dialog
                DisposableEffect(lifecycleOwner) {
                    val observer = LifecycleEventObserver { _, event ->
                        if (event == Lifecycle.Event.ON_RESUME) {
                            batteryExempt = BatteryOptimization.isExempt(context)
                        }
                    }
                    lifecycleOwner.lifecycle.addObserver(observer)
                    onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
                }

                val customPrototypes by settingsViewModel.customPrototypes.collectAsState()

                SettingsScreen(
                    sleepHour = settings.sleepWindowStartHour,
                    wakeHour = settings.wakeTimeHour,
                    preferredSound = settings.preferredSound,
                    maxVolumePct = (settings.maxVolume * 100).toInt(),
                    sensitivity = settings.sensitivity,
                    autoStopSeconds = (settings.autoStopDurationMs / 1000L).toInt(),
                    rainBehaviourEnabled = settings.rainBehaviourEnabled,
                    soundLevelFloorDb = settings.soundLevelFloor.toInt(),
                    batteryOptimisationAllowed = batteryExempt,
                    customPrototypes = customPrototypes,
                    onNavigateToEnrollment = { navController.navigate(Routes.ENROLLMENT) },
                    onDeletePrototype = { settingsViewModel.deletePrototype(it) },
                    onNavigateToSleepSchedule = { navController.navigate(Routes.SETUP) },
                    onSoundSelected = { settingsViewModel.updatePreferredSound(it) },
                    onMaxVolumeChanged = { settingsViewModel.updateMaxVolume(it / 100f) },
                    onSensitivitySelected = { settingsViewModel.updateSensitivity(it) },
                    onAutoStopChanged = { settingsViewModel.updateAutoStopDuration(it * 1000L) },
                    onRainBehaviourChanged = { settingsViewModel.updateRainBehaviour(it) },
                    onSoundFloorChanged = { settingsViewModel.updateSoundLevelFloor(it.toFloat()) },
                    onBatteryOptimisationClick = {
                        if (!batteryExempt) BatteryOptimization.requestExemption(context)
                    }
                )
            }

            composable(Routes.ENROLLMENT) {
                val enrollmentViewModel: EnrollmentViewModel = hiltViewModel()
                val uiState by enrollmentViewModel.uiState.collectAsState()
                val monitoringActive by enrollmentViewModel.isMonitoringActive.collectAsState()

                EnrollmentScreen(
                    name = uiState.name,
                    takesCompleted = uiState.takesCompleted,
                    phase = uiState.phase,
                    liveDb = uiState.liveDb,
                    monitoringActive = monitoringActive,
                    onNameChanged = { enrollmentViewModel.onNameChanged(it) },
                    onRecordTake = {
                        if (hasMicPermission(context)) {
                            enrollmentViewModel.recordTake()
                        } else {
                            navController.navigate(Routes.PERMISSIONS)
                        }
                    },
                    onRestart = { enrollmentViewModel.restart() },
                    onDone = { navController.popBackStack() }
                )
            }
        }
        }
    }
}
