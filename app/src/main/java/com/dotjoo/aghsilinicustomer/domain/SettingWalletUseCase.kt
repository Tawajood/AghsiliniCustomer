package com.dotjoo.aghsilinicustomer.domain

import com.dotjoo.aghsilinicustomer.base.BaseUseCase
import com.dotjoo.aghsilinicustomer.base.DevResponse
import com.dotjoo.aghsilinicustomer.base.ErrorResponse
import com.dotjoo.aghsilinicustomer.base.NetworkResponse
import com.dotjoo.aghsilinicustomer.data.Param.ChargeWalletParam
 import com.dotjoo.aghsilinicustomer.data.Repository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class SettingWalletUseCase @Inject constructor(private val repository: Repository):
    BaseUseCase<DevResponse<Any>, Any>() {

        override fun executeRemote(params: Any?): Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>> {
            return if (params is ChargeWalletParam) {
                flow {
                    emit(repository.chargeWallet(params ))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }
else if (params==1) {
                flow {
                    emit(repository.getNotifaction( ))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }

            else {
                flow {
                    emit(repository.getWallet())
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }

        }
    }


