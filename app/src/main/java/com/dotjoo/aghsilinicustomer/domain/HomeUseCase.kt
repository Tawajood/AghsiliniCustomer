package com.dotjoo.aghsilinicustomer.domain


import com.dotjoo.aghsilinicustomer.base.AllLaundriesParams
import com.dotjoo.aghsilinicustomer.base.BaseUseCase
import com.dotjoo.aghsilinicustomer.base.DevResponse
import com.dotjoo.aghsilinicustomer.base.ErrorResponse
import com.dotjoo.aghsilinicustomer.base.NetworkResponse
import com.dotjoo.aghsilinicustomer.data.Param.LaundryReviewParam
import com.dotjoo.aghsilinicustomer.data.Param.ReviewParam
import com.dotjoo.aghsilinicustomer.data.Repository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class HomeUseCase @Inject constructor(private val repository: Repository):
    BaseUseCase<DevResponse<Any>, Any>() {


        override fun executeRemote(params: Any?): Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>> {
            return if (params is AllLaundriesParams) {
                flow {
                    emit(repository.getNearestLaundries(params))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }
        else  if (params is LaundryReviewParam) {
                flow {
                  emit(repository.getLaundryReview(params))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }
             else {
                flow {
                    emit(repository.getSlider())
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }

        }
    }


