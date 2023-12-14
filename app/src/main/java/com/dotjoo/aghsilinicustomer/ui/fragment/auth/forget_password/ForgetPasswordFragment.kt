package com.dotjoo.aghsilinicustomer.ui.fragment.auth.forget_password

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghsilinicustomer.R
import com.dotjoo.aghsilinicustomer.base.BaseFragment
import com.dotjoo.aghsilinicustomer.data.PrefsHelper
import com.dotjoo.aghsilinicustomer.databinding.FragmentForgetPasswordBinding
import com.dotjoo.aghsilinicustomer.ui.activity.AuthActivity
import com.dotjoo.aghsilinicustomer.ui.fragment.auth.login.AuthAction
import com.dotjoo.aghsilinicustomer.ui.fragment.auth.login.AuthViewModel
import com.dotjoo.aghsilinicustomer.util.Constants
import com.dotjoo.aghsilinicustomer.util.ext.hideKeyboard
import com.dotjoo.aghsilinicustomer.util.observe
import com.hbb20.CountryCodePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.toolbar.view.card_back
import kotlinx.android.synthetic.main.toolbar.view.tv_title

@AndroidEntryPoint
class ForgetPasswordFragment : BaseFragment<FragmentForgetPasswordBinding>(),
    CountryCodePicker.OnCountryChangeListener {
    var state = 1

    private val mViewModel: AuthViewModel by viewModels()
    var countryCode = "+966"
    override fun onFragmentReady() {
        state1()
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

            is AuthAction.PhoneChecked -> {
                showProgress(false)
                showToast(action.message)
                state2()

            }

            is AuthAction.OtpChecked -> {
                showToast(action.message)
                showProgress(false)
                state3()

            }

            is AuthAction.ResetPasswordSucess -> {
                showToast(action.message)
                showProgress(false)
                PrefsHelper.clear()
                var intent = Intent(activity, AuthActivity::class.java)
                intent.putExtra(Constants.Start, Constants.login)
                startActivity(intent)
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


    private fun onClick() {
        binding.countryCodePicker.setOnCountryChangeListener(this)
        binding.tvResend.setPaintFlags(binding.tvResend.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        binding.btnEnterNumber.setOnClickListener {
            if (binding.etPhone.text.toString()
                    .isNullOrEmpty()
            ) showToast(resources.getString(R.string.msg_empty_phone_number))
            else {
                mViewModel.email = binding.etPhone.text.toString()
                mViewModel.checkPhone(countryCode, mViewModel.email.toString())
            }
        }

        binding.btnEnterOtp.setOnClickListener {
            if (binding.etOtp.otp.toString()
                    .isNullOrEmpty()
            ) showToast(resources.getString(R.string.msg_empty_otp))
            else {
                mViewModel.otp = binding.etOtp.otp.toString()
                mViewModel.checkOtp(
                    countryCode, mViewModel.email.toString(), mViewModel.otp.toString()
                )
            }
        }

        binding.btnEnterPass.setOnClickListener {
            mViewModel.isValidParamsChangePass(
                countryCode,
                binding.etPassword.text.toString(),
                binding.etPasswordConfim.text.toString()
            )
        }

        binding.toolbar.card_back.setOnClickListener {
            if (state == 1) {
                findNavController().navigateUp()
            } else if (state == 2) {
                state1()
            } else {
                state2()
            }
        }
        onBack()
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



    fun state1() {
        binding.toolbar.tv_title.setText(resources.getText(R.string.forget_password))
        state = 1
        binding.lytState1.visibility = View.VISIBLE
        binding.lytState2.visibility = View.GONE
        binding.lytState3.visibility = View.GONE

    }

    fun state2() {
        counterDawn()
        binding.toolbar.tv_title.setText(resources.getText(R.string.otp))
        state = 2
        binding.lytState1.visibility = View.GONE
        binding.lytState2.visibility = View.VISIBLE
        binding.lytState3.visibility = View.GONE

    }

    fun state3() {
        restTimer?.cancel()
        restTimer?.onFinish()


        state = 3
        binding.toolbar.tv_title.setText(resources.getText(R.string.new_password))
        binding.lytState1.visibility = View.GONE
        binding.lytState2.visibility = View.GONE
        binding.lytState3.visibility = View.VISIBLE

    }

    private var restTimer: CountDownTimer? = null
    private fun counterDawn() {
        binding.tvResend.isEnabled = false
        binding.tvResend.setTextColor(resources.getColor(R.color.grey_400))
        binding.tvTimer.setTextColor(resources.getColor(R.color.grey_400))

        restTimer = object : CountDownTimer(120000, 1000) {


            override fun onTick(millisUntilFinished: Long) {
                val seconds: Long = millisUntilFinished / 1000 % 60
                val minutes: Long = (millisUntilFinished - seconds) / 1000 / 60
                binding.tvTimer.text = "" + minutes + ":" + seconds
                Log.d(
                    "remainingremaining g", ("" + minutes + ":" + seconds).toString()
                )
            }


            override fun onFinish() {
                binding.tvResend.setText(resources.getString(R.string.resend))
                binding.tvTimer.text = ""
                binding.tvResend.isEnabled = true
                binding.tvResend.setTextColor(resources.getColor(R.color.black))


            }
        }.start()
    }

    override fun onCountrySelected() {
        countryCode = "+" + binding.countryCodePicker.selectedCountryCode
    }
}