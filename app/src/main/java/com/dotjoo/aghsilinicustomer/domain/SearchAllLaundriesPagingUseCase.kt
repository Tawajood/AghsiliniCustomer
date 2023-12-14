package com.dotjoo.aghsilinicustomer.domain

import androidx.paging.PagingSource
 import com.dotjoo.aghsilinicustomer.base.BasePagingUseCase
import com.dotjoo.aghsilinicustomer.base.PagingParams
import com.dotjoo.aghsilinicustomer.base.SearchParam
import com.dotjoo.aghsilinicustomer.data.Repository
 import com.dotjoo.aghsilinicustomer.data.pagingSource.SearchLaundryPagingSource
import com.dotjoo.aghsilinicustomer.data.remote.response.Laundry
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class SearchAllLaundriesPagingUseCase @Inject constructor(private val repo: Repository) :
    BasePagingUseCase<Laundry, SearchParam>() {

    override fun getPagingSource(params: SearchParam?): PagingSource<Int,
            Laundry> {
       var res= SearchLaundryPagingSource(repo, params!!)
        return res


    }
}
