package com.dotjoo.aghsilinicustomer.ui.fragment.main.account

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghsilinicustomer.R
import com.dotjoo.aghsilinicustomer.base.BaseFragment
import com.dotjoo.aghsilinicustomer.data.Param.UpdateProfileParam
import com.dotjoo.aghsilinicustomer.data.PrefsHelper
import com.dotjoo.aghsilinicustomer.databinding.FragmentEditProfileBinding
import com.dotjoo.aghsilinicustomer.ui.activity.MainActivity
import com.dotjoo.aghsilinicustomer.ui.dialog.CheckOtpSheetFragment
import com.dotjoo.aghsilinicustomer.ui.dialog.OnPhoneCheckedWithOtp
import com.dotjoo.aghsilinicustomer.util.ToastUtils
import com.dotjoo.aghsilinicustomer.util.ext.hideKeyboard
import com.dotjoo.aghsilinicustomer.util.observe
import com.hbb20.CountryCodePicker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileFragment : BaseFragment<FragmentEditProfileBinding>(),
    CountryCodePicker.OnCountryChangeListener {
    val mViewModel: AccountViewModel by viewModels()
    var countryCode = ""
    override fun onFragmentReady() {
        onclick()
        mViewModel.apply {
            getProfile()
            observe(viewState) {
                handleViewState(it)
            }

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
                } else {
                    ToastUtils.showToast(requireContext(), action.message)
                    showProgress(false)
                }
            }

            is AccountAction.ShowProfileUpdated -> {
                action?.message?.let {
                    showToast(it)
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

    lateinit var parent: MainActivity

    private fun setupUi() {
        binding.countryCodePicker.setOnCountryChangeListener(this)

        parent = requireActivity() as MainActivity
        parent.showBottomNav(false)
        binding.tvName.setText(PrefsHelper.getUserData()?.name)
        binding.etName.setText(PrefsHelper.getUserData()?.name)
        binding.etPhone.setText(PrefsHelper.getUserData()?.phone)
        PrefsHelper.getUserData()?.country_code?.toInt()?.let {
            binding.countryCodePicker.setCountryForPhoneCode(it)

        }
        countryCode = PrefsHelper.getUserData()?.country_code.toString()
        binding.lytData.isVisible = true
    }
    var verified_countryCode = ""
    var verified_phone: String? = null
    private fun onclick() {
        binding.btnSignup.setOnClickListener {
            mViewModel.updateProfile(
                UpdateProfileParam(
                    binding.etName.text.toString(), countryCode, binding.etPhone.text.toString()
                ))
/*            if (!verified_phone.isNullOrEmpty() || verified_phone !=null) {
                mViewModel.updateProfile(
                    UpdateProfileParam(
                        binding.etName.text.toString(), countryCode, binding.etPhone.text.toString()
                    )
                )

            }
            else {
                if( binding.etPhone.text.toString()== PrefsHelper.getUserData()?.phone.toString()  && "+"+binding.countryCodePicker.selectedCountryCode.toString() == PrefsHelper.getUserData()?.country_code)
                    mViewModel.updateProfile(
                        UpdateProfileParam(
                            binding.etName.text.toString(), countryCode, binding.etPhone.text.toString()
                        )
                    )else{
                    CheckOtpSheetFragment.newInstance(countryCode,
                        binding.etPhone.text.toString(),
                        object : OnPhoneCheckedWithOtp {
                            override fun onClick(
                                country_code: String, phone: String, verifed: Boolean
                            ) {
                                verified_phone = phone
                                verified_countryCode = country_code
                                mViewModel.updateProfile(
                                    UpdateProfileParam(
                                        binding.etName.text.toString(), countryCode, binding.etPhone.text.toString()
                                    ))
                            }
                        }).show(
                        childFragmentManager, "CheckOtpSheetFragment"
                    )
                }
            }*/
        }
    }
    override fun onCountrySelected() {
        countryCode = "+" + binding.countryCodePicker.selectedCountryCode
    }

}