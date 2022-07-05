package com.app.incomeapp.utils

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.View
import androidx.room.TypeConverter
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
    return SimpleDateFormat("MMM-dd-yyyy' Time: 'HH:mm")
        .format(timeMillis).toString()
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