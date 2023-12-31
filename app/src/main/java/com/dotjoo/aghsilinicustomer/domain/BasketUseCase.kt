package com.dotjoo.aghsilinicustomer.domain

 import com.dotjoo.aghsilinicustomer.base.BaseUseCase
import com.dotjoo.aghsilinicustomer.base.DevResponse
import com.dotjoo.aghsilinicustomer.base.ErrorResponse
import com.dotjoo.aghsilinicustomer.base.NetworkResponse
  import com.dotjoo.aghsilinicustomer.data.Param.AddToCartParam
 import com.dotjoo.aghsilinicustomer.data.Param.CreateOrderParam
  import com.dotjoo.aghsilinicustomer.data.Repository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class BasketUseCase @Inject constructor(private val repository: Repository):
    BaseUseCase<DevResponse<Any>, Any>() {

        override fun executeRemote(params: Any?): Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>> {
            return   if (params is AddToCartParam) {
                flow {
                  emit(repository.addToCart(params))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }

                 else  if (params is CreateOrderParam) {
                flow {
                  emit(repository.createOrder(params))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }
            else {
                flow {
                    emit(repository.getCart())
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }

        }
    }


