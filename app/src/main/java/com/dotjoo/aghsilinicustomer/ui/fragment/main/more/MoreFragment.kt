package com.dotjoo.aghsilinicustomer.ui.fragment.main.more

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghsilinicustomer.R
import com.dotjoo.aghsilinicustomer.base.BaseFragment
import com.dotjoo.aghsilinicustomer.data.PrefsHelper
import com.dotjoo.aghsilinicustomer.databinding.FragmentMoreBinding
import com.dotjoo.aghsilinicustomer.ui.activity.AuthActivity
import com.dotjoo.aghsilinicustomer.ui.activity.MainActivity
import com.dotjoo.aghsilinicustomer.ui.fragment.auth.login.AuthAction
import com.dotjoo.aghsilinicustomer.ui.fragment.auth.login.AuthViewModel
import com.dotjoo.aghsilinicustomer.util.Constants
import com.dotjoo.aghsilinicustomer.util.ext.hideKeyboard
import com.dotjoo.aghsilinicustomer.util.ext.showActivity
import com.dotjoo.aghsilinicustomer.util.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoreFragment : BaseFragment<FragmentMoreBinding>() {
    private val mViewModel: AuthViewModel by viewModels()
    override fun onFragmentReady() {
        onClick()
        setupUi()
        mViewModel.apply {
            observe(viewState) {
                handleViewState(it)
            }
        }
    }

    private fun setupUi() {
        if(PrefsHelper.getToken().isNullOrEmpty()){
            binding.tvWallet.isVisible= false
            binding.v4.isVisible= false
            binding.v8.isVisible= false
            binding.tvNotifaction.isVisible= false
            binding.v9.isVisible= false
            binding.tvIt.isVisible= false

             binding.tvLogout.setText(resources.getText(R.string.login))
        }else{
            binding.tvWallet.isVisible= true
             binding.v4.isVisible= true
        }

    }

    private fun handleViewState(action: AuthAction) {
        when (action) {
            is AuthAction.ShowLoading -> {
                showProgress(action.show)
                if (action.show) {
                    hideKeyboard()
                }
            }

            is AuthAction.LogoutSucess -> {
                showProgress(false)
                PrefsHelper.clear()
                var intent = Intent(activity, AuthActivity::class.java)
                intent.putExtra(Constants.Start, Constants.login)
                activity?. startActivity(intent)
                activity?.finish()
            }



            is AuthAction.ShowFailureMsg -> action.message?.let {
                if (it.contains("401") == true) {
                    showToast(it.substring(3, it.length))
                } else {
                    showToast(action.message)
                }
                showProgress(false)

            }

            else -> {

            }
        }
    }
    lateinit var parent: MainActivity
    private fun onClick() {
        binding.tvLogout.setPaintFlags(binding.tvLogout.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        parent = requireActivity() as MainActivity
        parent.showBottomNav(true)

            binding.tvWallet.setOnClickListener {
                findNavController().navigate(R.id.walletFragment)
            }
            binding.tvContactus.setOnClickListener {
                findNavController().navigate(R.id.contactUsFragment)
            }
            binding.tvAboutUs.setOnClickListener {
                findNavController().navigate(R.id.aboutFragment)
            }
           binding.tvSetting.setOnClickListener {
           //     findNavController().navigate(R.id.sett)
            }
           binding.tvTerms.setOnClickListener {
                findNavController().navigate(R.id.termsFragment)
            }
           binding.tvNotifaction.setOnClickListener {
                findNavController().navigate(R.id.notifactionFragment)
            }
           binding.tvIt.setOnClickListener {
                findNavController().navigate(R.id.itFragment)
            }
             binding.cardAddress.setOnClickListener {
                 if(PrefsHelper.getToken().isNullOrEmpty()) {
                     findNavController().navigate(R.id.loginFirstBotomSheetFragment)

                 }else{
                     findNavController().navigate(R.id.addressFragment)

                 }
             }
            binding.tvLogout.setOnClickListener {
                if(PrefsHelper.getToken().isNullOrEmpty()) {
                    PrefsHelper.clear()
                    var intent = Intent(activity, AuthActivity::class.java)
                    intent.putExtra(Constants.Start, Constants.login)
                    activity?. startActivity(intent)
                    activity?.finish()

                }else{
                    mViewModel.logout()

                }
            }

        }

}