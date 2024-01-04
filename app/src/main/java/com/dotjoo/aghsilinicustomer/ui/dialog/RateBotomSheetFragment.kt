package com.dotjoo.aghsilinicustomer.ui.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghsilinicustomer.R
import com.dotjoo.aghsilinicustomer.data.remote.response.Laundry
import com.dotjoo.aghsilinicustomer.databinding.DialogRateSheetBinding
 import com.dotjoo.aghsilinicustomer.ui.activity.MainActivity
import com.dotjoo.aghsilinicustomer.ui.fragment.main.order.OrderAction
import com.dotjoo.aghsilinicustomer.ui.fragment.main.order.OrderViewModel
import com.dotjoo.aghsilinicustomer.util.ToastUtils
import com.dotjoo.aghsilinicustomer.util.ext.hideKeyboard
import com.dotjoo.aghsilinicustomer.util.ext.loadImage
import com.dotjoo.aghsilinicustomer.util.observe
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class RateBotomSheetFragment(val lan: Laundry, val order_Id: String , val onClick :OnClickLoginFirst ) :  BottomSheetDialogFragment() {

    private lateinit var binding: DialogRateSheetBinding
    private lateinit var parent: MainActivity
    val mViewModel: OrderViewModel by viewModels()

    companion object {
        fun newInstance(lan: Laundry, order_Id: String,  onClick :OnClickLoginFirst ): RateBotomSheetFragment {
            val args = Bundle()
            val f = RateBotomSheetFragment(lan ,order_Id,onClick
            )
            f.arguments = args
            return f
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DialogRateSheetBinding.inflate(inflater)
        parent = requireActivity() as MainActivity
        onClick()
        mViewModel.apply {
            observe(viewState) {
                handleViewState(it)
            }

        }

        return binding.root
    }

    fun handleViewState(action: OrderAction) {
        when (action) {
            is OrderAction.ShowLoading -> {
                showProgress(action.show)
                if (action.show) {
                    hideKeyboard()
                }
            }

            is OrderAction.ShowFailureMsg -> action.message?.let {
                if (it.contains("401") == true) {
                    findNavController().navigate(R.id.loginFirstBotomSheetFragment)
                }else if (it.contains("aghsilini.com") == true) {
                    ToastUtils.showToast(
                        requireContext(),
                        resources.getString(R.string.connection_error)
                    )
                } else {
                    ToastUtils.showToast(requireContext(), action.message)
                    showProgress(false)
                }
            }

            is OrderAction.OrderReviewed -> {
                action.message?.let { ToastUtils.showToast(requireContext(), it) }
         onClick.onClick("")
                dismiss()
            }

            else -> {

            }
        }
    }

    private fun showProgress(show: Boolean) {
        binding.progressBar.isVisible= show
    }


    private fun onClick() {

        binding.tvLaundryName.text = lan?.name
        binding.tvAddress.text = lan?.address
        binding.tvRate.text = lan?.rate
        binding.ivlogo.loadImage(lan?.logo, isCircular = true)
        binding.btnSignup.setOnClickListener {
            if(binding.etNote.text.isNullOrEmpty())   ToastUtils.showToast(requireContext(), resources.getString(R.string.enter_complain))
         else if(binding.rating.rating==0f)  ToastUtils.showToast(requireContext(), resources.getString(R.string.please_rate))
            else lan.id?.let { it1 -> mViewModel.reviewLaundry(order_Id,binding.rating.rating.toString(), binding.etNote.text.toString(), it1) }
        }
    }


    @SuppressLint("CutPasteId")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener {
            val bottomSheet =
                bottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            val behavior: BottomSheetBehavior<*> =
                BottomSheetBehavior.from(bottomSheet!!)
            behavior.skipCollapsed = true
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        return bottomSheetDialog
    }

    override fun getTheme() = R.style.CustomBottomSheetDialogTheme

}