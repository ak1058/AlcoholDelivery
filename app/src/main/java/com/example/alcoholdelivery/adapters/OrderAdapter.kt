package com.example.alcoholdelivery.adapters

import android.content.Context
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.alcoholdelivery.databinding.ItemBeerBinding
import com.example.alcoholdelivery.listener.Listener
import com.example.alcoholdelivery.models.BeerListModelItem


class OrderAdapter(val context: Context, val listener: Listener): ListAdapter<BeerListModelItem, OrderAdapter.MyViewHolder >(diffUtil()) {

    inner class MyViewHolder(val binding: ItemBeerBinding): RecyclerView.ViewHolder(binding.root)

    class diffUtil: DiffUtil.ItemCallback<BeerListModelItem>(){
        override fun areItemsTheSame(oldItem: BeerListModelItem, newItem: BeerListModelItem): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: BeerListModelItem, newItem: BeerListModelItem): Boolean {
            return oldItem==newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ItemBeerBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.binding.apply {

                beerName.text = currentItem.name
                brewedDate.text = "Brewed Date: " + currentItem.first_brewed
                pHValue.text = "pH: " + currentItem.ph.toString()
                Glide.with(context)
                    .load(currentItem.image_url)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(beerImage)
                buyBtn.setOnClickListener {
                    listener.onItemBtnClickListener(position, currentItem)
                }
                deleteBtn.visibility = View.VISIBLE
                deleteBtn.setOnClickListener {
                    listener.onItemDeleteClickListener(position, currentItem)
                }




                when (context?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_YES -> {
                        beerImage.setBackgroundResource(com.example.alcoholdelivery.R.drawable.image_border_dark)
                    }
                    Configuration.UI_MODE_NIGHT_NO -> {
                        beerImage.setBackgroundResource(com.example.alcoholdelivery.R.drawable.image_border)
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        beerImage.setBackgroundResource(com.example.alcoholdelivery.R.drawable.image_border)
                    }
                }


            }
        }


    }
}
