package com.app.incomeapp.ui.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.incomeapp.R
import com.app.incomeapp.databinding.DeleteInputPopupBinding
import com.app.incomeapp.databinding.LayoutAddViewIncomeCostBinding
import com.app.incomeapp.ui.viewmodels.AddEditIncomeCostViewModel
import com.app.incomeapp.utils.BlurUtils.blurImg
import com.app.incomeapp.utils.BlurUtils.takeScreenshotOfView
import com.app.incomeapp.utils.showCustomToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditIncomeCost : Fragment() {

    lateinit var bindView: LayoutAddViewIncomeCostBinding
    lateinit var deleteInputBind: DeleteInputPopupBinding
    val viewmodel by viewModels<AddEditIncomeCostViewModel>()
    private var itemId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback {
            popFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        bindView = DataBindingUtil.inflate(
            inflater,
            R.layout.layout_add_view_income_cost,
            container,
            false
        )

        bindView.data = viewmodel
        bindView.lifecycleOwner = this
        setHasOptionsMenu(true)

        arguments?.getInt("id").let { id ->
            viewmodel.forEditItemId = id
            if (id != null && id > 0) {
                viewmodel.getIncomeCost(id)
                itemId = id
            }
        }

        bindView.backBtn.setOnClickListener { popFragment() }

        deleteInputBind = DataBindingUtil.inflate(
            layoutInflater, R.layout.delete_input_popup, null, false
        )

        viewmodel.toastMessage.observe(viewLifecycleOwner) { message ->
            this@AddEditIncomeCost.requireContext().showCustomToast(message)
        }

        viewmodel.popFragment.observe(viewLifecycleOwner) { boolean ->
            if (boolean == true) {
                popFragment()
                viewmodel.setPopFragmentFalse()
            }
        }

        bindView.deleteBtn.setOnClickListener {
            val bmp = takeScreenshotOfView(bindView.root, bindView.root.height, bindView.root.width)
            val bluredBmp = blurImg(requireContext(), bmp, 25f)
            bindView.blurredImgV.apply {
                visibility = VISIBLE
                setImageBitmap(bluredBmp)
            }
            showDeleteDialog()
        }

        return bindView.root
    }

    private fun showDeleteDialog() {
        Dialog(requireContext(), R.style.deleteItemDialogStyle).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
                attributes?.also { attributes ->
                    attributes.dimAmount = 0.0f
                    this.attributes = attributes
                }
            }
            DataBindingUtil.inflate<DeleteInputPopupBinding>(
                LayoutInflater.from(requireContext()),
                R.layout.delete_input_popup,
                this.findViewById(R.id.parent),
                false
            ).let { dialogBindView ->
                dialogBindView.parentFrame.let {imgV ->
                    imgV.scaleType = ImageView.ScaleType.FIT_XY
                }
                setCancelable(false)
                setContentView(dialogBindView.root)
                dialogBindView.positiveBtn.setOnClickListener {
                    deleteItem(itemId)
                    removeBluredImg()
                    this.dismiss()
                }
                dialogBindView.negativeBtn.setOnClickListener {
                    removeBluredImg()
                    this.dismiss()
                }
            }
        }.show()

//        AppPopUp(
//            deleteInputBind.root, requireActivity()
//        ).apply {
//            deleteInputBind.positiveBtn.setOnClickListener {
//                deleteItem(itemId)
//                dismiss()
//            }
//            deleteInputBind.negativeBtn.setOnClickListener { dismiss() }
//            show()
//        }

    }

    private fun removeBluredImg() {
        bindView.blurredImgV.visibility = GONE
    }

    private fun deleteItem(id: Int) {
        viewmodel.deleteIncomeCost(id)
        popFragment()
    }

    private fun popFragment() {
        findNavController().navigate(R.id.action_addEditIncomeCost_to_mainFragment)
    }

}