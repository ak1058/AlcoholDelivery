package com.example.alcoholdelivery.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.alcoholdelivery.R
import com.example.alcoholdelivery.databinding.FragmentBeerDetailBinding
import com.example.alcoholdelivery.db.CartDatabase
import com.example.alcoholdelivery.models.BeerListModelItem
import com.example.alcoholdelivery.sharedPreference.SavingDataPreference
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class BeerDetailFragment : Fragment() {

    private lateinit var binding: FragmentBeerDetailBinding
    private lateinit var savingDataPreference: SavingDataPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBeerDetailBinding.inflate(inflater, container,false)

        savingDataPreference = SavingDataPreference(requireActivity())
        val cartDatabase = CartDatabase.getDataBase(requireActivity())

        val jsonBeerData = savingDataPreference.getBeerData()
        val beerData = Gson().fromJson(jsonBeerData, BeerListModelItem::class.java)

        binding.toolBar.title = beerData.name


        binding.toolBar.setNavigationOnClickListener {

            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.popBackStack()
        }

        binding.addToCartBtn.setOnClickListener {
            Toast.makeText(requireActivity(), " Successfully added to Cart", Toast.LENGTH_SHORT).show()
            CoroutineScope(Dispatchers.IO).launch {
                cartDatabase.beerDao().addOrder(beerData)
            }
        }

        binding.buyNowBtn.setOnClickListener {
            val fragmentManager = activity?.supportFragmentManager
            val fragmentTransaction = fragmentManager?.beginTransaction()
            val newFragment = BuyingFragment()
            fragmentTransaction?.replace(R.id.frame_layout, newFragment)
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.commit()
        }
        setBeerData(beerData)
        return binding.root
    }

    private fun setBeerData(beerData: BeerListModelItem){
        binding.apply {
            beerName.text = beerData.name
            brewedDate.text = "Brewed Date: "+beerData.first_brewed
            pHValue.text = "pH: "+beerData.ph.toString()
            Glide.with(requireActivity())
                .load(beerData.image_url)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(beerImage)

            descText.text = beerData.description
            volumeText.text = "Volume: " + beerData.volume.value.toString()+ " ml"
            attenuationLevel.text = "Attenuation Level: " + beerData.attenuation_level.toString()
            tipsTxt.text = "Brewer Tips: " + beerData.brewers_tips
            taglineText.text = beerData.tagline
        }
    }




}