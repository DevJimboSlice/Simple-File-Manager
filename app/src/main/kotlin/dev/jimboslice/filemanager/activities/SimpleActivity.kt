package dev.jimboslice.filemanager.activities

import com.simplemobiletools.commons.activities.BaseSimpleActivity
import dev.jimboslice.filemanager.R

open class SimpleActivity : BaseSimpleActivity() {
    override fun getAppIconIDs() = arrayListOf(
            R.mipmap.ic_launcher_amber,
            R.mipmap.ic_launcher
    )

    override fun getAppLauncherName() = getString(R.string.app_launcher_name)
}
