package com.dotjoo.aghsilinicustomer.ui.fragment.main.settingFragments

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.dotjoo.aghsilinicustomer.R
 import com.dotjoo.aghsilinicustomer.base.BaseViewModel
import com.dotjoo.aghsilinicustomer.data.Param.ChargeWalletParam
import com.dotjoo.aghsilinicustomer.data.Param.SendMessageParam
import com.dotjoo.aghsilinicustomer.data.remote.response.*
import com.dotjoo.aghsilinicustomer.domain.SettingUseCase
import com.dotjoo.aghsilinicustomer.domain.SettingUseCase.OrderTypes.About
import com.dotjoo.aghsilinicustomer.domain.SettingUseCase.OrderTypes.Contact
import com.dotjoo.aghsilinicustomer.domain.SettingUseCase.OrderTypes.Terms_
import com.dotjoo.aghsilinicustomer.domain.SettingWalletUseCase
import com.dotjoo.aghsilinicustomer.util.NetworkConnectivity
import com.dotjoo.aghsilinicustomer.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SettingViewModel
@Inject constructor(app: Application, val useCase: SettingUseCase , val useCaseWallet: SettingWalletUseCase  ) :
    BaseViewModel<SettingAction>(app) {

    fun getContact( ) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(SettingAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope , Contact )
                { res ->
                    when (res) {
                      is Resource.Failure -> produce(SettingAction.ShowFailureMsg(res.message.toString()))
                      is Resource.Progress -> produce(SettingAction.ShowLoading(res.loading))
                      is Resource.Success -> {
                          produce(SettingAction.ShowContact(res.data?.data as ContactResponse))
                      }
                  }
              }
            }
        }
        else {
            produce(SettingAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

   fun getTerms() {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(SettingAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope ,Terms_)
                { res ->
                    when (res) {
                        is Resource.Failure -> produce(SettingAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(SettingAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(SettingAction.ShowTerms(res.data?.data as TermsResponse))
                        }
                    }
                }
            }
        }
        else {
            produce(SettingAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
 fun getAbout() {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(SettingAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope ,About
                )
                { res ->
                    when (res) {
                        is Resource.Failure -> produce(SettingAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(SettingAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(SettingAction.ShowAbout(res.data?.data as AboutusResponse))
                        }
                    }
                }
            }
        }
        else {
            produce(SettingAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
  fun getMessages( ) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(SettingAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope
                )
                { res ->
                    when (res) {
                        is Resource.Failure -> produce(SettingAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(SettingAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(SettingAction.ShowAllMessage(res.data?.data as AllMessagesResponse))
                        }
                    }
                }
            }
        }
        else {
            produce(SettingAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
   fun sendMessages(msg:String ) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(SettingAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                        viewModelScope ,SendMessageParam(msg)
                )
                { res ->
                    when (res) {
                        is Resource.Failure -> produce(SettingAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(SettingAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(SettingAction.ShowMessageSent(res.data?.message as String))
                        }
                    }
                }
            }
        }
        else {
            produce(SettingAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
  fun chargeWallet(balance :String, fort_id:String="" ) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(SettingAction.ShowLoading(true))

            viewModelScope.launch {
              useCaseWallet.invoke(
                        viewModelScope , ChargeWalletParam(balance, fort_id)
                )
                { res ->
                    when (res) {
                        is Resource.Failure -> produce(SettingAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(SettingAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(SettingAction.ShowWalletCharged(res.data?.message as String))
                        }
                    }
                }
            }
        }
        else {
            produce(SettingAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
  fun getNotifaction( ) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(SettingAction.ShowLoading(true))

            viewModelScope.launch {
              useCaseWallet.invoke(
                        viewModelScope , 1
                )
                { res ->
                    when (res) {
                        is Resource.Failure -> produce(SettingAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(SettingAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(SettingAction.ShowNotifactions(res.data?.data as NotifactionResponse))
                        }
                    }
                }
            }
        }
        else {
            produce(SettingAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
 fun getWallet(  ) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(SettingAction.ShowLoading(true))

            viewModelScope.launch {
             useCaseWallet.invoke(
                        viewModelScope
                )
                { res ->
                    when (res) {
                        is Resource.Failure -> produce(SettingAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(SettingAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(SettingAction.ShowBalanceInWallet(res.data?.data as WalletResponse))
                        }
                    }
                }
            }
        }
        else {
            produce(SettingAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

    }
