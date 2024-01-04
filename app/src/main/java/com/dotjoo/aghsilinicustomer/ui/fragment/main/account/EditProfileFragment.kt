package com.dotjoo.aghsilinicustomer.ui.fragment.main.account

import android.app.Activity
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghsilinicustomer.R
import com.dotjoo.aghsilinicustomer.base.BaseFragment
import com.dotjoo.aghsilinicustomer.data.Param.UpdatePhoneParam
import com.dotjoo.aghsilinicustomer.data.Param.UpdateProfileParam
import com.dotjoo.aghsilinicustomer.data.PrefsHelper
import com.dotjoo.aghsilinicustomer.databinding.FragmentEditProfileBinding
import com.dotjoo.aghsilinicustomer.ui.activity.MainActivity
import com.dotjoo.aghsilinicustomer.ui.dialog.CheckOtpSheetFragment
import com.dotjoo.aghsilinicustomer.ui.dialog.OnPhoneCheckedWithOtp
import com.dotjoo.aghsilinicustomer.util.FileManager
import com.dotjoo.aghsilinicustomer.util.ToastUtils
import com.dotjoo.aghsilinicustomer.util.ext.hideKeyboard
import com.dotjoo.aghsilinicustomer.util.observe
import com.hbb20.CountryCodePicker
import com.theartofdev.edmodo.cropper.CropImage
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class EditProfileFragment : BaseFragment<FragmentEditProfileBinding>(),
    CountryCodePicker.OnCountryChangeListener {
    val mViewModel: AccountViewModel by viewModels()
    var countryCode = ""
    var updatPhoneFlag =-1
    override fun onFragmentReady() {

        onclick()
        mViewModel.apply {
            getProfile()
            observe(viewState) {
                handleViewState(it)
            }

        }
        binding.swiperefreshHome.setOnRefreshListener {
            mViewModel.getProfile()
            binding.swiperefreshHome.isRefreshing= false

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

            is AccountAction.ShowPhoneUpdated -> {
                action?.message?.let {
                    showToast(it)
                }
                mViewModel.getProfile()
            }
            is AccountAction.ShowProfileUpdated -> {
                action?.message?.let {
                    showToast(it)
                }
                if(updatPhoneFlag ==1) {
                    CheckOtpSheetFragment.newInstance(countryCode, binding.etPhone.text.toString(), object :OnPhoneCheckedWithOtp{
                        override fun onClick(
                            country_code: String,
                            phone: String,
                            verifed: Boolean
                        ) {
mViewModel.updatePhone(UpdatePhoneParam(country_code, phone))                        }


                    }).show(childFragmentManager, "CheckOtpSheetFragment")
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
            if( binding.etPhone.text.toString() == PrefsHelper.getUserData()?.phone) updatPhoneFlag=0
            else updatPhoneFlag=1
            mViewModel.updateProfile(
                UpdateProfileParam(
                    binding.etName.text.toString(), countryCode, binding.etPhone.text.toString(),logo
                ),updatPhoneFlag)
        }
    }

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                CropImage.activity(uri)
                    .start(requireContext(), this)
            } else {
            }
        }
var logo : File? =null
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                //  val resultUri: Uri = result.uri
                //      image = File(getRealPathFromURI(parent, resultUri))
                result.uri?.let {
                    FileManager.from(requireActivity(), it)?.let { file ->
                        logo = file

                     //   binding.iv.loadImage(file, isCircular = true)
                    }
                }
            }
        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            //  val error = result.
            showToast(data?.extras.toString())
        }
    }
    override fun onCountrySelected() {
        countryCode = "+" + binding.countryCodePicker.selectedCountryCode
    }

}