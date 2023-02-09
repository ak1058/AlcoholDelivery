package com.example.alcoholdelivery.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.alcoholdelivery.models.BeerListModelItem
import com.example.alcoholdelivery.utils.RequestConverters

@Database(entities = [BeerListModelItem::class], version = 1)
@TypeConverters(RequestConverters::class)
abstract class BeerDataBase: RoomDatabase() {

    abstract fun beerDao(): BeerDao

    companion object{
        @Volatile
        private var INSTANCE: BeerDataBase? =null

        fun getDataBase(context: Context): BeerDataBase {
            if (INSTANCE == null){
                synchronized(this){
                    if (INSTANCE == null){
                        INSTANCE = Room.databaseBuilder(context,
                            BeerDataBase::class.java,
                            "beerDb")
                            .build()
                    }
                }
            }
            return INSTANCE!!
        }
    }

}