package com.example.alcoholdelivery.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.alcoholdelivery.db.BeerDataBase
import com.example.alcoholdelivery.pagination.BeerPagingSource
import com.example.alcoholdelivery.repository.BeerRepository

class BeerViewModel(val beerRepository: BeerRepository): ViewModel() {

    val beerList = Pager(PagingConfig(25)){
        BeerPagingSource(beerRepository)
    }.flow.cachedIn(viewModelScope)


}