package com.app.incomeapp.ui.viewmodels

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.incomeapp.models.db.IncomeCost
import com.app.incomeapp.repository.DataBaseRepository
import com.app.incomeapp.utils.AmountType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditIncomeCostViewModel @Inject constructor(
    var dataBaseRepository: DataBaseRepository
) : ViewModel() {

    var forEditItemId: Int? = null

    private var _mTitle = MutableLiveData<String>()
    val mTitle: MutableLiveData<String>
        get() = _mTitle

    private var _mAmount = MutableLiveData<Long>()
    val mAmount: MutableLiveData<Long>
        get() = _mAmount

    private var _mDescription = MutableLiveData<String>()
    val mDescription: MutableLiveData<String>
        get() = _mDescription

    private var _isIncome = MutableLiveData<AmountType>()
    val isIncome: MutableLiveData<AmountType>
        get() = _isIncome

    private var _toastMessage = MutableLiveData<String>()
    val toastMessage: MutableLiveData<String>
        get() = _toastMessage

    private var _popFragment = MutableLiveData<Boolean>()
    val popFragment: MutableLiveData<Boolean>
        get() = _popFragment

    fun getAmountTitle(text: CharSequence) {
        mTitle.value = text.toString()
    }

    fun getAmount(amount: String?) {
        if (amount?.length!! > 0) {
            mAmount.value = amount.toLong()
        }
    }

    fun getDescription(text: String) {
        mDescription.value = text
    }

    fun isIncomeClick() {
        _isIncome.value = AmountType.INCOME
    }

    fun isBuyClick() {
        _isIncome.value = AmountType.BUY
    }

    fun isCostClick(v: View) {
        _isIncome.value = AmountType.COST
    }

    fun addIncomeCostClick() {
        viewModelScope.launch {
            if (checkInputValidity()) {
                val incomeCost: IncomeCost =
                    if (forEditItemId == 0) {
                        IncomeCost(
                            title = mTitle.value!!,
                            amount = mAmount.value!!,
                            description = mDescription.value!!,
                            amountType = isIncome.value!!
                        )
                    } else dataBaseRepository.getIncomeCost(forEditItemId!!).copy(
                        title = mTitle.value!!,
                        amount = mAmount.value!!,
                        description = mDescription.value!!,
                        amountType = isIncome.value!!
                    )
                dataBaseRepository.addToDatabase(incomeCost)
                _popFragment.value = true
            }
        }
    }

    fun getIncomeCost(id: Int?) {
        forEditItemId = id
        if (id == null) return
        viewModelScope.launch {
            val incomeCost = dataBaseRepository.getIncomeCost(id)
            mTitle.value = incomeCost.title
            mAmount.value = incomeCost.amount
            mDescription.value = incomeCost.description
            isIncome.value = incomeCost.amountType
        }
    }

    fun deleteIncomeCost(id: Int) {
        if (id > 0) {
            viewModelScope.launch {
                dataBaseRepository.deleteIncomeCost(id)
                _toastMessage.value = "با موفقیت حذف شد"
            }
        }
    }


    fun checkInputValidity(): Boolean {
        if (mTitle.value != null && mAmount.value != null && mDescription.value != null) {
            return true
        }
        _toastMessage.value = "لطفا همه ورودی ها را تکمیل کنید"
        return false
    }

    fun setPopFragmentFalse() {
        _popFragment.value = false
    }

}