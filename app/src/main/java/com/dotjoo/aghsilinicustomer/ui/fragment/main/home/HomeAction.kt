package com.dotjoo.aghsilinicustomer.ui.fragment.main.home

import androidx.paging.PagingData
import com.dotjoo.aghsilinicustomer.base.Action
import com.dotjoo.aghsilinicustomer.data.remote.response.Address
import com.dotjoo.aghsilinicustomer.data.remote.response.AllLaundriesResponse
import com.dotjoo.aghsilinicustomer.data.remote.response.Laundry
import com.dotjoo.aghsilinicustomer.data.remote.response.ReviewsResponse
import com.dotjoo.aghsilinicustomer.data.remote.response.SliderResponse


sealed class HomeAction : Action {

    data class ShowLoading(val show: Boolean) : HomeAction()
    data class ShowFailureMsg(val message: String?) : HomeAction()

     data class AllLaundries(val data: PagingData<Laundry>): HomeAction()
     data class ShowSearchResult(val data: PagingData<Laundry>): HomeAction()
     data class ShowLaundryReviews(val data:ReviewsResponse): HomeAction()
     data class ShowSliderHome(val data: SliderResponse): HomeAction()
    data class  ShowNearestLaundries(val data : AllLaundriesResponse): HomeAction()
    data class  ShowCurrent(val data: ArrayList<Address>?): HomeAction()


}
