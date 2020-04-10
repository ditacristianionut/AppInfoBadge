package com.dci.dev.appinfobadge

import android.net.Uri
import android.view.View
import androidx.annotation.LayoutRes
import kotlinx.serialization.ContextualSerialization

@kotlinx.serialization.Serializable
abstract class BaseInfoItem

@kotlinx.serialization.Serializable
data class InfoItemWithView(
    val iconId: Int = -1,
    val title: String = "",
    val headerColor: Int = R.color.white,
    @LayoutRes val res: Int?,
    @ContextualSerialization val view: View?
) : BaseInfoItem()

@kotlinx.serialization.Serializable
data class InfoItemWithLink(
    val iconId: Int = -1,
    val title: String = "",
    val headerColor: Int = R.color.white,
    @ContextualSerialization val link: Uri
) : BaseInfoItem()
