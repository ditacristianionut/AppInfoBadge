package com.dci.dev.appinfobadge.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.ImageView
import com.dci.dev.appinfobadge.utils.px

/**
 * The RoundedCornerImageView class provides a way to round individual corners for an imageView.
 * The two main parameters are radius and roundedCorners
 * @see radius and
 * @see roundedCorners
 */
@SuppressLint("AppCompatCustomView")
class RoundedCornerImageView : ImageView {

    /**
     * Specify the radius of the corners that will be rounded in pixels
     */
    var radius: Radius =
        Radius(20.px)
        set(value) {
            field = value
            recalculateClipPath()
        }

    /**
     * Specify the corners to be rounded(bit-wise). eg. CORNER_BOTTOM_LEFT or CORNER_TOP_LEFT will round both those corners
     * CORNER_ALL xor CORNER_BOTTOM_LEFT will round everything except bottom left corner
     */
    var roundedCorners: Int =
        CORNER_NONE
        set(value) {
            field = value
            recalculateClipPath()
        }

    /**
     * Hold all corners for recalculating
     */
    private val cornersAll = intArrayOf(
        CORNER_TOP_LEFT,
        CORNER_TOP_RIGHT,
        CORNER_BOTTOM_RIGHT,
        CORNER_BOTTOM_LEFT
    )

    /**
     * The Clipping path that will be applied to the view
     */
    private val clipPath = Path()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // recalculate clipping path when the size of the view has been changed
        recalculateClipPath()
    }

    /**
     * Recalculates the clipping path of the view based on selected rounded corners and the actual radius
     * If no corners have been selected or the radius is 0 no clipping will occur
     */
    private fun recalculateClipPath() {
        clipPath.rewind()

        if (roundedCorners != CORNER_NONE && radius.sum() > 0) {
            val radii = FloatArray(8)

            for (i in 0..3) {
                if (isCornerRounded(cornersAll[i])) {
                    radii[2 * i] = radius[i].toFloat()
                    radii[2 * i + 1] = radius[i].toFloat()
                }
            }
            clipPath.addRoundRect(
                    RectF(0f, 0f, width.toFloat(), height.toFloat()),
                    radii, Path.Direction.CW
            )
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (!clipPath.isEmpty) {
            canvas.clipPath(clipPath)
        }
        super.onDraw(canvas)
    }

    /**
     * Utility function to check corner selection
     */
    private fun isCornerRounded(corner: Int): Boolean {
        return roundedCorners and corner == corner
    }

    /**
     * Helper class for specifying radius for each individual corner
     */
    data class Radius(
        val topLeft: Int = 0,
        val topRight: Int = 0,
        val bottomRight: Int = 0,
        val bottomLeft: Int = 0
    ) {
        /**
         * Initialize all corners with same radius
         */
        constructor(radius: Int = 0) : this(
                topLeft = radius,
                topRight = radius,
                bottomRight = radius,
                bottomLeft = radius
        )

        override fun toString(): String = "Radius - tl:$topLeft, tr:$topRight, br:$bottomRight, $bottomLeft"
        fun sum(): Int = topLeft + topRight + bottomRight + bottomLeft
    }

    /**
     * Index operator to make drawing easier
     */
    operator fun Radius.get(index: Int): Int =
            when (index) {
                0 -> topLeft
                1 -> topRight
                2 -> bottomRight
                3 -> bottomLeft
                else -> 0
            }

    companion object {
        const val CORNER_NONE = 0
        const val CORNER_TOP_LEFT = 1
        const val CORNER_TOP_RIGHT = 2
        const val CORNER_BOTTOM_RIGHT = 4
        const val CORNER_BOTTOM_LEFT = 8
        const val CORNER_ALL = 15
    }
}
