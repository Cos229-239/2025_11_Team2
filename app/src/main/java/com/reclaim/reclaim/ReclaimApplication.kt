package com.reclaim.reclaim

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * ReclaimApplication
 * ------------------
 * Entry point for Hilt dependency injection.
 * - Annotated with @HiltAndroidApp to trigger Hilt code generation.
 * - Provides global DI graph for the app.
 */

@HiltAndroidApp
class ReclaimApplication : Application()
