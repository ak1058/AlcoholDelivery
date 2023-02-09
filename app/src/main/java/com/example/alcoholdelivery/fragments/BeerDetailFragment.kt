package com.example.alcoholdelivery.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
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

        //initialising the shared prefernce and database class
        savingDataPreference = SavingDataPreference(requireActivity())
        val cartDatabase = CartDatabase.getDataBase(requireActivity())

        // getting the json data from the sharedpreference of the beer item which is clicked
        val jsonBeerData = savingDataPreference.getBeerData()
        //parsing it and getting the beerdata which is clicked
        val beerData = Gson().fromJson(jsonBeerData, BeerListModelItem::class.java)

        //setting toolbar title the name of beer we get from the shared preference
        binding.toolBar.title = beerData.name


        // setting back function on clicking back btn of toolbar
        binding.toolBar.setNavigationOnClickListener {

            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.popBackStack()
        }

        //setting add to cart btn functionality
        binding.addToCartBtn.setOnClickListener {
            Toast.makeText(requireActivity(), " Successfully added to Cart", Toast.LENGTH_SHORT).show()


            CoroutineScope(Dispatchers.IO).launch {
                //adding beer item to the cart database
                cartDatabase.beerDao().addOrder(beerData)
            }
        }

        //setting buy now btn functionality
        binding.buyNowBtn.setOnClickListener {
            //redirecting it to buying Fragment
            val fragmentManager = activity?.supportFragmentManager
            val fragmentTransaction = fragmentManager?.beginTransaction()
            val newFragment = BuyingFragment()
            fragmentTransaction?.replace(R.id.frame_layout, newFragment)
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.commit()
        }

        // calling setBeer data function that defined below to set all the textview and imageview their respective details of beer
        setBeerData(beerData)
        return binding.root
    }


    // setBeerdata function is used to set all the details to the views related to particular beer
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