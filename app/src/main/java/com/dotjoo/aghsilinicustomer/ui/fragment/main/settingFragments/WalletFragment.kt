package com.dotjoo.aghsilinicustomer.ui.fragment.main.settingFragments

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghsilinicustomer.R
import com.dotjoo.aghsilinicustomer.base.BaseFragment
import com.dotjoo.aghsilinicustomer.databinding.FragmentWalletBinding
import com.dotjoo.aghsilinicustomer.ui.activity.MainActivity
import com.dotjoo.aghsilinicustomer.ui.dialog.AddBalanceSheetFragment
import com.dotjoo.aghsilinicustomer.ui.dialog.ComplainBotomSheetFragment
import com.dotjoo.aghsilinicustomer.ui.dialog.OnClickLoginFirst
import com.dotjoo.aghsilinicustomer.util.ext.hideKeyboard
import com.dotjoo.aghsilinicustomer.util.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WalletFragment : BaseFragment<FragmentWalletBinding>() {

    val mViewModel: SettingViewModel by viewModels()
    override fun onFragmentReady() {
        onClick()
        mViewModel.apply {
            getWallet()
            observe(viewState) {
                handleViewState(it)
            }

        }
        binding.swiperefreshHome.setOnRefreshListener {
            mViewModel.getWallet()
            if (binding.swiperefreshHome != null) binding.swiperefreshHome.isRefreshing = false
        }
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

                } else {
                    showToast(action.message)
                    showProgress(false)
                }
            }

            is SettingAction.ShowWalletCharged -> {
                mViewModel.getWallet()
            }

            is SettingAction.ShowBalanceInWallet -> {
                binding.tvBalance.setText(action.data.wallet + resources.getString(R.string.sr))
            }

            else -> {

            }
        }
    }

    lateinit var parent: MainActivity
    private fun onClick() {
        parent = requireActivity() as MainActivity
        parent.showBottomNav(false)

        binding.btnAddBalance.setOnClickListener {
            AddBalanceSheetFragment.newInstance(object : OnClickLoginFirst {
                override fun onClick(choice: String) {

                    mViewModel.getWallet()
                }

            }).show(childFragmentManager, AddBalanceSheetFragment::class.java.canonicalName)
        }
    }
}

