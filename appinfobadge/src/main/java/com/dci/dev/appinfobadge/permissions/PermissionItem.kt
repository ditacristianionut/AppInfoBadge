package com.dci.dev.appinfobadge.permissions

import androidx.annotation.StringRes

internal data class PermissionItem(
    val name: String,
    @StringRes val description: Int,
    val isGranted: Boolean = false
)
