package com.dotjoo.aghsilinicustomer.domain

 import com.dotjoo.aghsilinicustomer.base.BaseUseCase
import com.dotjoo.aghsilinicustomer.base.DevResponse
import com.dotjoo.aghsilinicustomer.base.ErrorResponse
import com.dotjoo.aghsilinicustomer.base.NetworkResponse
 import com.dotjoo.aghsilinicustomer.data.Param.GetItemsInServiceParam
 import com.dotjoo.aghsilinicustomer.data.Param.IncreaseItemParam
 import com.dotjoo.aghsilinicustomer.data.Param.RemoveItemParam
 import com.dotjoo.aghsilinicustomer.data.Repository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class CreateOrderUseCase @Inject constructor(private val repository: Repository):
    BaseUseCase<DevResponse<Any>, Any>() {

        override fun executeRemote(params: Any?): Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>> {
            return if (params is IncreaseItemParam) {
               if((params as IncreaseItemParam ).plus ){
                   flow {
                       emit(repository.increaseItem(params))
                   } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>

               }  else{
                   flow {
                       emit(repository.decreaseItem(params))
                   } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>

               }
                 }

           else  if (params is RemoveItemParam) {
                flow {
                  emit(repository.removeItem(params))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }
 else  if (params is GetItemsInServiceParam) {
                flow {
                  emit(repository.getItemsInService(params))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }

            else {
                flow {
                    emit(null)
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }

        }
    }


