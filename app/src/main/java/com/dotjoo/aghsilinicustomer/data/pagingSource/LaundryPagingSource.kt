package com.dotjoo.aghsilinicustomer.data.pagingSource

import com.dotjoo.aghsilinicustomer.base.AllLaundriesParams
import com.dotjoo.aghsilinicustomer.base.BasePagingDataSource
import com.dotjoo.aghsilinicustomer.base.BasePagingResponse
import com.dotjoo.aghsilinicustomer.base.ErrorResponse
import com.dotjoo.aghsilinicustomer.base.NetworkResponse
import com.dotjoo.aghsilinicustomer.base.PagingParams
import com.dotjoo.aghsilinicustomer.data.Repository
import com.dotjoo.aghsilinicustomer.data.remote.response.Laundry


class LaundryPagingSource(private val repo: Repository,
                          private val params: AllLaundriesParams
) : BasePagingDataSource<Laundry>() {


    override val queryParams: PagingParams = params

    override suspend fun execute(): NetworkResponse<BasePagingResponse<Laundry>, ErrorResponse> {
        var data =repo.getAllLaundries(params.page, params.lat, params.lng)
        return data
    }

}