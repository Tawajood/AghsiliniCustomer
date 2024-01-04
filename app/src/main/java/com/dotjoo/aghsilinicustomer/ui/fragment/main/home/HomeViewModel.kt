package com.dotjoo.aghsilinicustomer.ui.fragment.main.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.dotjoo.aghsilinicustomer.R
import com.dotjoo.aghsilinicustomer.base.AllLaundriesParams
import com.dotjoo.aghsilinicustomer.base.BaseViewModel
import com.dotjoo.aghsilinicustomer.base.SearchParam
import com.dotjoo.aghsilinicustomer.data.Param.LaundryReviewParam
import com.dotjoo.aghsilinicustomer.data.remote.response.*
import com.dotjoo.aghsilinicustomer.domain.AddressUseCase
import com.dotjoo.aghsilinicustomer.domain.AllLaundriesPagingUseCase
 import com.dotjoo.aghsilinicustomer.domain.HomeUseCase
import com.dotjoo.aghsilinicustomer.domain.SearchAllLaundriesPagingUseCase
import com.dotjoo.aghsilinicustomer.util.NetworkConnectivity
import com.dotjoo.aghsilinicustomer.util.Resource
 import com.dotjoo.aghsilinicustomer.util.service.UpdateFcmUseCase

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel
@Inject constructor(app: Application, val useCase: HomeUseCase,  val useCaseAddress: AddressUseCase, val useCaseAllLaundriesPaging: AllLaundriesPagingUseCase, val useCaseSearch: SearchAllLaundriesPagingUseCase,val useCaseFcm: UpdateFcmUseCase ) :
    BaseViewModel<HomeAction>(app) {

    fun getNearestLaundries( lat:String , long: String) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(HomeAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope,AllLaundriesParams(lat , long ))
                { res ->
                    when (res) {
                        is Resource.Failure -> produce(HomeAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(HomeAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(HomeAction.ShowNearestLaundries(res.data?.data as AllLaundriesResponse))
                        }
                    }
                }
            }
        }
        else {
            produce(HomeAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
    fun getSlider(  ) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(HomeAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope )
                { res ->
                    when (res) {
                        is Resource.Failure -> produce(HomeAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(HomeAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(HomeAction.ShowSliderHome(res.data?.data as SliderResponse))
                        }
                    }
                }
            }
        }
        else {
            produce(HomeAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

    fun getAllAddresses(    ) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(HomeAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCaseAddress.invoke(
                    viewModelScope
                )
                { res ->
                    when (res) {
                        is Resource.Failure -> produce(HomeAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(HomeAction.ShowLoading(res.loading))
                        is Resource.Success -> {

                            produce(HomeAction.ShowCurrent( (  res.data?.data as AllAddressResponse). address))
                        }
                    }
                }
            }
        }
        else {
            produce(HomeAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
    fun getLaundryReview( laundryId:String) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(HomeAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope, LaundryReviewParam(laundryId)
                )
                { res ->
                    when (res) {
                        is Resource.Failure -> produce(HomeAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(HomeAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(HomeAction.ShowLaundryReviews(res.data?.data as ReviewsResponse))
                        }
                    }
                }
            }
        }
        else {
            produce(HomeAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }


    fun getAllLaundries(lat:String , lang: String)  {

        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(HomeAction.ShowLoading(true))

            useCaseAllLaundriesPaging.invoke(
                viewModelScope, AllLaundriesParams(lat, lang)
            ) { data ->
                viewModelScope.launch {
                    produce(HomeAction.AllLaundries(data))
                }
            }
        } else {
            produce(HomeAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

    fun searchLaundry(name:String )  {

        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(HomeAction.ShowLoading(true))

            useCaseSearch.invoke(
                viewModelScope, SearchParam( name)
            ) { data ->
                viewModelScope.launch {
                    produce(HomeAction.ShowSearchResult(data))
                }
            }
        } else {
            produce(HomeAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
    fun updateToken(    ) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            viewModelScope.launch {
                try {
                    useCaseFcm.invoke()
                } catch (e: Exception) {
                    Log.i("updateFcm", "updateFcm: ${e.message}")
                }
            }

        }
    }}
