package com.dotjoo.aghsilinicustomer.ui.fragment.main.account

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.dotjoo.aghsilinicustomer.R
 import com.dotjoo.aghsilinicustomer.base.BaseViewModel
import com.dotjoo.aghsilinicustomer.data.Param.ChangePasswordParams
import com.dotjoo.aghsilinicustomer.data.Param.UpdateProfileParam
import com.dotjoo.aghsilinicustomer.data.remote.response.*
import com.dotjoo.aghsilinicustomer.domain.AccountUseCase
import com.dotjoo.aghsilinicustomer.domain.AccountUseCase.OrderTypes.Delete
import com.dotjoo.aghsilinicustomer.domain.AccountUseCase.OrderTypes.Profile
 import com.dotjoo.aghsilinicustomer.util.NetworkConnectivity
import com.dotjoo.aghsilinicustomer.util.Resource 
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AccountViewModel
@Inject constructor(app: Application, val useCase: AccountUseCase  ):
    BaseViewModel<AccountAction>(app) {
 
    fun getProfile(  ) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(AccountAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope,Profile)
                { res ->
                    when (res) {
                        is Resource.Failure -> produce(AccountAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(AccountAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(AccountAction.ShowProfile(res.data?.data as LoginResponse))
                        }
                    }
                }
            }
        }
        else {
            produce(AccountAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

    fun deleteProfile(  ) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(AccountAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope,Delete)
                { res ->
                    when (res) {
                        is Resource.Failure -> produce(AccountAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(AccountAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(AccountAction.ShowProfileDeleted(res.data?.message as String))
                        }
                    }
                }
            }
        }
        else {
            produce(AccountAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
    fun updatlang(  ) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(AccountAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope  )
                { res ->
                    when (res) {
                        is Resource.Failure -> produce(AccountAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(AccountAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(AccountAction.LangUpdated(res.data?.message as String))
                        }
                    }
                }
            }
        }
        else {
            produce(AccountAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
 
    fun updateProfile(param: UpdateProfileParam) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(AccountAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope, param)
                { res ->
                    when (res) {
                        is Resource.Failure -> produce(AccountAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(AccountAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(AccountAction.ShowProfileUpdated(res.data?.message as String))
                        }
                    }
                }
            }
        }
        else {
            produce(AccountAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

    fun isValidParamsChangePass(pass: String,    newpass: String, confirmpass: String) {
        if (pass.isNullOrBlank()) {
            produce(AccountAction.ShowFailureMsg(getString(R.string.msg_empty_password)))
            false

        }  
        else if (newpass.isNullOrBlank()) {
            produce(AccountAction.ShowFailureMsg(getString(R.string.msg_empty_new_password)))
            false

        } else if (confirmpass.isNullOrBlank()) {
            produce(AccountAction.ShowFailureMsg(getString(R.string.empty_confirm_password)))
            false

        }/* else if (confirmpass.length<8 ||newpass.length<8 ) {
        produce(AccountAction.ShowFailureMsg(getString(R.string.passmust_be_at_least_8_characters)))
        false

    }*/ else if (!confirmpass.equals(newpass)) {
            produce(AccountAction.ShowFailureMsg(getString(R.string.both_passwords_must_match)))
            false

        } else {
            changePass(ChangePasswordParams(pass, newpass, confirmpass))
        }

    }

    fun changePass(param: ChangePasswordParams) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(AccountAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope, param)
                { res ->
                    when (res) {
                        is Resource.Failure -> produce(AccountAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(AccountAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(AccountAction.PasswordChanged(res.data?.message as String))
                        }
                    }
                }
            }
        }
        else {
            produce(AccountAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
    

}
