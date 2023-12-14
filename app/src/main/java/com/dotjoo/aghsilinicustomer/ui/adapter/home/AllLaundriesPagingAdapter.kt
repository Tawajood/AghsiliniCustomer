package com.dotjoo.aghsilinicustomer.ui.adapter.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dotjoo.aghsilinicustomer.R
import com.dotjoo.aghsilinicustomer.data.remote.response.Laundry
import com.dotjoo.aghsilinicustomer.databinding.ItemNearLaundryBinding
import com.dotjoo.aghsilinicustomer.ui.lisener.LaundryClickListener
import com.dotjoo.aghsilinicustomer.util.ext.loadImage
import com.dotjoo.aghsilinicustomer.util.ext.roundTo


class AllLaundriesPagingAdapter(
    var listener: LaundryClickListener,
    ) :
    PagingDataAdapter<Laundry, AllLaundriesPagingAdapter.MyViewHolder>(ORDER_DIFF_CALLBACK) {
    lateinit var context:Context

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
var currentItem= getItem(position)
        holder.binding.ivLogo.loadImage(currentItem?.logo, isCircular = true)

         holder.binding.tvLaundryName.text =   currentItem?.name
        holder.binding.tvDistence.text =  currentItem?.distance?.toDoubleOrNull()?.roundTo(2).toString() + " "+context.resources.getString(
            R.string.km)
        holder.binding.tvLaundryAddress.text = currentItem?.address
        holder.binding.tvRating.text = currentItem?.rate

        holder.binding.root.setOnClickListener {

            currentItem?.let { it1 -> listener.onInfoClickLisener(it1) }
        }
        holder.binding.tvRating.setOnClickListener {
            currentItem?.let { it1 -> listener.onRateClickLisener(it1) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
  context = parent.context
        return MyViewHolder(
            ItemNearLaundryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    class MyViewHolder(var binding: ItemNearLaundryBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private val ORDER_DIFF_CALLBACK = object : DiffUtil.ItemCallback<Laundry>() {
            override fun areItemsTheSame(oldItem: Laundry, newItem: Laundry): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Laundry, newItem: Laundry): Boolean =
                oldItem == newItem


        }
    }
}


