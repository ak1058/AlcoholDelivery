package com.example.alcoholdelivery.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alcoholdelivery.MainActivity2
import com.example.alcoholdelivery.R
import com.example.alcoholdelivery.databinding.FragmentBeerListBinding
import com.example.alcoholdelivery.listener.Listener
import com.example.alcoholdelivery.models.BeerListModelItem
import com.example.alcoholdelivery.pagination.BeerPagingAdapter
import com.example.alcoholdelivery.sharedPreference.SavingDataPreference
import com.example.alcoholdelivery.viewModels.BeerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class BeerListFragment : Fragment(), Listener {
    private lateinit var binding: FragmentBeerListBinding
    private lateinit var beerViewModel: BeerViewModel
    private lateinit var beerAdapter: BeerPagingAdapter
    private lateinit var savingDataPreference: SavingDataPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBeerListBinding.inflate(inflater, container, false)

        savingDataPreference = SavingDataPreference(requireActivity())



        beerViewModel = (activity as MainActivity2).beerViewModel
        beerAdapter = BeerPagingAdapter(requireContext(), this)


        //setting recycler view
        binding.beerRecyclerView.apply {
            val linearLayoutManager = LinearLayoutManager(activity?.applicationContext)
            linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
            layoutManager =linearLayoutManager

            adapter = beerAdapter

            setHasFixedSize(true)
        }




        return binding.root
    }

    //listener that is used to handle click when someone click on an item in the recycler view
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
        return
    }



    // a broadcastreciever used for checkin internet connection regularly
    private val networkChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
            val isConnected: Boolean = activeNetwork?.isConnected == true
            if (isConnected) {
                // internet connected, it will fetch the list, otherwise
                binding.noInternetImg.visibility = View.GONE
                CoroutineScope(Dispatchers.IO).launch{
                    beerViewModel.beerList.collect {

                        beerAdapter.submitData(it)

                    }
                }

            } else {
                // internet not connected, it will just show a no internet connected image
                binding.noInternetImg.visibility = View.VISIBLE
                binding.beerRecyclerView.visibility = View.GONE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        requireContext().registerReceiver(networkChangeReceiver, filter)
    }


    override fun onPause() {
        super.onPause()
        requireContext().unregisterReceiver(networkChangeReceiver)
    }



}