package com.dotjoo.aghsilinicustomer.ui.fragment.auth.login


import android.graphics.Paint
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghsilinicustomer.R
import com.dotjoo.aghsilinicustomer.base.BaseFragment
import com.dotjoo.aghsilinicustomer.data.PrefsHelper
 import com.dotjoo.aghsilinicustomer.databinding.FragmentLoginBinding
import com.dotjoo.aghsilinicustomer.ui.activity.AuthActivity
import com.dotjoo.aghsilinicustomer.ui.activity.MainActivity
import com.dotjoo.aghsilinicustomer.util.ext.hideKeyboard
import com.dotjoo.aghsilinicustomer.util.ext.showActivity
import com.dotjoo.aghsilinicustomer.util.observe
import com.hbb20.CountryCodePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.toolbar.view.iv_back
import kotlinx.android.synthetic.main.toolbar.view.tv_title

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(),
    CountryCodePicker.OnCountryChangeListener {
    var countryCode ="+966"
    lateinit var parent: AuthActivity
    private val mViewModel: AuthViewModel by viewModels()

    override fun onFragmentReady() {
        onClick()
        mViewModel.apply {
            observe(viewState) {
                handleViewState(it)
            }
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

            is AuthAction.LoginSuccess -> {
                showProgress(false)
              //  action.data.client?.social= action.social
                PrefsHelper.saveToken(action.data.token)
                //  PrefsHelper.saveUserData(action.data)
              //  PrefsHelper.saveToken(action.data.client?.token)
                goHome()

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


    private fun onClick() {
        parent = requireActivity() as AuthActivity
        binding.toolbar.tv_title.setText(resources.getString(R.string.login))
        binding.tvForgetpass.setPaintFlags(binding.tvForgetpass.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        binding.countryCodePicker.setOnCountryChangeListener(this)


        binding.btnSignin.setOnClickListener {
       mViewModel.isVaildLogin(countryCode , binding.etPhone.text.toString(), binding.etPassword.text.toString())
       //     PrefsHelper.saveToken("ggggggg")
         //   goHome()
        }
        binding.btnSignup.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)

        }
          binding.tvForgetpass.setOnClickListener {
            findNavController().navigate(R.id.forgetPasswordFragment)

        }
        binding.toolbar.iv_back.setOnClickListener {
            PrefsHelper.saveToken("")
            goHome()
        }
    }

    fun goHome() {
        showActivity(MainActivity::class.java, clearAllStack = true)

    }

    override fun onCountrySelected() {
        countryCode = "+" + binding.countryCodePicker.selectedCountryCode
    }
}