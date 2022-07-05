package com.app.incomeapp.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.incomeapp.R
import com.app.incomeapp.adapters.MainAdapter
import com.app.incomeapp.adapters.OnClick
import com.app.incomeapp.databinding.LayoutMainFragmentBinding
import com.app.incomeapp.utils.numberFormatter
import com.app.incomeapp.utils.pieEntry
import com.app.incomeapp.ui.viewmodels.MainFragmentViewModel
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF
import dagger.hilt.android.AndroidEntryPoint
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class MainFragment : Fragment() {

    lateinit var bindView: LayoutMainFragmentBinding
    private val viewModel: MainFragmentViewModel by viewModels()
    lateinit var chart: PieChart
    var chartEntresList = mutableListOf(pieEntry(), pieEntry(), pieEntry())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {
                activity?.onBackPressed()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindView = DataBindingUtil.inflate(
            inflater,
            R.layout.layout_main_fragment,
            container,
            false
        )
        chart = bindView.pieChart

        val adapter = MainAdapter(object : OnClick {
            override fun onItemClicked(id: Int) {
                findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToAddEditIncomeCost(
                        id
                    )
                )
            }
        }).apply {

        }

        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            .apply { bindView.rv.layoutManager = this }
        DividerItemDecoration(requireContext(), layoutManager.orientation)
            .apply { bindView.rv.addItemDecoration(this) }
        bindView.rv.adapter = adapter

        viewModel.allIncomeCosts.asLiveData().observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        viewModel.currentJalaliTime.observe(viewLifecycleOwner) { dateString ->
            bindView.calendarTv.text = dateString
        }
        viewModel.totalIncome.observe(viewLifecycleOwner) { totalIncome ->
            val formattedNum = numberFormatter(totalIncome)
            initChart()
        }
        viewModel.todayTotalIncome.observe(viewLifecycleOwner) { todayTotalIncome ->
            val formattedNum = numberFormatter(todayTotalIncome)
            bindView.sellAmountTv.text = formattedNum
            initChart()
        }
        viewModel.todayProfit.observe(viewLifecycleOwner) { todayProfit ->
            if (todayProfit < 0) return@observe
            chartEntresList[0] = pieEntry(
                todayProfit.toFloat(),
                ContextCompat.getColor(requireContext(), R.color.green_color)
            )
            val formattedNum = numberFormatter(todayProfit)
            bindView.profitAmountTv.text = formattedNum
            initChart()
        }
        viewModel.todayTotalBuy.observe(viewLifecycleOwner) { todayTotalBuy ->
            if (todayTotalBuy < 0) return@observe
            chartEntresList[1] = pieEntry(
                todayTotalBuy.toFloat(),
                ContextCompat.getColor(requireContext(), R.color.yellow_color)
            )
            val formattedNum = numberFormatter(todayTotalBuy)
            bindView.buyAmountTv.text = formattedNum
            initChart()
        }
        viewModel.todayTotalCost.observe(viewLifecycleOwner) { todayTotalCost ->
            if (todayTotalCost < 0) return@observe
            chartEntresList[2] = pieEntry(
                todayTotalCost.toFloat(),
                ContextCompat.getColor(requireContext(), R.color.red_color)
            )
            val formattedNum = numberFormatter(todayTotalCost)
            bindView.costAmountTv.text = formattedNum
            initChart()
        }

        bindView.add.setOnClickListener {
            it.findNavController().navigate(R.id.action_mainFragment_to_addEditIncomeCost)
        }


        return bindView.root

    }


    private fun initChart() {
        chart.apply {
            setUsePercentValues(true)
            description.isEnabled = false
            legend.isEnabled = false
            centerText = "ABC \n 1234567"
            setCenterTextColor(Color.RED)
            setCenterTextTypeface(ResourcesCompat.getFont(requireContext(), R.font.iran_yekan_bold))
            setExtraOffsets(0f, 0f, 0f, 0f)
        }
        val entries = ArrayList<PieEntry>()
        val chartColor = ArrayList<Int>()

        chartEntresList.forEach {
            entries.add(
                PieEntry(
                    it.entryValue
                )
            )
            chartColor.add(it.entryColor)
        }

        val dataSet = PieDataSet(entries, null).apply {
            setDrawIcons(false)
            sliceSpace = 0f
            iconsOffset = MPPointF(0f, 0f)
            selectionShift = 5f
            colors = chartColor
        }

        PieData(dataSet).apply {
            setValueFormatter(PercentFormatter())
            setValueTextSize(14f)
            setValueTextColor(Color.WHITE)
            chart.data = this
        }
        chart.highlightValues(null)
    }


    fun onBackPress(): Boolean {
        var shouldExit = false


        AlertDialog.Builder(requireContext()).setTitle("A").setOnCancelListener {
            shouldExit = true
        }
            .show()
        return shouldExit
    }

    fun shouldInterceptBackPress() = true

}

