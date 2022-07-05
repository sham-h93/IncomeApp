package com.app.incomeapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.View
import androidx.renderscript.Allocation
import androidx.renderscript.Element
import androidx.renderscript.RenderScript
import androidx.renderscript.ScriptIntrinsicBlur

object BlurUtils {

    private const val BITMAP_SCALE = 0.4f
    private var BLUR_RADIUS = 10.5f

    fun takeScreenshotOfView(view: View, height: Int, width: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val bgDrawable = view.background
        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)
        return bitmap
    }

    fun blurImg(context: Context?, image: Bitmap?, blurRadius: Float): Bitmap? {
        var blurRadius = blurRadius
        var outputBitmap: Bitmap? = null
        if (image != null) {
            if (blurRadius == 0f) {
                return image
            }
            if (blurRadius < 1) {
                blurRadius = 1f
            }
            if (blurRadius > 25) {
                blurRadius = 25f
            }
            BLUR_RADIUS = blurRadius
            val width = Math.round(image.width * BITMAP_SCALE)
            val height = Math.round(image.height * BITMAP_SCALE)
            val inputBitmap = Bitmap.createScaledBitmap(image, width, height, false)
            outputBitmap = Bitmap.createBitmap(inputBitmap)
            val rs = RenderScript.create(context)
            val theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
            val tmpIn = Allocation.createFromBitmap(rs, inputBitmap)
            val tmpOut = Allocation.createFromBitmap(rs, outputBitmap)
            theIntrinsic.setRadius(BLUR_RADIUS)
            theIntrinsic.setInput(tmpIn)
            theIntrinsic.forEach(tmpOut)
            tmpOut.copyTo(outputBitmap)
        }
        return outputBitmap
    }

}