package com.dotjoo.aghsilinicustomer.ui.fragment.main.settingFragments

import com.dotjoo.aghsilinicustomer.base.Action
import com.dotjoo.aghsilinicustomer.data.remote.response.AboutusResponse
import com.dotjoo.aghsilinicustomer.data.remote.response.AllMessagesResponse
import com.dotjoo.aghsilinicustomer.data.remote.response.ContactResponse
import com.dotjoo.aghsilinicustomer.data.remote.response.NotifactionResponse
import com.dotjoo.aghsilinicustomer.data.remote.response.TermsResponse
import com.dotjoo.aghsilinicustomer.data.remote.response.WalletResponse

sealed class SettingAction : Action {

    data class ShowLoading(val show: Boolean) : SettingAction()
    data class ShowFailureMsg(val message: String?) : SettingAction()

    data class ShowMessageSent(val message: String?) : SettingAction()
    data class ShowContact(val data: ContactResponse?) : SettingAction()
    data class ShowAllMessage(val data: AllMessagesResponse?) : SettingAction()
    data class ShowTerms(val data: TermsResponse) : SettingAction()
    data class ShowAbout(val data: AboutusResponse) : SettingAction()
    data class ShowBalanceInWallet(val data: WalletResponse) : SettingAction()
    data class ShowNotifactions(val data: NotifactionResponse) : SettingAction()
    data class ShowWalletCharged(val message: String?) : SettingAction()
}
