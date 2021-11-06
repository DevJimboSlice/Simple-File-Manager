package dev.jimboslice.filemanager.extensions

import android.content.Context
import com.simplemobiletools.commons.extensions.isPathOnOTG
import com.simplemobiletools.commons.extensions.isPathOnSD
import dev.jimboslice.filemanager.helpers.Config

val Context.config: Config get() = Config.newInstance(applicationContext)

fun Context.isPathOnRoot(path: String) = !(path.startsWith(config.internalStoragePath) || isPathOnOTG(path) || (isPathOnSD(path)))
