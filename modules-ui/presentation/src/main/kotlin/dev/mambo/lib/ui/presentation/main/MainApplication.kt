package dev.mambo.lib.ui.presentation.main

import android.app.Application
import dev.mambo.lib.ui.presentation.helpers.initTimber
import org.koin.core.component.KoinComponent

abstract class MainApplication : Application(), KoinComponent {
    override fun onCreate() {
        super.onCreate()
        initTimber()
    }
}
