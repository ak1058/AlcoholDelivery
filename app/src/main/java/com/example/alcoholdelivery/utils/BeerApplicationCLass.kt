package com.example.alcoholdelivery.utils

import android.app.Application
import com.example.alcoholdelivery.db.BeerDataBase
import com.example.alcoholdelivery.db.CartDatabase
import com.example.alcoholdelivery.db.PastOrdersDataBase
import com.example.alcoholdelivery.repository.BeerRepository

class BeerApplicationCLass: Application() {

    lateinit var beerRepository: BeerRepository
    override fun onCreate() {
        super.onCreate()
        initialize()
    }
    private fun initialize(){
        val beerDatabase = BeerDataBase.getDataBase(applicationContext)
        beerRepository = BeerRepository(beerDatabase)


        val pastOrdersDataBase = PastOrdersDataBase.getDataBase(applicationContext)
        val cartDatabase = CartDatabase.getDataBase(applicationContext)

    }
}