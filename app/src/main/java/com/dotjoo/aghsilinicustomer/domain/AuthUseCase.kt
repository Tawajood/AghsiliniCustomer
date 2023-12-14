package com.dotjoo.aghsilinicustomer.domain


import com.dotjoo.aghsilinicustomer.base.BaseUseCase
import com.dotjoo.aghsilinicustomer.base.DevResponse
import com.dotjoo.aghsilinicustomer.base.ErrorResponse
import com.dotjoo.aghsilinicustomer.base.NetworkResponse
import com.dotjoo.aghsilinicustomer.data.Param.CheckOtpWithPhoneParam
import com.dotjoo.aghsilinicustomer.data.Param.CheckPhoneParam
import com.dotjoo.aghsilinicustomer.data.Param.LoginParams
import com.dotjoo.aghsilinicustomer.data.Param.RegisterParams
import com.dotjoo.aghsilinicustomer.data.Param.ResetPasswordParams
import com.dotjoo.aghsilinicustomer.data.Repository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class AuthUseCase @Inject constructor(private val repository: Repository):
    BaseUseCase<DevResponse<Any>, Any>() {


        override fun executeRemote(params: Any?): Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>> {
            return if (params is LoginParams) {
                flow {
                    emit(repository.login(params))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }
           else  if (params is RegisterParams) {
                flow {
                  emit(repository.register(params))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }    else  if (params is ResetPasswordParams) {
                flow {
                  emit(repository.resetpassword(params))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }
            else  if (params is CheckPhoneParam) {

                    flow {
                        emit(repository.checkPhone(params))
                    } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>

            }
            else  if (params is CheckOtpWithPhoneParam) {
                flow {
                    emit(repository.checkOTpWIthPhone(params))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }
            else {
                flow {
                    emit(repository.logout())
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }

        }
    }


