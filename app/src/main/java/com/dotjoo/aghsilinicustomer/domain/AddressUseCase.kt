package com.dotjoo.aghsilinicustomer.domain

 import com.dotjoo.aghsilinicustomer.base.BaseUseCase
import com.dotjoo.aghsilinicustomer.base.DevResponse
import com.dotjoo.aghsilinicustomer.base.ErrorResponse
import com.dotjoo.aghsilinicustomer.base.NetworkResponse
 import com.dotjoo.aghsilinicustomer.data.Param.AddAddressParam
  import com.dotjoo.aghsilinicustomer.data.Param.ChangeCurrentAddressParam
  import com.dotjoo.aghsilinicustomer.data.Param.DeleteAddressParam
 import com.dotjoo.aghsilinicustomer.data.Repository
  import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class AddressUseCase @Inject constructor(private val repository: Repository):
    BaseUseCase<DevResponse<Any>, Any>() {


        override fun executeRemote(params: Any?): Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>> {
            return     if (params is ChangeCurrentAddressParam) {
                flow {
                    emit(repository.getAllAddresses( ))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }
            else  if (params is AddAddressParam) {
                flow {
                  emit(repository.addAddress(params))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }     else  if (params is DeleteAddressParam) {
                flow {
                  emit(repository.deleteAddress(params))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }
            else {
                flow {
                    emit(repository.getAllAddresses())
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }

        }
    }


