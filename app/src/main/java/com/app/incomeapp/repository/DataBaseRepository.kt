package com.app.incomeapp.repository

import com.abcdandroid.hiltinandroid.PersianDate
import com.app.incomeapp.db.AppDatabase
import com.app.incomeapp.models.db.IncomeCost
import com.app.incomeapp.utils.AmountType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject

class DataBaseRepository @Inject constructor(
    private var appDatabase: AppDatabase,
) {

    suspend fun addToDatabase(incomeCost: IncomeCost) = appDatabase.getDao().addIncomeCost(incomeCost)

    suspend fun deleteIncomeCost(id: Int) = appDatabase.getDao().deleteIncomeCost(id)

    suspend fun getIncomeCost(id: Int) = appDatabase.getDao().getIncomeCost(id)

    fun getAllDatabase(): Flow<List<IncomeCost>> = appDatabase.getDao().getAllIncomeCost()

    fun getIncomeOrCost(isIncome: AmountType): Flow<List<IncomeCost>> =
        appDatabase.getDao().getAllIncomesOrCosts(isIncome)


    fun getRecordsFromTo(from:Long,to:Long): Flow<Map<Int, IncomeCostSummery>> {
        return appDatabase.getDao().getRecordsFromTo(from,to)
            .map { rawList ->
                rawList.groupBy {
                    Calendar.getInstance().apply { timeInMillis = it.time }[Calendar.DAY_OF_YEAR]
                }.mapValues { calculateDayProfit(it.value) }
            }
    }


    fun getLastWeekRecords(): Flow<Map<Int, IncomeCostSummery>> {
        val dataCount = 7
        val today = Calendar.getInstance()
        val lastWeek = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_YEAR, today[Calendar.DAY_OF_YEAR] - dataCount + 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
        return getRecordsFromTo(lastWeek.timeInMillis, today.timeInMillis)
    }


    fun getLastMonthRecords(): Flow<Map<Int, IncomeCostSummery>> {

        val dataCount = PersianDate().monthDays

        val today = Calendar.getInstance()
        val lastMonth = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_YEAR, today[Calendar.DAY_OF_YEAR] - dataCount + 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        return getRecordsFromTo(lastMonth.timeInMillis, today.timeInMillis)
    }




    private fun calculateDayProfit(dayTransactions: List<IncomeCost>): IncomeCostSummery {
        val income: Long = dayTransactions.filter { it.amountType == AmountType.INCOME }
            .foldRight(0L) { total, item -> total.amount + item }
        val buy: Long = dayTransactions.filter { it.amountType == AmountType.BUY }
            .foldRight(0L) { total, item -> total.amount + item }
        val cost: Long = dayTransactions.filter { it.amountType == AmountType.COST }
            .foldRight(0L) { total, item -> total.amount + item }
        return IncomeCostSummery(time = PersianDate(dayTransactions[0].time),income, buy, cost)
    }



    data class IncomeCostSummery(val time:PersianDate,val income: Long, val buy: Long, val cost: Long){
        val profit = income - buy - cost
    }

}