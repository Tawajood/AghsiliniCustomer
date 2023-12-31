package com.dotjoo.aghsilinicustomer.ui.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghsilinicustomer.R
import com.dotjoo.aghsilinicustomer.data.PrefsHelper
import com.dotjoo.aghsilinicustomer.databinding.DialogChangePassSheetBinding
 import com.dotjoo.aghsilinicustomer.ui.activity.MainActivity
import com.dotjoo.aghsilinicustomer.ui.fragment.main.account.AccountAction
import com.dotjoo.aghsilinicustomer.ui.fragment.main.account.AccountViewModel
import com.dotjoo.aghsilinicustomer.util.ToastUtils
import com.dotjoo.aghsilinicustomer.util.ext.hideKeyboard
import com.dotjoo.aghsilinicustomer.util.observe
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ChangePasswordSheetFragment( ) : BottomSheetDialogFragment() {
    val mViewModel: AccountViewModel by viewModels()

    private lateinit var binding: DialogChangePassSheetBinding
    private lateinit var parent: MainActivity

    companion object {
        fun newInstance( ): ChangePasswordSheetFragment {
            val args = Bundle()
            val f = ChangePasswordSheetFragment( )
            f.arguments = args
            return f
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = DialogChangePassSheetBinding.inflate(inflater)
        parent = requireActivity() as MainActivity
        onClick()
        mViewModel.apply {
            observe(viewState) {
                handleViewState(it)
            }

        }

        return binding.root
    }

    fun handleViewState(action: AccountAction) {
        when (action) {
            is AccountAction.ShowLoading -> {
                showProgress(action.show)
                if (action.show) {
                    hideKeyboard()
                }
            }

            is AccountAction.ShowFailureMsg -> action.message?.let {
                if (it.contains("401") == true) {
                    findNavController().navigate(R.id.loginFirstBotomSheetFragment)
                } else if (it.contains("aghsilini.com") == true) {
                    ToastUtils.showToast(
                        requireContext(),
                        resources.getString(R.string.connection_error)
                    )
                }else {
                    ToastUtils.showToast(requireContext(), action.message)
                    showProgress(false)
                }
            }

            is AccountAction.PasswordChanged -> {
                action.message?.let { ToastUtils.showToast(requireContext(), it) }
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
        binding.btnSignup.setOnClickListener {
            mViewModel.isValidParamsChangePass(
                binding.etPassword.text.toString(),
                binding.etNewpassword.text.toString(),
                binding.etPasswordConfim.text.toString(),
            )
        }
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