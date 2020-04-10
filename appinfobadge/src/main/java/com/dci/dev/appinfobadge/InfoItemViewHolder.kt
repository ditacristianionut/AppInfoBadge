package com.dci.dev.appinfobadge

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.afollestad.recyclical.ViewHolder

class InfoItemViewHolder(itemView: View) : ViewHolder(itemView) {
    val title: TextView = itemView.findViewById(R.id.tvName)
    val icon: ImageView = itemView.findViewById(R.id.ivIcon)
}
