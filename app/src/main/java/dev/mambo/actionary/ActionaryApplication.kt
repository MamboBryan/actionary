package dev.mambo.actionary

import dev.mambo.lib.data.core.DataModule
import dev.mambo.lib.ui.presentation.helpers.initKoin
import dev.mambo.lib.ui.presentation.main.MainApplication

class ActionaryApplication : MainApplication() {
    override fun onCreate() {
        super.onCreate()
        initKoin(DataModule)
    }
}