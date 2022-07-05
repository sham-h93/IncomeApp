package com.app.incomeapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.abcdandroid.hiltinandroid.PersianDate
import com.abcdandroid.hiltinandroid.ui.PersianDateFormat
import com.app.incomeapp.utils.AmountType
import com.app.incomeapp.R
import com.app.incomeapp.databinding.MainFragmentRvItemBinding
import com.app.incomeapp.utils.formatTime
import com.app.incomeapp.models.db.IncomeCost
import javax.inject.Inject

class MainAdapter @Inject constructor(
    val clickListener: OnClick
) : ListAdapter<IncomeCost, MainAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(
        val binding: MainFragmentRvItemBinding,
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: MainFragmentRvItemBinding = MainFragmentRvItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.titleTv.text = getItem(position).title
        var amountTextColor = 0
        when (getItem(position).amountType) {
            AmountType.INCOME -> {
                amountTextColor = ContextCompat.getColor(holder.binding.root.context, R.color.green_color)
            }
            AmountType.COST -> {
                amountTextColor = ContextCompat.getColor(holder.binding.root.context, R.color.red_color)
            }
            AmountType.BUY -> {
                amountTextColor = ContextCompat.getColor(holder.binding.root.context, R.color.yellow_color)
            }
        }
        holder.binding.amountTv.apply {
            text = getItem(position).amount.toString()
            setTextColor(amountTextColor)
        }
        val dt = PersianDate(getItem(position).time)
        val dateTime = formatTime(getItem(position).time)
        holder.binding.dateTv.text = dt.toString()
        holder.binding.root.setOnClickListener {
            clickListener.onItemClicked(getItem(position).id)
        }
        holder.binding.executePendingBindings()
    }

}

object diffUtil : DiffUtil.ItemCallback<IncomeCost>() {
    override fun areItemsTheSame(oldItem: IncomeCost, newItem: IncomeCost): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: IncomeCost, newItem: IncomeCost): Boolean {
        return oldItem == newItem
    }

}

interface OnClick {
    fun onItemClicked(id: Int)
}