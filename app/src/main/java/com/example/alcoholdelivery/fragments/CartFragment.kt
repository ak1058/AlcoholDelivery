package com.example.alcoholdelivery.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alcoholdelivery.R
import com.example.alcoholdelivery.adapters.OrderAdapter
import com.example.alcoholdelivery.databinding.FragmentCartBinding
import com.example.alcoholdelivery.db.CartDatabase
import com.example.alcoholdelivery.models.BeerListModelItem
import com.example.alcoholdelivery.sharedPreference.SavingDataPreference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CartFragment : Fragment(), com.example.alcoholdelivery.listener.Listener {

    private lateinit var binding: FragmentCartBinding
    private lateinit var orderAdapter: OrderAdapter
    private lateinit var savingDataPreference: SavingDataPreference
    private lateinit var cartDatabase: CartDatabase


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater, container,false)
        cartDatabase = CartDatabase.getDataBase(requireActivity())
        orderAdapter = OrderAdapter(requireActivity(), this)
        savingDataPreference = SavingDataPreference(requireActivity())


        CoroutineScope(Dispatchers.IO).launch {
            val orderList = cartDatabase.beerDao().getOrders()
            orderAdapter.submitList(orderList)

        }

        binding.cartRecyclerView.apply {
            val linearLayoutManager = LinearLayoutManager(activity?.applicationContext)
            linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
            layoutManager =linearLayoutManager

            adapter = orderAdapter

            setHasFixedSize(true)
        }

        binding.toolBar.setNavigationOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.popBackStack()
        }


        return binding.root
    }

    override fun onItemBtnClickListener(position: Int, beerListModelItem: BeerListModelItem) {
        savingDataPreference.saveBeerData(beerListModelItem)

        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()
        val newFragment = BeerDetailFragment()
        fragmentTransaction?.replace(R.id.frame_layout, newFragment)
        fragmentTransaction?.addToBackStack(null)
        fragmentTransaction?.commit()
    }

    override fun onItemDeleteClickListener(position: Int, beerListModelItem: BeerListModelItem) {

        CoroutineScope(Dispatchers.IO).launch {
        cartDatabase.beerDao().deleteOrderFromCart(beerListModelItem)}

        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()
        val newFragment = CartFragment()
        fragmentTransaction?.replace(R.id.frame_layout, newFragment)
        fragmentManager?.popBackStack()
        fragmentTransaction?.commit()


    }


}