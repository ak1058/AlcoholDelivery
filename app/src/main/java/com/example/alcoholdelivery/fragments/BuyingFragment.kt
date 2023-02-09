package com.example.alcoholdelivery.fragments

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.alcoholdelivery.R
import com.example.alcoholdelivery.adapters.PastOrderAdapter
import com.example.alcoholdelivery.databinding.FragmentBuyingBinding
import com.example.alcoholdelivery.db.CartDatabase
import com.example.alcoholdelivery.db.PastOrdersDataBase
import com.example.alcoholdelivery.listener.Listener
import com.example.alcoholdelivery.models.BeerListModelItem
import com.example.alcoholdelivery.sharedPreference.SavingDataPreference
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BuyingFragment : Fragment() {

    private lateinit var binding: FragmentBuyingBinding
    private lateinit var savingDataPreference: SavingDataPreference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBuyingBinding.inflate(inflater, container, false)

        savingDataPreference = SavingDataPreference(requireActivity())
        val pastOrderDatabase = PastOrdersDataBase.getDataBase(requireActivity())

        val jsonBeerData = savingDataPreference.getBeerData()
        val beerData = Gson().fromJson(jsonBeerData, BeerListModelItem::class.java)









        setOrderData(beerData)
        binding.placeOrderBtn.setOnClickListener {
            if (!(TextUtils.isEmpty(binding.txtName.text.toString())
                        && TextUtils.isEmpty(binding.txtCity.text.toString())
                        && TextUtils.isEmpty(binding.txtAddress.text.toString())
                        && TextUtils.isEmpty(binding.txtPhone.text.toString())
                        && TextUtils.isEmpty(binding.txtPincode.text.toString())
                        && TextUtils.isEmpty(binding.txtState.text.toString()))){

                Toast.makeText(requireActivity(), "Your order is Successfully Placed.", Toast.LENGTH_SHORT).show()
                CoroutineScope(Dispatchers.IO).launch {
                    pastOrderDatabase.beerDao().addOrder(beerData)
                }

                val fragmentManager = activity?.supportFragmentManager
                val fragmentTransaction = fragmentManager?.beginTransaction()
                val newFragment = BeerListFragment()
                fragmentTransaction?.replace(R.id.frame_layout, newFragment)
                fragmentManager?.popBackStack()
                fragmentTransaction?.commit()
            }else{
                Toast.makeText(requireActivity(), "Please fill all the details.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.toolBar.setNavigationOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.popBackStack()
        }


        return binding.root
    }

    private fun setOrderData(beerData: BeerListModelItem){
        binding.apply {
            beerName.text = beerData.name
            brewedDate.text = "Brewed Date: "+beerData.first_brewed
            pHValue.text = "pH: "+beerData.ph.toString()
            volumeText.text = "Volume: " + beerData.volume.value.toString()+ " ml"
            attenuationLevel.text = "Attenuation Level: " + beerData.attenuation_level.toString()

        }
    }




}