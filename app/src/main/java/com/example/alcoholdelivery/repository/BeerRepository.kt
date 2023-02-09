package com.example.alcoholdelivery.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.alcoholdelivery.api.RetrofitHelper
import com.example.alcoholdelivery.db.BeerDataBase
import com.example.alcoholdelivery.models.BeerListModel
import com.example.alcoholdelivery.models.BeerListModelItem
import com.example.alcoholdelivery.utils.NetworkResult
import com.google.gson.GsonBuilder
import retrofit2.Response

    class BeerRepository(val beerDataBase: BeerDataBase) {

        private val beerMutableLiveData = MutableLiveData<NetworkResult<List<BeerListModelItem>>>()
        val beerLiveData: LiveData<NetworkResult<List<BeerListModelItem>>>
            get() = beerMutableLiveData


        suspend fun getBeers(pageNumber: Int): Response<List<BeerListModelItem>>{
            beerMutableLiveData.postValue(NetworkResult.Loading())
            val response = RetrofitHelper.beerApi.getBeerList(pageNumber)
            Log.d("ggg", response.body().toString())
            beerDataBase.beerDao().addBeers(response.body()!!)
            handleResponse(response)
            return response
        }

        private fun handleResponse(response: Response<List<BeerListModelItem>>) {

            if (response.isSuccessful && response.body()!=null){

                beerMutableLiveData.postValue(NetworkResult.Success(response.body()!!))
            }else if (response.errorBody()!=null){
                beerMutableLiveData.postValue(NetworkResult.Error(response.errorBody().toString()))
            }else{
                beerMutableLiveData.postValue(NetworkResult.Error("Something Error occurred"))
            }
        }
    }