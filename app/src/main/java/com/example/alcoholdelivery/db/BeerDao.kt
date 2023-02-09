package com.example.alcoholdelivery.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.alcoholdelivery.models.BeerListModelItem

@Dao

interface BeerDao {

    @Insert
    suspend fun addBeers(beers: List<BeerListModelItem>)

    @Query(value = "SELECT * FROM beerTable")
    suspend fun getBeers(): List<BeerListModelItem>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addOrder(order: BeerListModelItem)

    @Query(value = "SELECT * FROM beerTable")
    suspend fun getOrders(): List<BeerListModelItem>

    @Delete
    suspend fun deleteOrderFromCart(order: BeerListModelItem)



}