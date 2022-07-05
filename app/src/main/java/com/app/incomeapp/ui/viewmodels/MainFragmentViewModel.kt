package com.app.incomeapp.ui.viewmodels

import androidx.lifecycle.*
import com.abcdandroid.hiltinandroid.PersianDate
import com.abcdandroid.hiltinandroid.ui.PersianDateFormat
import com.app.incomeapp.utils.AmountType
import com.app.incomeapp.utils.getToday
import com.app.incomeapp.repository.DataBaseRepository
import com.app.incomeapp.utils.timeStampToDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(
    val dataBaseRepository: DataBaseRepository
) : ViewModel() {

    val allIncomeCosts = dataBaseRepository.getAllDatabase()

    private var _currentJalaliTime = MutableLiveData<String>()
    val currentJalaliTime: MutableLiveData<String>
        get() = _currentJalaliTime

    private var _totalIncome = MutableLiveData<Long>()
    val totalIncome: MutableLiveData<Long>
        get() = _totalIncome

    private var _totalCost = MutableLiveData<Long>()
    val totalCost: MutableLiveData<Long>
        get() = _totalCost

    private var _totalBuy = MutableLiveData<Long>()
    val totalBuy: MutableLiveData<Long>
        get() = _totalBuy

    private var _todayTotalIncome = MutableLiveData<Long>()
    val todayTotalIncome: MutableLiveData<Long>
        get() = _todayTotalIncome

    private var _todayTotalCost = MutableLiveData<Long>()
    val todayTotalCost: MutableLiveData<Long>
        get() = _todayTotalCost

    private var _todayTotalBuy = MutableLiveData<Long>()
    val todayTotalBuy: MutableLiveData<Long>
        get() = _todayTotalBuy

    private var _todayProfit = MediatorLiveData<Long>()
    val todayProfit: MediatorLiveData<Long>
        get() = _todayProfit



    init {
        _totalIncome.value = 0L
        _totalCost.value = 0L
        _totalBuy.value = 0L
        _todayTotalIncome.value = 0L
        _todayTotalCost.value = 0L
        _todayTotalBuy.value = 0L
        currentTime()
        getTotalIncomes()
        getTotalCost()
        getTotalBuy()
        calculateTodayProfit()
    }

    private fun currentTime() {
        val currentTime = System.currentTimeMillis()
        val persianDate = PersianDate(currentTime)
        val formattedPersianDate = PersianDateFormat("l j Fماه Y").format(persianDate)
        _currentJalaliTime.value = "امروز : " + formattedPersianDate
    }

    fun getTotalIncomes() {
        dataBaseRepository.getIncomeOrCost(AmountType.INCOME).onEach { list ->
            list.forEach { incomeCost ->
                _totalIncome.value = _totalIncome.value?.plus(incomeCost.amount)
                val getItemDay = timeStampToDate(incomeCost.time)
                if (getItemDay == getToday()) {
                    _todayTotalIncome.value = _todayTotalIncome.value?.plus(incomeCost.amount)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getTotalCost() {
        dataBaseRepository.getIncomeOrCost(AmountType.COST).onEach { list ->
            list.forEach { incomeCost ->
                _totalCost.value = _totalCost.value?.plus(incomeCost.amount)
                val getItemDay = timeStampToDate(incomeCost.time)
                if (getItemDay == getToday()) {
                    _todayTotalCost.value = _todayTotalCost.value?.plus(incomeCost.amount)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getTotalBuy() {
        dataBaseRepository.getIncomeOrCost(AmountType.BUY).onEach { list ->
            list.forEach { incomeCost ->
                _totalBuy.value = _totalBuy.value?.plus(incomeCost.amount)
                val getItemDay = timeStampToDate(incomeCost.time)
                if (getItemDay == getToday()) {
                    _todayTotalBuy.value = _todayTotalBuy.value?.plus(incomeCost.amount)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun calculateTodayProfit() {
        viewModelScope.launch {
            _todayProfit.addSource(_todayTotalIncome) {
                _todayProfit.value =
                    it.minus(_todayTotalCost.value?.plus(_todayTotalBuy.value ?: 0) ?: 0)
            }

            _todayProfit.addSource(_todayTotalCost) {
                _todayProfit.value =
                    _todayTotalIncome.value?.minus(it.plus(_todayTotalBuy.value ?: 0))
            }

            _todayProfit.addSource(_todayTotalBuy) {
                _todayProfit.value =
                    _todayTotalIncome.value?.minus(_todayTotalCost.value?.plus(it) ?: 0)
            }
        }
    }
}