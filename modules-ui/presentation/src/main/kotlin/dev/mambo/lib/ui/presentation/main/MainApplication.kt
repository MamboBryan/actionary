package dev.mambo.lib.ui.presentation.main

import android.app.Application
import dev.mambo.lib.ui.presentation.helpers.initTimber

abstract class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initTimber()
    }
}
