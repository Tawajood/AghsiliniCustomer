package com.dotjoo.aghsilinicustomer.data.pagingSource

 import com.dotjoo.aghsilinicustomer.base.BasePagingDataSource
import com.dotjoo.aghsilinicustomer.base.BasePagingResponse
import com.dotjoo.aghsilinicustomer.base.ErrorResponse
import com.dotjoo.aghsilinicustomer.base.NetworkResponse
import com.dotjoo.aghsilinicustomer.base.PagingParams
 import com.dotjoo.aghsilinicustomer.base.SearchParam
 import com.dotjoo.aghsilinicustomer.data.Repository
import com.dotjoo.aghsilinicustomer.data.remote.response.Laundry


class SearchLaundryPagingSource(private val repo: Repository,
                                private val params: SearchParam
) : BasePagingDataSource<Laundry>() {


    override val queryParams: PagingParams = params

    override suspend fun execute(): NetworkResponse<BasePagingResponse<Laundry>, ErrorResponse> {
        var data =repo.searchAllLaundries(params.page  , params.name)
        return data
    }

}