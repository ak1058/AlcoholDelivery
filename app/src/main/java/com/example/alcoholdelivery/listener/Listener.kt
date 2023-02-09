package com.example.alcoholdelivery.listener

import com.example.alcoholdelivery.models.BeerListModelItem

interface Listener {
    fun onItemBtnClickListener(position: Int, beerListModelItem: BeerListModelItem)


    fun onItemDeleteClickListener(position: Int, beerListModelItem: BeerListModelItem)
}