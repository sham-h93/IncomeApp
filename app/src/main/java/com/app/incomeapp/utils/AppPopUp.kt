package com.app.incomeapp.utils

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.app.incomeapp.R

class AppPopUp(
    private val view: View,
    private val activity: Activity,
    private val popupWindow: PopupWindow =
        PopupWindow(
            view,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        ),
    private val bmp: Bitmap
) {

    init {
        popupWindow.inputMethodMode = PopupWindow.INPUT_METHOD_NEEDED
        val drawable = BitmapDrawable(null, bmp)
        popupWindow.setBackgroundDrawable(drawable)
        popupWindow.animationStyle = R.style.Animation
        if (Build.VERSION.SDK_INT >= 21) {
            popupWindow.elevation = 5.0f
        }
    }

    fun show() {
        popupWindow.showAtLocation(
            activity.window.decorView.findViewById(android.R.id.content),
            Gravity.CENTER,
            0,
            0
        )
    }

    fun dismiss() {
        popupWindow.dismiss()
    }

}