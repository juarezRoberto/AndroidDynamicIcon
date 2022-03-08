package com.juarez.myapplication

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast

enum class AppIcons {
    MainActivity,
    FirstIconAlias,
    SecondIconAlias
}

object AppIconUtils {
    fun setNewIcon(context: Context, iconName: String) {

        AppIcons.values().map {

            Log.d("iconname", it.toString())
            context.packageManager.setComponentEnabledSetting(
                ComponentName(context, Constants.packagePath + it.toString()),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )

        }

        context.packageManager.setComponentEnabledSetting(
            ComponentName(context, Constants.packagePath + iconName),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )

        Toast.makeText(context, "Enable Icon: $iconName", Toast.LENGTH_LONG).show()
    }

    fun reset(context: Context) {
        AppIcons.values().map {
            context.packageManager.setComponentEnabledSetting(
                ComponentName(context, Constants.packagePath + it.toString()),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )
        }

        context.packageManager.setComponentEnabledSetting(
            ComponentName(context, MainActivity::class.java),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
        Toast.makeText(context, "Reset Icon", Toast.LENGTH_LONG).show()
    }
}