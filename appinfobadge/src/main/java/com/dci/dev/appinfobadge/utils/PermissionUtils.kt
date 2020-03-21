package com.dci.dev.appinfobadge.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.PermissionInfo
import java.util.*

internal object PermissionUtils {

    fun retrievePermissions(context: Context): Array<String> {
        return try {
            context
                .packageManager
                .getPackageInfo(context.packageName, PackageManager.GET_PERMISSIONS)
                .requestedPermissions
        } catch (e: PackageManager.NameNotFoundException) {
            throw RuntimeException("This should have never happened.", e)
        }
    }

    private fun getPermissionDetails(permission: String, context: Context): PermissionInfo? {
        return context.packageManager.getPermissionInfo(
            permission,
            PackageManager.GET_META_DATA
        )
    }

    @SuppressLint("DefaultLocale")
    fun getPermissionName(permission: String, context: Context): String? {
        return getPermissionDetails(
            permission,
            context
        )?.name
            ?.replace("android.permission.".toRegex(), "")
            ?.replace("_".toRegex(), " ")
            ?.toLowerCase(Locale.ROOT)
            ?.capitalize()
    }

    fun getPermissionDescription(permission: String, context: Context): Int? {
        return getPermissionDetails(
            permission,
            context
        )?.descriptionRes
    }

    fun hasPermission(
        permission: String,
        context: Context
    ): Boolean {
        return PackageManager.PERMISSION_GRANTED == context.packageManager.checkPermission(
            permission,
            context.packageName
        )
    }


}
