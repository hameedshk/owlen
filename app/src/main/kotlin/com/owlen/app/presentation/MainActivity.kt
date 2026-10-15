package com.owlen.app.presentation

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.owlen.app.presentation.ui.theme.OwlenTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private val android.content.Context.onboardingDataStore: DataStore<Preferences>
    by preferencesDataStore(name = "onboarding")

private val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val onboardingCompletedFlow by lazy {
        onboardingDataStore.data.map { prefs ->
            prefs[ONBOARDING_COMPLETED] ?: false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        enableEdgeToEdge()
        setContent {
            OwlenTheme {
                val onboardingCompleted by onboardingCompletedFlow.collectAsState(initial = false)
                OwlenNavGraph(
                    onboardingCompleted = onboardingCompleted,
                    onOnboardingComplete = {
                        lifecycleScope.launch {
                            onboardingDataStore.edit { prefs ->
                                prefs[ONBOARDING_COMPLETED] = true
                            }
                        }
                    }
                )
            }
        }
    }
}
