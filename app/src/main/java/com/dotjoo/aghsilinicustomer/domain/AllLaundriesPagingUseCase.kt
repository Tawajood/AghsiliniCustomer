package com.dotjoo.aghsilinicustomer.domain


import androidx.paging.PagingSource
import com.dotjoo.aghsilinicustomer.base.AllLaundriesParams
import com.dotjoo.aghsilinicustomer.base.BasePagingUseCase
import com.dotjoo.aghsilinicustomer.data.Repository
import com.dotjoo.aghsilinicustomer.data.pagingSource.LaundryPagingSource
import com.dotjoo.aghsilinicustomer.data.remote.response.Laundry

import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class AllLaundriesPagingUseCase @Inject constructor(private val repo: Repository) :
    BasePagingUseCase<Laundry, AllLaundriesParams>() {

    override fun getPagingSource(params: AllLaundriesParams?): PagingSource<Int,
            Laundry> {
       var res= LaundryPagingSource(repo, params!!)
        return res


    }
}
