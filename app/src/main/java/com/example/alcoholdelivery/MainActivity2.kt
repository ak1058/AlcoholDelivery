package com.example.alcoholdelivery

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.alcoholdelivery.databinding.ActivityMain2Binding
import com.example.alcoholdelivery.db.CartDatabase
import com.example.alcoholdelivery.fragments.AccountFragment
import com.example.alcoholdelivery.fragments.BeerListFragment
import com.example.alcoholdelivery.fragments.CartFragment
import com.example.alcoholdelivery.fragments.OrderFragment
import com.example.alcoholdelivery.utils.BeerApplicationCLass
import com.example.alcoholdelivery.viewModels.BeerViewModel
import com.example.alcoholdelivery.viewModels.BeerViewModelFactory

class MainActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityMain2Binding
    lateinit var beerViewModel: BeerViewModel
    private lateinit var cartDatabase: CartDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(BeerListFragment())

        cartDatabase = CartDatabase.getDataBase(this)


        val repository = (application as BeerApplicationCLass).beerRepository
        val factory = BeerViewModelFactory(repository)
        beerViewModel = ViewModelProvider(this, factory).get(BeerViewModel::class.java)

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.homeMenu -> replaceFragment(BeerListFragment())
                R.id.cartMenu ->{replaceFragment(CartFragment())}
                R.id.orderMenu ->{replaceFragment(OrderFragment())}
                R.id.accountMenu ->{replaceFragment(AccountFragment())}


            }
            true
        }

//        val badge = binding.bottomNavigationView.getOrCreateBadge(R.id.cartMenu)
//        badge.isVisible = true
//// An icon only badge will be displayed unless a number is set:
//        val query = SimpleSQLiteQuery("SELECT COUNT(*) FROM beerTable")
//        badge.number = cartDatabase.query(query).count.toInt()
    }


    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}