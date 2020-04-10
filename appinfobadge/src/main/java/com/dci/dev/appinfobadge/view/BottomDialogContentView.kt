package com.dci.dev.appinfobadge.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.LayoutRes
import com.dci.dev.appinfobadge.InfoItemWithView
import com.dci.dev.appinfobadge.R
import com.dci.dev.appinfobadge.utils.inflate
import com.dci.dev.appinfobadge.utils.px
import com.dci.dev.appinfobadge.utils.resolveColor

class BottomDialogContentView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : RelativeLayout(context, attrs, defStyle, defStyleRes) {

    private var headerBackground: RoundedCornerImageView
    private var headerIcon: ImageView
    private var contentLayout: LinearLayout
    private var customView: View? = null

    fun setData(data: InfoItemWithView) {
        headerIcon.setImageResource(data.iconId)
        headerBackground.setImageResource(data.headerColor)
        headerBackground.radius = RoundedCornerImageView.Radius(20.px, 20.px, 0.px, 0.px)
        headerBackground.roundedCorners = RoundedCornerImageView.CORNER_ALL
        addCustomView(res = data.res, view = data.view)
        invalidate()
    }

    var isDarkModeOn: Boolean = false
        set(value) {
            field = value
            if (field)
                contentLayout.setBackgroundColor(context.resolveColor(R.color.dark_background))
            else
                contentLayout.setBackgroundColor(context.resolveColor(R.color.light_background))
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.bottom_dialog_layout, this, true)
        headerBackground = findViewById(R.id.ivBannerBackground)
        headerIcon = findViewById(R.id.ivBanner)
        contentLayout = findViewById(R.id.content)
    }

    private fun addCustomView(
        @LayoutRes res: Int?,
        view: View?
    ): View {
        check(customView == null) { "Custom view already set." }

        if (view != null && view.parent != null) {
            // Make sure the view is detached from any former parents.
            val parent = view.parent as? ViewGroup
            parent?.let { parent.removeView(view) }
        }

        customView = view ?: inflate(res!!)
        contentLayout.addView(customView)
        return customView!!
    }
}
