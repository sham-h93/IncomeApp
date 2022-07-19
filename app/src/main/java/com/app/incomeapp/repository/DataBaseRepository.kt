package com.app.incomeapp.repository

import com.app.incomeapp.utils.AmountType
import com.app.incomeapp.db.AppDatabase
import com.app.incomeapp.models.db.IncomeCost
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DataBaseRepository @Inject constructor(
    private var appDatabase: AppDatabase,
) {

    suspend fun addToDatabase(incomeCost: IncomeCost) = appDatabase.getDao().addIncomeCost(incomeCost)

    suspend fun deleteIncomeCost(id: Int) = appDatabase.getDao().deleteIncomeCost(id)

    suspend fun getIncomeCost(id: Int) = appDatabase.getDao().getIncomeCost(id)

    fun getAllDatabase(): Flow<List<IncomeCost>> = appDatabase.getDao().getAllIncomeCost()

    fun getIncomeOrCost(isIncome: AmountType) = appDatabase.getDao().getAllIncomesOrCosts(isIncome)

}