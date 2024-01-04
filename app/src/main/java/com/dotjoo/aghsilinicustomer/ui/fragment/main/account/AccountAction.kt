package com.dotjoo.aghsilinicustomer.ui.fragment.main.account

 import com.dotjoo.aghsilinicustomer.base.Action
 import com.dotjoo.aghsilinicustomer.data.remote.response.LoginResponse


sealed class AccountAction : Action {

    data class ShowLoading(val show: Boolean) : AccountAction()
    data class ShowFailureMsg(val message: String?) : AccountAction()
    data class LangUpdated(val message: String?) : AccountAction()
    data class ShowProfileUpdated(val message: String?) : AccountAction()
    data class ShowPhoneUpdated(val message: String?) : AccountAction()
    data class PasswordChanged(val message: String?) : AccountAction()
    data class ShowProfileDeleted(val message: String?) : AccountAction()

     data class ShowProfile(val data: LoginResponse): AccountAction ()


}
