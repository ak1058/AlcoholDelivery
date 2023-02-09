package com.example.alcoholdelivery.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.alcoholdelivery.db.BeerDataBase
import com.example.alcoholdelivery.repository.BeerRepository


class BeerViewModelFactory(val beerRepository: BeerRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BeerViewModel(beerRepository) as T
    }
}