package com.app.incomeapp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.room.TypeConverter
import com.app.incomeapp.R
import com.app.incomeapp.databinding.LayoutToastBinding
import com.google.android.material.textview.MaterialTextView
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

enum class AmountType {
    INCOME,
    BUY,
    COST
}

class Converters {

    @TypeConverter
    fun toHealth(value: String) = enumValueOf<AmountType>(value)

    @TypeConverter
    fun fromHealth(value: AmountType) = value.name
}

@SuppressLint("SimpleDateFormat")
fun formatTime(timeMillis: Long): String {
    return SimpleDateFormat("MMM-dd' Time: 'HH:mm")
        .format(timeMillis * 1000).toString()
}

@SuppressLint("SimpleDateFormat")
fun timeStampToDate(timeMillis: Long): String {
    return SimpleDateFormat("MMM-dd-yyyy")
        .format(timeMillis).toString()
}

@SuppressLint("SimpleDateFormat")
fun getToday(): String? {
    val current = Date().time
    return SimpleDateFormat("MMM-dd-yyyy").format(current)
}

fun numberFormatter(number: Any): String? = DecimalFormat("#,###").format(number)

data class pieEntry(
    val entryValue: Float = 0f,
    val entryColor: Int = 0
)

fun Context.showCustomToast(message: String) {
    Toast(this@showCustomToast).apply {
        setGravity(Gravity.BOTTOM, 0, 100)
        duration = Toast.LENGTH_LONG
        view = DataBindingUtil.inflate<LayoutToastBinding>(
            LayoutInflater.from(this@showCustomToast),
            R.layout.layout_toast,
            null,
            false
        ).root.also {
            it.findViewById<MaterialTextView>(R.id.toast_txt).text = message
        }
    }.show()


}