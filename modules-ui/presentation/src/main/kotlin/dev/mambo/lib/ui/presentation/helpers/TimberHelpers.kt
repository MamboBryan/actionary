package dev.mambo.lib.ui.presentation.helpers

import android.app.Application
import timber.log.Timber

fun Application.initTimber(){
    Timber.plant(Timber.DebugTree())
}