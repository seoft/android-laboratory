package kr.co.seoft.antonio_sample.data

import io.reactivex.Single

interface MockApi {
    fun loadMore(): Single<List<ResponseModel>>
}