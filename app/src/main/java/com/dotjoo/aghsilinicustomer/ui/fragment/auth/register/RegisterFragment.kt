package com.dotjoo.aghsilinicustomer.ui.fragment.auth.register

import android.graphics.Paint
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghsilinicustomer.R
import com.dotjoo.aghsilinicustomer.base.BaseFragment
import com.dotjoo.aghsilinicustomer.data.Param.AddAddressParams
import com.dotjoo.aghsilinicustomer.data.PrefsHelper
import com.dotjoo.aghsilinicustomer.databinding.FragmentRegisterBinding
import com.dotjoo.aghsilinicustomer.ui.activity.AuthActivity
import com.dotjoo.aghsilinicustomer.ui.activity.MainActivity
import com.dotjoo.aghsilinicustomer.ui.dialog.CheckOtpSheetFragment
import com.dotjoo.aghsilinicustomer.ui.dialog.OnPhoneCheckedWithOtp
import com.dotjoo.aghsilinicustomer.ui.fragment.auth.forget_password.login.AuthAction
import com.dotjoo.aghsilinicustomer.ui.fragment.auth.forget_password.login.AuthViewModel
import com.dotjoo.aghsilinicustomer.util.PermissionManager
import com.dotjoo.aghsilinicustomer.util.WWLocationManager
import com.dotjoo.aghsilinicustomer.util.ext.hideKeyboard
import com.dotjoo.aghsilinicustomer.util.ext.isNull
import com.dotjoo.aghsilinicustomer.util.ext.showActivity
import com.dotjoo.aghsilinicustomer.util.observe
import com.dotjoo.aghsilinicustomer.util.openLocationSettingsResultLauncher
import com.dotjoo.aghsilinicustomer.util.requestAppPermissions
import com.hbb20.CountryCodePicker
import dagger.hilt.android.AndroidEntryPoint
 import kotlinx.android.synthetic.main.toolbar.view.iv_back
import kotlinx.android.synthetic.main.toolbar.view.tv_title
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>(),
    CountryCodePicker.OnCountryChangeListener {
    lateinit var parent: AuthActivity
    private val mViewModel: AuthViewModel by viewModels()
    var countryCode = "+966"
    var verified_countryCode = ""
    var verified_phone: String? = null
    var lat: String? = ""
    var lon: String? = ""
    var address: String? = ""

    @Inject
    lateinit var locationManager: WWLocationManager

    @Inject
    lateinit var permissionManager: PermissionManager
    override fun onFragmentReady() {
        onClick()
        mViewModel.apply {
            observe(viewState) {
                handleViewState(it)
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

    private fun handleViewState(action: AuthAction) {
        when (action) {
            is AuthAction.ShowLoading -> {
                showProgress(action.show)
                if (action.show) {
                    hideKeyboard()
                }
            }

            is AuthAction.RegisterSucess -> {
                showProgress(false)
                //  action.data.client?.social= action.social
                PrefsHelper.saveUserData(action.data.user)
                PrefsHelper.saveToken(action.data.token)
                goHome()

            }

            is AuthAction.ShowRegisterVaildation -> {
                showProgress(false)
                if (verified_phone.isNullOrEmpty() || verified_phone == null) {
                    CheckOtpSheetFragment.newInstance(action.data.country_code,
                        action.data.phone,
                        object : OnPhoneCheckedWithOtp {
                            override fun onClick(
                                country_code: String, phone: String, verifed: Boolean
                            ) {
                                verified_phone = phone
                                verified_countryCode = country_code
                                mViewModel.register(action.data)
                            }
                        }).show(
                        childFragmentManager, "CheckOtpSheetFragment"
                    )

                } else {
                    if (verified_phone == action.data.phone && verified_countryCode == action.data.country_code) {
                        mViewModel.register(action.data)
                    } else{
                        CheckOtpSheetFragment.newInstance(action.data.country_code,
                            action.data.phone,
                            object : OnPhoneCheckedWithOtp {
                                override fun onClick(
                                    country_code: String, phone: String, verifed: Boolean
                                ) {
                                    verified_phone = phone
                                    verified_countryCode = country_code
                                    mViewModel.register(action.data)
                                }
                            }).show(
                            childFragmentManager, "CheckOtpSheetFragment"
                        )
                    }
                }


            }


            is AuthAction.ShowFailureMsg -> action.message?.let {
                if (it.contains("401") == true) {
                    showToast(it.substring(3, it.length))
                }else if (it.contains("aghsilini.com") == true) {
                    showToast(resources.getString(R.string.connection_error))
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
        binding.toolbar.tv_title.setText(resources.getString(R.string.new_user))
        binding.tvTermsandcondito.setPaintFlags(binding.tvTermsandcondito.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        binding.countryCodePicker.setOnCountryChangeListener(this)


        binding.btnSignup.setOnClickListener {


            mViewModel.isVaildRegisteration(

                binding.etName.text.toString(),
                countryCode,
                binding.etPhone.text.toString(),
                lat, lon, address,
                binding.etPassword.text.toString(),
                binding.etPasswordConfim.text.toString(),
            )
        }
        binding.btnSignin.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)

        }
        binding.tvTermsandcondito.setOnClickListener {
            findNavController().navigate(R.id.termsRegisterFragment)

        }
        binding.etAddress.setOnClickListener {
            checkLocation()
        }
        binding.toolbar.iv_back.setOnClickListener {
            PrefsHelper.saveToken("")
            goHome()
        }
        binding.tvSkip.setOnClickListener {
            PrefsHelper.saveToken("")
            goHome()
        }
    }

    fun goHome() {
        showActivity(MainActivity::class.java, clearAllStack = true)

    }


    private fun openMaps() {
        MapBottomSheet.newInstance(object : onLocationClick {
            override fun onClick(lat: Double?, long: Double?, address: AddAddressParams?) {
                this@RegisterFragment.lat = lat.toString()
                this@RegisterFragment.lon = long.toString()
                this@RegisterFragment.address = address?.fullAddress
                if (!address.isNull()) {
                    binding.etAddress.text = address?.fullAddress
                }
            }
        }, null).show(childFragmentManager, MapBottomSheet::class.java.canonicalName)
    }

    private fun checkLocation() {
        if (permissionManager.hasAllLocationPermissions()) {
            checkIfLocationEnabled()
        } else {
            permissionsLauncher?.launch(permissionManager.getAllLocationPermissions())
        }
    }

    private fun checkIfLocationEnabled() {
        if (locationManager.isLocationEnabled()) {
            openMaps()
        } else {
            activity?.let {
                locationManager.showAlertDialogButtonClicked(
                    it, locationSettingLauncher
                )
            }
        }
    }


    private val permissionsLauncher = requestAppPermissions { allIsGranted, _ ->
        if (allIsGranted) {
            checkIfLocationEnabled()
        } else {
            Toast.makeText(
                activity, getString(R.string.not_all_permissions_accepted), Toast.LENGTH_LONG
            ).show()
        }
    }

    private val locationSettingLauncher = openLocationSettingsResultLauncher {
        checkIfLocationEnabled()
    }

    override fun onCountrySelected() {
        countryCode = "+" + binding.countryCodePicker.selectedCountryCode
    }
}