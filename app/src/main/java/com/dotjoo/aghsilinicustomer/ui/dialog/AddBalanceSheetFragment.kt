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
import com.dotjoo.aghsilinicustomer.databinding.DialogAddBalanceSheetBinding
 import com.dotjoo.aghsilinicustomer.ui.activity.MainActivity
import com.dotjoo.aghsilinicustomer.ui.fragment.main.settingFragments.SettingAction
import com.dotjoo.aghsilinicustomer.ui.fragment.main.settingFragments.SettingViewModel
import com.dotjoo.aghsilinicustomer.util.ToastUtils.Companion.showToast
import com.dotjoo.aghsilinicustomer.util.ext.hideKeyboard
import com.dotjoo.aghsilinicustomer.util.observe
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddBalanceSheetFragment(var onClick: OnClickLoginFirst) : BottomSheetDialogFragment() {

    val mViewModel: SettingViewModel by viewModels()
    private lateinit var binding: DialogAddBalanceSheetBinding
    private lateinit var parent: MainActivity

    companion object {
        fun newInstance(onClick: OnClickLoginFirst): AddBalanceSheetFragment {
            val args = Bundle()
            val f = AddBalanceSheetFragment(onClick)
            f.arguments = args
            return f
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = DialogAddBalanceSheetBinding.inflate(inflater)
        parent = requireActivity() as MainActivity
        onClick()
        mViewModel.apply {
            observe(viewState) {
                handleViewState(it)
            }

        }

        return binding.root
    }

    fun handleViewState(action: SettingAction) {
        when (action) {
            is SettingAction.ShowLoading -> {
                showProgress(action.show)
                if (action.show) {
                    hideKeyboard()
                }
            }


            is SettingAction.ShowFailureMsg -> action.message?.let {
                if (it.contains("401") == true) {
                    findNavController().navigate(R.id.loginFirstBotomSheetFragment)

                }else if (it.contains("aghsilini.com") == true) {
                    showToast(requireContext(),resources.getString(R.string.connection_error))
                } else {
                    showToast(requireContext(), action.message)
                    showProgress(false)
                }
            }

            is SettingAction.ShowWalletCharged -> {
                onClick.onClick("d")
                action.message?.let { showToast(requireContext(), it) }
                dismiss()
            }

            else -> {

            }
        }
    }


    private fun onClick() {
        binding.btnAdd.setOnClickListener {
            if(binding.etAmount.text.isNullOrEmpty())showToast(requireContext(), resources.getString(R.string.please_enter_amount))
              else {
                mViewModel.chargeWallet(
                    binding.etAmount.text.toString(),
                     "111111111111"
                )
            }
            }
    }


    private fun showProgress(show: Boolean) {
        binding.progressBar.isVisible = show
    }

    @SuppressLint("CutPasteId")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener {
            val bottomSheet =
                bottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet!!)
            behavior.skipCollapsed = true
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        return bottomSheetDialog
    }

    override fun getTheme() = R.style.CustomBottomSheetDialogTheme

}