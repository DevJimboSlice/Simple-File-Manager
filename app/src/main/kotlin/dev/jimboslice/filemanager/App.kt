package dev.jimboslice.filemanager

import android.app.Application
import com.github.ajalt.reprint.core.Reprint
import com.simplemobiletools.commons.extensions.checkUseEnglish

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        // TODO: learn purpose of this
        checkUseEnglish()

        // TODO: switch to androidx biometrics
        Reprint.initialize(this)
    }
}
