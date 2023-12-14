package com.dotjoo.aghsilinicustomer.ui.adapter.laundry_info

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dotjoo.aghsilinicustomer.data.remote.response.Laundry
import com.dotjoo.aghsilinicustomer.data.remote.response.ReviewItem
import com.dotjoo.aghsilinicustomer.databinding.ItemRatesBinding
import com.dotjoo.aghsilinicustomer.util.ext.roundTo

class RateLaundriesAdapter(
 ) : RecyclerView.Adapter< RateLaundriesAdapter. RateLaundriesViewHolder>() {

    var _binding: ItemRatesBinding? = null
    var ordersList = mutableListOf<ReviewItem>()
        @SuppressLint("NotifyDataSetChanged") set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  RateLaundriesViewHolder {
        _binding = ItemRatesBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return  RateLaundriesViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder:  RateLaundriesViewHolder, position: Int) {

        var currentItem = ordersList[position]
        holder.binding.tvRate.setText(currentItem.rate?.toDouble().roundTo(2).toString())
        holder.binding.tvName.setText(currentItem.user?.name.toString())
        holder.binding.tvDesc.setText(currentItem.note.toString())

    }

    override fun getItemCount(): Int = ordersList.size

    class  RateLaundriesViewHolder(val binding: ItemRatesBinding) :
        RecyclerView.ViewHolder(binding.root)

}


