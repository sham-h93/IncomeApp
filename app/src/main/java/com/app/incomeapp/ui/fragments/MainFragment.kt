package com.app.incomeapp.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.incomeapp.R
import com.app.incomeapp.adapters.MainAdapter
import com.app.incomeapp.adapters.OnClick
import com.app.incomeapp.databinding.LayoutMainFragmentBinding
import com.app.incomeapp.ui.viewmodels.MainFragmentViewModel
import com.app.incomeapp.utils.LineEntryValCol
import com.app.incomeapp.utils.PieEntryValCol
import com.app.incomeapp.utils.numberFormatter
import com.app.incomeapp.utils.showCustomToast
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.MPPointF
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainFragment : Fragment() {

    lateinit var bindView: LayoutMainFragmentBinding
    private val viewModel: MainFragmentViewModel by viewModels()
    lateinit var pieChart: PieChart
    lateinit var lineChart: LineChart
    private var finishActivity = false
    var pieChartEntresList = mutableListOf(
        PieEntryValCol(),
        PieEntryValCol(),
        PieEntryValCol()
    )
    var incomeLineChartEntresList = mutableListOf<LineEntryValCol>()
    var costLineChartEntresList = mutableListOf<LineEntryValCol>()
    var buyLineChartEntresList = mutableListOf<LineEntryValCol>()
    var profitLineChartEntresList = mutableListOf<LineEntryValCol>()

    val dataSets = mutableListOf<ILineDataSet>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback {
            this@MainFragment.requireContext().showCustomToast("برای خروج مجددا ضربه بزنید")
            CoroutineScope(Dispatchers.Main).launch {
                finishActivity = true
                delay(1500)
                finishActivity = false
            }
            if (finishActivity) {
                requireActivity().finishAffinity()
            }
        }
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
        pieChart = bindView.pieChart
        lineChart = bindView.lineChart

        val adapter = MainAdapter(object : OnClick {
            override fun onItemClicked(id: Int) {
                findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToAddEditIncomeCost(
                        id
                    )
                )
            }
        })


        incomeLineChartEntresList.add(
            LineEntryValCol(
                Entry(0f, 0f)
            )
        )
        incomeLineChartEntresList.add(
            LineEntryValCol(
                Entry(10f, 10f)
            )
        )
        incomeLineChartEntresList.add(
            LineEntryValCol(
                Entry(5f, 7f)
            )
        )

        incomeLineChartEntresList.add(
            LineEntryValCol(
                Entry(15f, 20f)
            )
        )

        costLineChartEntresList.add(
            LineEntryValCol(
                Entry(20f, 20f)
            )
        )
        costLineChartEntresList.add(
            LineEntryValCol(
                Entry(30f, 30f)
            )
        )

        buyLineChartEntresList.add(
            LineEntryValCol(
                Entry(40f, 50f)
            )
        )
        buyLineChartEntresList.add(
            LineEntryValCol(
                Entry(50f, 60f)
            )
        )

        profitLineChartEntresList.add(
            LineEntryValCol(
                Entry(10f, 30f)
            )
        )
        profitLineChartEntresList.add(
            LineEntryValCol(
                Entry(70f, 80f)
            )
        )
        profitLineChartEntresList.add(
            LineEntryValCol(
                Entry(90f, 100f)
            )
        )
        profitLineChartEntresList.add(
            LineEntryValCol(
                Entry(100f, 50f)
            )
        )

        initLineChart()

        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            .apply { bindView.rv.layoutManager = this }
        DividerItemDecoration(requireContext(), layoutManager.orientation)
            .apply { bindView.rv.addItemDecoration(this) }
        bindView.rv.adapter = adapter

        bindView.statusRg.setOnCheckedChangeListener { rg, i ->
            if (bindView.today.isChecked) {
                checkPeriodVisibility()
            } else if (bindView.lastWeek.isChecked) {
                checkPeriodVisibility()
            } else {
                bindView.periodBtn.visibility = VISIBLE
                bindView.periodBtn.animate().alpha(1.0f).start()
            }
        }

        viewModel.allIncomeCosts.asLiveData().observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        viewModel.currentJalaliTime.observe(viewLifecycleOwner) { dateString ->
            //bindView.calendarTv.text = dateString
        }
        viewModel.todayTotalIncome.observe(viewLifecycleOwner) { todayTotalIncome ->
            val formattedNum = numberFormatter(todayTotalIncome)
            bindView.sellAmountTv.text = formattedNum
            initPieChart()
        }
        viewModel.todayProfit.observe(viewLifecycleOwner) { todayProfit ->
            if (todayProfit < 0) return@observe
            pieChartEntresList[0] = PieEntryValCol(
                todayProfit.toFloat(),
                ContextCompat.getColor(requireContext(), R.color.green_color)
            )
            val formattedNum = numberFormatter(todayProfit)
            bindView.profitAmountTv.text = formattedNum
            initPieChart()
        }
        viewModel.todayTotalBuy.observe(viewLifecycleOwner) { todayTotalBuy ->
            if (todayTotalBuy < 0) return@observe
            pieChartEntresList[1] = PieEntryValCol(
                todayTotalBuy.toFloat(),
                ContextCompat.getColor(requireContext(), R.color.yellow_color)
            )
            val formattedNum = numberFormatter(todayTotalBuy)
            bindView.buyAmountTv.text = formattedNum
            initPieChart()
        }
        viewModel.todayTotalCost.observe(viewLifecycleOwner) { todayTotalCost ->
            if (todayTotalCost < 0) return@observe
            pieChartEntresList[2] = PieEntryValCol(
                todayTotalCost.toFloat(),
                ContextCompat.getColor(requireContext(), R.color.red_color)
            )
            val formattedNum = numberFormatter(todayTotalCost)
            bindView.costAmountTv.text = formattedNum
            initPieChart()
        }

        bindView.add.setOnClickListener {
            it.findNavController().navigate(R.id.action_mainFragment_to_addEditIncomeCost)
        }

        return bindView.root

    }

    private fun checkPeriodVisibility() {
        if (bindView.periodBtn.visibility == VISIBLE) {
            bindView.periodBtn.apply {
                animate().alpha(0.0f).start()
                visibility = INVISIBLE
            }

        }
    }

    private fun initPieChart() {
        pieChart.apply {
            setUsePercentValues(true)
            description.isEnabled = false
            legend.isEnabled = false
            // centerText = "ABC \n 1234567"
            // setCenterTextColor(Color.RED)
            setCenterTextTypeface(ResourcesCompat.getFont(requireContext(), R.font.iran_yekan_bold))
            setExtraOffsets(0f, 0f, 0f, 0f)
        }
        val entries = mutableListOf<PieEntry>()
        val chartColor = mutableListOf<Int>()

        pieChartEntresList.forEach {
            entries.add(
                PieEntry(it.entryValue)
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
            pieChart.data = this
        }
        pieChart.highlightValues(null)
    }

    private fun initLineChart() {

        lineChart.apply {
            setDrawGridBackground(false)
            getDescription().setEnabled(false)
            getAxisLeft().setEnabled(true)
            getAxisRight().setEnabled(false)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            getAxisRight().setDrawAxisLine(true)
            getAxisRight().setDrawGridLines(true)
            getXAxis().setDrawAxisLine(false)
            getXAxis().setDrawGridLines(false)
            setTouchEnabled(false)
            setDragEnabled(false)
            setScaleEnabled(false)
            setPinchZoom(false)
        }

        setLineData(
            incomeLineChartEntresList,
            "Income",
            ContextCompat.getColor(requireContext(), R.color.blue)
        )
        setLineData(
            costLineChartEntresList, "Cost", ContextCompat.getColor(
                requireContext(),
                R.color.red_color
            )
        )
        setLineData(
            buyLineChartEntresList,
            "Buy",
            ContextCompat.getColor(requireContext(), R.color.yellow)
        )
        setLineData(
            profitLineChartEntresList,
            "Profit",
            ContextCompat.getColor(requireContext(), R.color.green_color)
        )


        val profitData = LineData(dataSets)
        lineChart.setData(profitData)

        val legend: Legend = lineChart.legend
        legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.setDrawInside(false)

    }

    private fun setLineData(
        entriesList: List<LineEntryValCol>,
        dataSetLabel: String,
        color: Int,
    ) {
        val entryList = mutableListOf<Entry>()
        entriesList.forEach {
            entryList.add(it.entryValues)
            LineDataSet(entryList, dataSetLabel).apply {
                lineWidth = 2.5f
                circleRadius = 4f
                setDrawFilled(true)
                fillDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.gradeint)
                fillAlpha = 35
                mode = LineDataSet.Mode.CUBIC_BEZIER
                this.color = color
                setCircleColor(color)
                dataSets.add(this)
            }
        }
    }

}

