package com.dotjoo.aghsilinicustomer.domain


import com.dotjoo.aghsilinicustomer.base.BaseUseCase
import com.dotjoo.aghsilinicustomer.base.DevResponse
import com.dotjoo.aghsilinicustomer.base.ErrorResponse
import com.dotjoo.aghsilinicustomer.base.NetworkResponse
import com.dotjoo.aghsilinicustomer.data.Param.SendMessageParam
import com.dotjoo.aghsilinicustomer.data.Repository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class SettingUseCase @Inject constructor(private val repository: Repository):
    BaseUseCase<DevResponse<Any>, Any>() {

    companion object OrderTypes{
        val Contact =1
        val Terms_ =2
        val About =3

    }
        override fun executeRemote(params: Any?): Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>> {
            return if (params == Contact) {
                flow {
                    emit(repository.getContact( ))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }
           else  if (params == About) {
                flow {
                  emit(repository.getAbout())
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }    else  if (params == Terms_) {
                flow {
                  emit(repository.getTerms_condition())
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }
            else  if (params is SendMessageParam) {

                    flow {
                        emit(repository.sendMessage(params))
                    } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>

            }

            else {
                flow {
                    emit(repository.getMessages())
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }

        }
    }


