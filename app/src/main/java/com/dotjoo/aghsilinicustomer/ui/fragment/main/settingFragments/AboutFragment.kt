package com.dotjoo.aghsilinicustomer.ui.fragment.main.settingFragments

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghsilinicustomer.R
import com.dotjoo.aghsilinicustomer.base.BaseFragment
import com.dotjoo.aghsilinicustomer.databinding.FragmentAboutBinding
import com.dotjoo.aghsilinicustomer.ui.activity.MainActivity
 import com.dotjoo.aghsilinicustomer.util.ext.hideKeyboard
import com.dotjoo.aghsilinicustomer.util.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.toolbar.view.card_back

@AndroidEntryPoint
class AboutFragment : BaseFragment<FragmentAboutBinding>() {
    val mViewModel: SettingViewModel by viewModels()
    override fun onFragmentReady() {
        onClick()
        mViewModel.apply {
            getAbout()
            observe(viewState) {
                handleViewState(it)
            }

        }
        binding.swiperefreshHome.setOnRefreshListener {
            mViewModel.getAbout()
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

                } else if (it.contains("aghsilini.com") == true) {
                    showToast(resources.getString(R.string.connection_error))
                }else {
                    showToast(action.message)
                    showProgress(false)
                }
            }

            is SettingAction.ShowAbout -> {
          binding.lytData.isVisible= true
                binding.tvDesc.setText(action.data.terms?.body)
            }

            else -> {

            }
        }
    }


lateinit var parent: MainActivity
private fun onClick() {
    parent = requireActivity() as MainActivity
    parent.showBottomNav(false)
    binding.toolbar.card_back.setOnClickListener {
        findNavController().navigateUp()
    }
}

}