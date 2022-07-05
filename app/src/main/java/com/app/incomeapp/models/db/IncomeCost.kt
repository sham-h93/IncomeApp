package com.app.incomeapp.models.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.app.incomeapp.utils.AmountType
import com.app.incomeapp.utils.Converters

@Entity(tableName = "income_cost_table")
@TypeConverters(Converters::class)
data class IncomeCost(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String,
    var description: String,
    var time: Long = System.currentTimeMillis(),
    val modifyTime: Long = System.currentTimeMillis(),
    var amount: Long,
    var amountType: AmountType
)
