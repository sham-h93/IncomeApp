package com.app.incomeapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import com.app.incomeapp.R
import com.app.incomeapp.databinding.SortSpinnerBinding
import com.app.incomeapp.databinding.TypeSpinnerDropdownBinding

class TypeSpinnerAdapter(val context: Context, var dataSource: List<String>) : BaseAdapter(){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, parent, true)
    }

    private fun initView(position: Int, parent: ViewGroup, isDropDown: Boolean = false): View {
        DataBindingUtil.inflate<SortSpinnerBinding>(
            LayoutInflater.from(context),
            R.layout.sort_spinner,
            parent,
            false
        ).apply {
            data = dataSource[position]
            isFirstItem = position == 0
            val view: View = root
            if (position == 0 && isDropDown) {
                arrow.animate().rotation(180f).start()
            } else if (!isDropDown) {
                isFirstItem = true
            }
            return view
        }
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}