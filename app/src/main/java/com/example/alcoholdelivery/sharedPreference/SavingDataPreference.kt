package com.example.alcoholdelivery.sharedPreference

import android.content.Context
import com.example.alcoholdelivery.models.BeerListModelItem
import com.example.alcoholdelivery.utils.Constants.BEER_KEY
import com.example.alcoholdelivery.utils.Constants.SHARED_PREFERENCE_KEY
import com.google.gson.Gson

class SavingDataPreference(val context: Context) {

    val prefs = context.getSharedPreferences(SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE)


    fun saveBeerData(beerListModelItem: BeerListModelItem){
        val editor = prefs.edit()
        val parsedBeerListItem = Gson().toJson(beerListModelItem)
        editor.putString(BEER_KEY, parsedBeerListItem)
        editor.apply()
    }
    fun getBeerData(): String? {
        return prefs.getString(BEER_KEY, null)
    }
}