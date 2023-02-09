package com.example.alcoholdelivery.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.alcoholdelivery.R
import com.example.alcoholdelivery.databinding.FragmentBuyingBinding
import com.example.alcoholdelivery.db.PastOrdersDataBase
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

        //getting beer data stored in sharedprefernce
        val jsonBeerData = savingDataPreference.getBeerData()
        val beerData = Gson().fromJson(jsonBeerData, BeerListModelItem::class.java)


        setOrderData(beerData)


        binding.placeOrderBtn.setOnClickListener {

            // checking all the input edit text field are empty or not
            if (!(TextUtils.isEmpty(binding.txtName.text.toString())
                        && TextUtils.isEmpty(binding.txtCity.text.toString())
                        && TextUtils.isEmpty(binding.txtAddress.text.toString())
                        && TextUtils.isEmpty(binding.txtPhone.text.toString())
                        && TextUtils.isEmpty(binding.txtPincode.text.toString())
                        && TextUtils.isEmpty(binding.txtState.text.toString()))){

                Toast.makeText(requireActivity(), "Your order is Successfully Placed.", Toast.LENGTH_SHORT).show()
                CoroutineScope(Dispatchers.IO).launch {
                    //adding the orders to the past order database after successfully placing the order
                    pastOrderDatabase.beerDao().addOrder(beerData)
                }

                // after successfully placing order redirecting it to Home tab i.e, BeerList Fragment
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

        //for back pressing
        binding.toolBar.setNavigationOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.popBackStack()
        }


        return binding.root
    }

    //for setting the beer data to all the views
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