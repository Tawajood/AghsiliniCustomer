package com.dotjoo.aghsilinicustomer.ui.fragment.main.account

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghsilinicustomer.R
import com.dotjoo.aghsilinicustomer.base.BaseFragment
import com.dotjoo.aghsilinicustomer.data.PrefsHelper
import com.dotjoo.aghsilinicustomer.databinding.FragmentAccountBinding
import com.dotjoo.aghsilinicustomer.ui.activity.MainActivity
import com.dotjoo.aghsilinicustomer.util.ToastUtils
import com.dotjoo.aghsilinicustomer.util.ext.hideKeyboard
import com.dotjoo.aghsilinicustomer.util.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment : BaseFragment<FragmentAccountBinding>() {
    val mViewModel: AccountViewModel by viewModels()

    override fun onFragmentReady() {
        onclick()
       //aaaaaaaaa setupUi()
        mViewModel.apply {
            getProfile()
            observe(viewState) {
                handleViewState(it)
            }

        }
    binding.swiperefreshHome.setOnRefreshListener {
        mViewModel.getProfile()
         if (binding.swiperefreshHome != null) binding.swiperefreshHome.isRefreshing = false
    }


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
                    showToast(resources.getString(R.string.connection_error))
                }else {
                    ToastUtils.showToast(requireContext(), action.message)
                    showProgress(false)
                }
            }

            is AccountAction.ShowProfile -> {
              PrefsHelper.saveUserData(action.data.user)
                setupUi()
            }

            else -> {

            }
        }
    }

    private fun setupUi() {
 binding.tvName.setText(PrefsHelper.getUserData()?.name)
  binding.etName.setText(PrefsHelper.getUserData()?.name)
  binding.lytData.isVisible= true
        binding.etPhone.setText(PrefsHelper.getUserData()?.phone)
    }

    lateinit var parent: MainActivity
    private fun onclick() {
        parent = requireActivity() as MainActivity
        parent.showBottomNav(true)
        binding.lytDeletAcc.setOnClickListener {
          findNavController().navigate(R.id.deleteAccountSheetFragment)
        }
        binding.lytChangePass.setOnClickListener {
            findNavController().navigate(R.id.changePasswordSheetFragment)
        }

        binding.cardEdit.setOnClickListener {
            findNavController().navigate(R.id.editProfileFragment) }
     }

}