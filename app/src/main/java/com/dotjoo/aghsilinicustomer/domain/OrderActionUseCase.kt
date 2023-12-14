package com.dotjoo.aghsilinicustomer.domain

import com.dotjoo.aghsilinicustomer.base.BaseUseCase
import com.dotjoo.aghsilinicustomer.base.DevResponse
import com.dotjoo.aghsilinicustomer.base.ErrorResponse
import com.dotjoo.aghsilinicustomer.base.NetworkResponse
import com.dotjoo.aghsilinicustomer.data.Param.CompalinParam
import com.dotjoo.aghsilinicustomer.data.Param.OrderInfoParam
import com.dotjoo.aghsilinicustomer.data.Param.ReviewParam
import com.dotjoo.aghsilinicustomer.data.Repository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped

class OrderActionUseCase @Inject constructor(private val repository: Repository) :
    BaseUseCase<DevResponse<Any>, Any>() {

    override fun executeRemote(params: Any?): Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>> {
        return if (params is ReviewParam) {
            flow {
                emit(repository.reviewLaundry(params))
            } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
        } else if (params is CompalinParam) {
            flow {
                emit(repository.complainLaundry(params))
            } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
        } else if (params is OrderInfoParam) {
            flow {
                emit(repository.cancelOrder(params))
            } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
        } else {
            flow {
                emit(null)
            } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
        }

    }
}


