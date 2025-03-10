package dev.mambo.lib.ui.presentation.helpers

import android.app.Application
import dev.mambo.lib.ui.presentation.PresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.module.Module

fun Application.initKoin(vararg modules: Module) {
    val list = modules.toMutableList().plus(PresentationModule)
    startKoin {
        androidContext(androidContext = this@initKoin)
        loadKoinModules(list)
    }
}
