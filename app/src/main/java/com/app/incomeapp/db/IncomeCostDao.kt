package com.app.incomeapp.db

import androidx.room.*
import com.app.incomeapp.utils.AmountType
import com.app.incomeapp.utils.Converters
import com.app.incomeapp.models.db.IncomeCost
import kotlinx.coroutines.flow.Flow

@Dao
@TypeConverters(Converters::class)
interface IncomeCostDao {

    @Insert(onConflict =OnConflictStrategy.REPLACE )
    suspend fun addIncomeCost(incomeCost: IncomeCost)

    @Query("DELETE FROM income_cost_table WHERE id = :id")
    suspend fun deleteIncomeCost(id: Int)

    @Query("SELECT * FROM income_cost_table WHERE id = :id")
    suspend fun getIncomeCost(id:Int): IncomeCost

    @Query("SELECT * FROM income_cost_table ORDER BY id DESC")
    fun getAllIncomeCost(): Flow<List<IncomeCost>>

    @Query("SELECT * FROM income_cost_table WHERE amountType = :isIncome")
    fun getAllIncomesOrCosts(isIncome: AmountType): Flow<List<IncomeCost>>

    @Query("SELECT * FROM income_cost_table WHERE amountType = :isIncome & time = :time")
    fun getTodayIncomesOrCosts(isIncome: AmountType, time: Long): Flow<List<IncomeCost>>


    @Query("SELECT * FROM income_cost_table WHERE time>:startFrom AND time<:endTo ")
    fun getRecordsFromTo(startFrom:Long,endTo:Long): Flow<List<IncomeCost>>

}