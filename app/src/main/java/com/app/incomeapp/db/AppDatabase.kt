package com.app.incomeapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.app.incomeapp.utils.Converters
import com.app.incomeapp.models.db.IncomeCost

@Database(entities = [IncomeCost::class], version = 2, exportSchema = false,)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getDao() : IncomeCostDao

}