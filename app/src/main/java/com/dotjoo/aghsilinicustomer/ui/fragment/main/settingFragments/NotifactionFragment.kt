package com.dotjoo.aghsilinicustomer.ui.fragment.main.settingFragments


import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghsilinicustomer.R
import com.dotjoo.aghsilinicustomer.base.BaseFragment
 import com.dotjoo.aghsilinicustomer.databinding.FragmentNotifactionBinding
import com.dotjoo.aghsilinicustomer.ui.activity.MainActivity
 import com.dotjoo.aghsilinicustomer.ui.adapter.NotifactionAdapter
import com.dotjoo.aghsilinicustomer.util.ext.hideKeyboard
import com.dotjoo.aghsilinicustomer.util.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.toolbar.view.card_back
import kotlinx.android.synthetic.main.toolbar.view.tv_title

@AndroidEntryPoint
class NotifactionFragment : BaseFragment<FragmentNotifactionBinding>() {
    val mViewModel: SettingViewModel by viewModels()

    private val adapter by lazy { NotifactionAdapter() }

    override fun onFragmentReady() {
        onClick()
initAdapter()
        mViewModel.apply {
            getNotifaction()
            observe(viewState) {
                handleViewState(it)
            }

        }
        binding.swiperefreshHome.setOnRefreshListener {
            mViewModel.getNotifaction()
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

            is SettingAction.ShowNotifactions -> {
                action.data?.notificationItem?.let {
                  if(it.size>0) {
                      binding.lytEmptyState.isVisible= false
                      adapter.notifactionsItemsList = it
                  }
                    else{
                        binding.lytEmptyState.isVisible= true
                  }
                 }
            }

            else -> {

            }
        }
    }
    private fun initAdapter() {
        binding.rvNotifactions.adapter = adapter

    }

    lateinit var parent: MainActivity
    private fun onClick() {
        parent = requireActivity() as MainActivity
        parent.showBottomNav(false)
        binding.toolbar.tv_title.setText(resources.getString(R.string.notifaction))
        binding.toolbar.card_back.setOnClickListener {
            findNavController().navigateUp()
        }   }

}