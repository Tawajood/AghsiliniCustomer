package com.dotjoo.aghsilinicustomer.domain

import com.dotjoo.aghsilinicustomer.base.BaseUseCase
import com.dotjoo.aghsilinicustomer.base.DevResponse
import com.dotjoo.aghsilinicustomer.base.ErrorResponse
import com.dotjoo.aghsilinicustomer.base.NetworkResponse
import com.dotjoo.aghsilinicustomer.data.Param.ChangePasswordParams
import com.dotjoo.aghsilinicustomer.data.Param.UpdateProfileParam
import com.dotjoo.aghsilinicustomer.data.Repository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class AccountUseCase @Inject constructor(private val repository: Repository) :
    BaseUseCase<DevResponse<Any>, Any>() {

    companion object OrderTypes {
        val Profile = 1
        val Delete = 2

    }

    override fun executeRemote(params: Any?): Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>> {
        return if (params is UpdateProfileParam) {
            flow {
                emit(repository.updateProfile(params))
            } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
        } else if (params is ChangePasswordParams) {
            flow {
                emit(repository.changePass(params))
            } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
        } else if (params == Profile) {
            flow {
                emit(repository.getProfile())
            } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
        } else if (params == Delete) {
            flow {
                emit(repository.deleteAccount())
            } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
        } else {
            flow {
                emit(repository.updatlang())
            } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
        }

    }
}


