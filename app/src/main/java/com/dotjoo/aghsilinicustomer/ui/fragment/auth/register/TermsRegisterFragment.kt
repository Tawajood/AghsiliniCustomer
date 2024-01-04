package com.dotjoo.aghsilinicustomer.ui.fragment.auth.register

import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghsilinicustomer.R
import com.dotjoo.aghsilinicustomer.base.BaseFragment
 import com.dotjoo.aghsilinicustomer.databinding.FragmentTermsBinding
import com.dotjoo.aghsilinicustomer.ui.activity.AuthActivity
import com.dotjoo.aghsilinicustomer.ui.activity.MainActivity
import com.dotjoo.aghsilinicustomer.ui.fragment.auth.forget_password.login.AuthAction
import com.dotjoo.aghsilinicustomer.ui.fragment.auth.forget_password.login.AuthViewModel
import com.dotjoo.aghsilinicustomer.ui.fragment.main.settingFragments.SettingAction
import com.dotjoo.aghsilinicustomer.util.ext.hideKeyboard
import com.dotjoo.aghsilinicustomer.util.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.toolbar.view.card_back

@AndroidEntryPoint
class TermsRegisterFragment:BaseFragment<FragmentTermsBinding>() {
    val mViewModel: AuthViewModel by viewModels()
    override fun onFragmentReady() {
        onClick()
        mViewModel.apply {
            getTerms()
            observe(viewState) {
                handleViewState(it)
            }
        }
        binding.swiperefreshHome.setOnRefreshListener {
            mViewModel.getTerms()
            binding.swiperefreshHome.isRefreshing = false
        }
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

                }else if (it.contains("aghsilini.com") == true) {
                    showToast(resources.getString(R.string.connection_error))
                } else {
                    showToast(action.message)
                    showProgress(false)
                }
            }
            is AuthAction.ShowTerms -> {
                binding.lytData.isVisible= true
                binding.tvDesc.setText(action.data.terms?.body)
            }

            else -> {

            }
        }
    }


    lateinit var parent: AuthActivity
    private fun onClick() {
    try {
        parent = requireActivity() as AuthActivity
        //parent.showBottomNav(false)
    }   catch (E:Exception){

    }
        binding.toolbar.card_back.setOnClickListener {
            findNavController().navigateUp()
        }
     }

    private fun onBack() {
        activity?.let {
            requireActivity().onBackPressedDispatcher.addCallback(
                it,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {

                        if (isEnabled) {
                            isEnabled = false
                            findNavController().navigateUp()
                        }
                    }
                })
        }
    }


}