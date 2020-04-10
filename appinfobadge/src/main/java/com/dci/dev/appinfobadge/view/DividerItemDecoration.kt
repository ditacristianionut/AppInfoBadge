package com.dci.dev.appinfobadge.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.dci.dev.appinfobadge.R


/**
 * DividerItemDecoration is a [RecyclerView.ItemDecoration] that can be used as a divider
 * between items of a [LinearLayoutManager]. It supports both [.HORIZONTAL] and
 * [.VERTICAL] orientations.
 *
 * <pre>
 * mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
 * mLayoutManager.getOrientation());
 * recyclerView.addItemDecoration(mDividerItemDecoration);
</pre> *
 */
class DividerItemDecoration(
    context: Context,
    orientation: Int,
    isShowInLastItem: Boolean,
    isDarkMode: Boolean
) :
    ItemDecoration() {
    private var divider: Drawable?
    /**
     * Current orientation. Either [.HORIZONTAL] or [.VERTICAL].
     */
    private var orientation = 0
    private val isShowInLastItem: Boolean

    /**
     * Sets the orientation for this divider. This should be called if
     * [RecyclerView.LayoutManager] changes orientation.
     *
     * @param orientation [.HORIZONTAL] or [.VERTICAL]
     */
    fun setOrientation(orientation: Int) {
        require(!(orientation != HORIZONTAL && orientation != VERTICAL)) { "Invalid orientation. It should be either HORIZONTAL or VERTICAL" }
        this.orientation = orientation
    }

    /**
     * Sets the [Drawable] for this divider.
     *
     * @param drawable Drawable that should be used as a divider.
     */
    fun setDrawable(drawable: Drawable) {
        requireNotNull(drawable) { "Drawable cannot be null." }
        divider = drawable
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.layoutManager == null || divider == null) {
            return
        }
        if (orientation == VERTICAL) {
            drawVertical(c, parent)
        } else {
            drawHorizontal(c, parent)
        }
    }

    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        val left: Int
        val right: Int
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && parent.clipToPadding) {
            left = parent.paddingLeft
            right = parent.width - parent.paddingRight
            canvas.clipRect(
                left, parent.paddingTop, right,
                parent.height - parent.paddingBottom
            )
        } else {
            left = 0
            right = parent.width
        }
        val childCount: Int
        childCount = if (isShowInLastItem) {
            parent.childCount
        } else {
            parent.childCount - 1
        }
        for (i in 0 until childCount) {
            val child: View = parent.getChildAt(i)
            //                parent.getDecoratedBoundsWithMargins(child, mBounds);
//                final int bottom = mBounds.bottom + Math.round(child.getTranslationY());
            val decoratedBottom = parent.layoutManager!!.getDecoratedBottom(child)
            val bottom = (decoratedBottom + Math.round(child.getTranslationY())).toInt()
            val top = bottom - divider!!.intrinsicHeight
            divider!!.setBounds(left, top, right, bottom)
            divider!!.draw(canvas)
        }
        canvas.restore()
    }

    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        val top: Int
        val bottom: Int
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && parent.clipToPadding) {
            top = parent.paddingTop
            bottom = parent.height - parent.paddingBottom
            canvas.clipRect(
                parent.paddingLeft, top,
                parent.width - parent.paddingRight, bottom
            )
        } else {
            top = 0
            bottom = parent.height
        }
        val childCount: Int
        childCount = if (isShowInLastItem) {
            parent.childCount
        } else {
            parent.childCount - 1
        }
        for (i in 0 until childCount) {
            val child: View = parent.getChildAt(i)
            //                parent.getLayoutManager().getDecoratedBoundsWithMargins(child, mBounds);
//                final int right = mBounds.right + Math.round(child.getTranslationX());
            val decoratedRight = parent.layoutManager!!.getDecoratedRight(child)
            val right = (decoratedRight + Math.round(child.getTranslationX())).toInt()
            val left = right - divider!!.intrinsicWidth
            divider!!.setBounds(left, top, right, bottom)
            divider!!.draw(canvas)
        }
        canvas.restore()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (divider == null) {
            outRect.setEmpty()
            return
        }
        val itemPosition =
            (view.getLayoutParams() as RecyclerView.LayoutParams).viewLayoutPosition
        val itemCount = state.itemCount
        if (isShowInLastItem) {
            if (orientation == VERTICAL) {
                outRect.set(0, 0, 0, divider!!.intrinsicHeight)
            } else {
                outRect.set(0, 0, divider!!.intrinsicWidth, 0)
            }
        } else if (itemPosition == itemCount - 1) { // We didn't set the last item when mIsShowInLastItem's value is false.
            outRect.setEmpty()
        } else {
            if (orientation == VERTICAL) {
                outRect.set(0, 0, 0, divider!!.intrinsicHeight)
            } else {
                outRect.set(0, 0, divider!!.intrinsicWidth, 0)
            }
        }
    }

    companion object {
        const val HORIZONTAL = LinearLayout.HORIZONTAL
        const val VERTICAL = LinearLayout.VERTICAL
        private const val TAG = "DividerItem"
    }

    /**
     * Creates a divider [RecyclerView.ItemDecoration] that can be used with a
     * [LinearLayoutManager].
     *
     * @param context          Current context, it will be used to access resources.
     * @param orientation      Divider orientation. Should be [.HORIZONTAL] or [.VERTICAL].
     * @param isShowInLastItem Whether show the divider in last item.
     */
    init {
        divider = if (isDarkMode)
            ContextCompat.getDrawable(context, R.drawable.item_separator_dark)
        else
            ContextCompat.getDrawable(context, R.drawable.item_separator)
        setOrientation(orientation)
        this.isShowInLastItem = isShowInLastItem
    }
}
