package com.example.alcoholdelivery.api

import com.example.alcoholdelivery.models.BeerListModel
import com.example.alcoholdelivery.models.BeerListModelItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BeerApi {

    @GET("v2/beers")
    suspend fun getBeerList(@Query("page") pageNumber: Int): Response<List<BeerListModelItem>>
}