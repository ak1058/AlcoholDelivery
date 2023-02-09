package com.example.alcoholdelivery.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "beerTable")
data class BeerListModelItem(
    @PrimaryKey(autoGenerate = true)
    val beerId: Int,
    val abv: Double,
    val attenuation_level: Float,
    val brewers_tips: String,
    val description: String,
    val ebc: Float,
    val first_brewed: String,
    val ibu: Float,
    val id: Float,
    val image_url: String,
    val name: String,
    val ph: Double,
    val srm: Float,
    val tagline: String,
    val target_fg: Float,
    val target_og: Float,
    val volume: Volume
)