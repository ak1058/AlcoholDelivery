package com.example.alcoholdelivery.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alcoholdelivery.R
import com.example.alcoholdelivery.adapters.PastOrderAdapter
import com.example.alcoholdelivery.databinding.FragmentOrderBinding
import com.example.alcoholdelivery.db.PastOrdersDataBase
import com.example.alcoholdelivery.listener.Listener
import com.example.alcoholdelivery.models.BeerListModelItem
import com.example.alcoholdelivery.sharedPreference.SavingDataPreference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class OrderFragment : Fragment(),Listener {

    private lateinit var binding: FragmentOrderBinding
    private lateinit var savingDataPreference: SavingDataPreference
    private lateinit var pastOrderAdapter: PastOrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOrderBinding.inflate(inflater, container,false)

        savingDataPreference = SavingDataPreference(requireActivity())
        val pastOrderDatabase = PastOrdersDataBase.getDataBase(requireActivity())


        pastOrderAdapter = PastOrderAdapter(requireActivity(), this)

        CoroutineScope(Dispatchers.IO).launch{
            //getting past order list from past order database
            val pastOrderList = pastOrderDatabase.beerDao().getOrders()
            pastOrderAdapter.submitList(pastOrderList)
        }

        binding.pastOrderRecyclerView.apply {
            val linearLayoutManager = LinearLayoutManager(activity?.applicationContext)
            linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
            layoutManager =linearLayoutManager

            adapter = pastOrderAdapter

            setHasFixedSize(true)
        }


        return binding.root
    }

    override fun onItemBtnClickListener(position: Int, beerListModelItem: BeerListModelItem) {
        savingDataPreference.saveBeerData(beerListModelItem)

        // if any user click on any order he will redirected to that order detail
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()
        val newFragment = BeerDetailFragment()
        fragmentTransaction?.replace(R.id.frame_layout, newFragment)
        fragmentTransaction?.addToBackStack(null)
        fragmentTransaction?.commit()
    }

    override fun onItemDeleteClickListener(position: Int, beerListModelItem: BeerListModelItem) {
        return
    }


}