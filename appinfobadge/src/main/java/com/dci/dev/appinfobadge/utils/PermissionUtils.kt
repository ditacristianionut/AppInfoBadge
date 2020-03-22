package com.dci.dev.appinfobadge.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.PermissionInfo
import java.util.*

internal object PermissionUtils {

    private const val permissionPrefix = "android.permission."
    private val dangerousPermissions = arrayListOf<String>(
        "android.permission.READ_CALENDAR",
        "android.permission.WRITE_CALENDAR",
        "android.permission.CAMERA",
        "android.permission.READ_CONTACTS",
        "android.permission.WRITE_CONTACTS",
        "android.permission.GET_ACCOUNTS",
        "android.permission.ACCESS_FINE_LOCATION",
        "android.permission.ACCESS_COARSE_LOCATION",
        "android.permission.RECORD_AUDIO",
        "android.permission.READ_PHONE_STATE",
        "android.permission.READ_PHONE_NUMBERS",
        "android.permission.CALL_PHONE",
        "android.permission.ANSWER_PHONE_CALLS",
        "android.permission.READ_CALL_LOG",
        "android.permission.WRITE_CALL_LOG",
        "android.permission.ADD_VOICEMAIL",
        "android.permission.USE_SIP",
        "android.permission.PROCESS_OUTGOING_CALLS",
        "android.permission.BODY_SENSORS",
        "android.permission.SEND_SMS",
        "android.permission.RECEIVE_SMS",
        "android.permission.READ_SMS",
        "android.permission.RECEIVE_WAP_PUSH",
        "android.permission.RECEIVE_MMS",
        "android.permission.READ_EXTERNAL_STORAGE",
        "android.permission.WRITE_EXTERNAL_STORAGE"
    )

    @SuppressLint("DefaultLocale")
    fun retrievePermissions(context: Context): List<String> {
        val permissions = arrayListOf<String>()
        try {
            permissions.addAll(
                context.packageManager
                    ?.getPackageInfo(context.packageName, PackageManager.GET_PERMISSIONS)
                    ?.requestedPermissions
                    ?.filter {
                        it.toLowerCase().startsWith(permissionPrefix)
                    } ?: emptyList()
            )
        } catch (e: PackageManager.NameNotFoundException) {
            throw RuntimeException("This should have never happened.", e)
        }
        return  permissions
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
            ?.replace(permissionPrefix.toRegex(), "")
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
