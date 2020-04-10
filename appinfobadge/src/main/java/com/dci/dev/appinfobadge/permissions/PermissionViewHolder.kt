package com.dci.dev.appinfobadge.permissions

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.afollestad.recyclical.ViewHolder
import com.dci.dev.appinfobadge.R

internal class PermissionViewHolder (itemView: View) : ViewHolder(itemView) {
    val name: TextView = itemView.findViewById(R.id.tvPermissionName)
    val description: TextView = itemView.findViewById(R.id.tvPermissionDetails)
    val settings: TextView = itemView.findViewById(R.id.tvGoToSettings)
    val settingsIv: ImageView = itemView.findViewById(R.id.ivGoToSettings)
}
