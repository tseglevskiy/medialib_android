package ru.roscha_akademii.medialib.common.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View.MeasureSpec.*
import android.widget.ScrollView

class SwitchedScrollView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
: ScrollView(context, attrs, defStyleAttr) {

    var scrollable = true
        set(value) {
            field = value
            requestLayout()
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val child = getChildAt(0)
        if (!scrollable && child != null &&
                getMode(widthMeasureSpec) != UNSPECIFIED
                && getMode(heightMeasureSpec) != UNSPECIFIED) {

            child.measure(
                    makeMeasureSpec(getSize(widthMeasureSpec), EXACTLY),
                    makeMeasureSpec(getSize(heightMeasureSpec), EXACTLY)
            )
        }
    }

    override fun canScrollVertically(direction: Int): Boolean {
        if (!scrollable) {
            return false
        }
        return super.canScrollVertically(direction)
    }
}


