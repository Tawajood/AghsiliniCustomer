package com.dotjoo.aghsilinicustomer.util.service

 import com.dotjoo.aghsilinicustomer.base.BaseUseCase
import com.dotjoo.aghsilinicustomer.base.DevResponse
import com.dotjoo.aghsilinicustomer.base.ErrorResponse
import com.dotjoo.aghsilinicustomer.base.NetworkResponse
import com.dotjoo.aghsilinicustomer.data.Param.UpdateFcmTokenParam
import com.dotjoo.aghsilinicustomer.data.PrefsHelper
import com.dotjoo.aghsilinicustomer.data.Repository
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
 import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FcmUseCase @Inject constructor(
    val repo: Repository
):  BaseUseCase<DevResponse<Any>, Any>() {

    fun generateFcmToken(callBack: (String?) -> Unit = {}) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            PrefsHelper.setFCMToken(token)
            sendFcmTokenToServer(FcmParam(token))
        })
    }

    fun generateFcmTokenIfNotExist() {

        if (PrefsHelper.getFcmToken().isNullOrBlank()) {
            generateFcmToken()
        }
    }
    fun sendFcmTokenToServer(params: FcmParam) {
        invoke(CoroutineScope(Dispatchers.IO), params = params)
    }


    override fun executeRemote(params: Any?): Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>> {
        return flow {
            emit(repo.updateFcmToken(params!! as UpdateFcmTokenParam))
        } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
    }
    }