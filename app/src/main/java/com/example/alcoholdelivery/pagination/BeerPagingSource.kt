package com.example.alcoholdelivery.pagination

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.alcoholdelivery.db.BeerDataBase
import com.example.alcoholdelivery.models.BeerListModel
import com.example.alcoholdelivery.models.BeerListModelItem
import com.example.alcoholdelivery.repository.BeerRepository
import com.google.gson.GsonBuilder
import retrofit2.HttpException

class BeerPagingSource(val beerRepository: BeerRepository): PagingSource<Int, BeerListModelItem>() {
    override fun getRefreshKey(state: PagingState<Int, BeerListModelItem>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BeerListModelItem> {

        return try {
            val currentPage = params.key?: 1

            val response = beerRepository.getBeers(currentPage)
            val gson = GsonBuilder().create()
//            val beerList: List<BeerListModelItem> = gson.fromJson(response.body()?.beers.toString(), Array<BeerListModelItem>::class.java).toList()
            val page = 9
            val data = response.body()!!


            Log.d("ResponseData1", data.toString())
            val responseData = mutableListOf<BeerListModelItem>()
            var i: Int = 0
            for (x in data){
                if(x!=null){
                    responseData.add(x)

                }
            }

        Log.d("ResponseData", responseData.toString())
        LoadResult.Page(
            data = responseData,
            prevKey = if(currentPage==1) null else -1,

            nextKey = if (currentPage== page) null else currentPage.plus(1)
        )

    }catch (e: HttpException){
        LoadResult.Error(e)
    }catch (e: Exception){
        LoadResult.Error(e)
    }
}
}