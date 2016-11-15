package ru.roscha_akademii.medialib.common.widget

import android.widget.FrameLayout
import android.widget.LinearLayout

fun FrameLayout.heightMatchParent() {
    var params = this.layoutParams
    params.height =  FrameLayout.LayoutParams.MATCH_PARENT
    this.layoutParams = params

    this.parent?.requestLayout()
}

fun FrameLayout.heightWrapContent() {
    var params = this.layoutParams
    params.height =  LinearLayout.LayoutParams.WRAP_CONTENT
    this.layoutParams = params

    this.parent?.requestLayout()
}