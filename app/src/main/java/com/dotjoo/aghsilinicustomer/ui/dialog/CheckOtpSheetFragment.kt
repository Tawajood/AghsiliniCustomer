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
 import com.dotjoo.aghsilinicustomer.databinding.DialogCheckOtpBinding
 import com.dotjoo.aghsilinicustomer.ui.activity.MainActivity
import com.dotjoo.aghsilinicustomer.ui.fragment.auth.login.AuthAction
import com.dotjoo.aghsilinicustomer.ui.fragment.auth.login.AuthViewModel
 import com.dotjoo.aghsilinicustomer.util.ToastUtils
import com.dotjoo.aghsilinicustomer.util.ToastUtils.Companion.showToast
import com.dotjoo.aghsilinicustomer.util.ext.hideKeyboard
 import com.dotjoo.aghsilinicustomer.util.observe
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

interface OnPhoneCheckedWithOtp{
    fun onClick(country_code: String,phone: String,verifed: Boolean )
}
@AndroidEntryPoint
class CheckOtpSheetFragment(val country_code: String, val phone: String , val onClick:OnPhoneCheckedWithOtp ) :  BottomSheetDialogFragment() {

    private lateinit var binding: DialogCheckOtpBinding
    private lateinit var parent: MainActivity
    val mViewModel: AuthViewModel by viewModels()

    companion object {
        fun newInstance(country_code: String, phone: String,onClick:OnPhoneCheckedWithOtp  ): CheckOtpSheetFragment {
            val args = Bundle()
            val f = CheckOtpSheetFragment(country_code ,phone , onClick
            )
            f.arguments = args
            return f
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DialogCheckOtpBinding.inflate(inflater)
         onClick()
        mViewModel.apply {
            observe(viewState) {
                handleViewState(it)
            }

        }

        return binding.root
    }

    fun handleViewState(action: AuthAction) {
        when (action) {
            is AuthAction.ShowLoading -> {
                showProgress(action.show)
                if (action.show) {
                    hideKeyboard()
                }
            }

            is AuthAction.ShowFailureMsg -> action.message?.let {
                if (it.contains("401") == true) {
                    findNavController().navigate(R.id.loginFirstBotomSheetFragment)
                } else {
                    ToastUtils.showToast(requireContext(), action.message)
                    showProgress(false)
                }
            }


            is AuthAction.OtpChecked -> {
                 showProgress(false)
                onClick.onClick(country_code, phone, true)
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
        binding.btnEnterOtp.setOnClickListener {
            if (binding.etOtp.otp.toString()
                    .isNullOrEmpty()
            ) showToast(requireContext(), resources.getString(R.string.msg_empty_otp))
            else {
                mViewModel.otp = binding.etOtp.otp.toString()
                mViewModel.checkOtp(
                    country_code, phone, mViewModel.otp.toString()
                )
            }
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