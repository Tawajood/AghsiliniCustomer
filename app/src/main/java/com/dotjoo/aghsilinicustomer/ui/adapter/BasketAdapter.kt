package com.dotjoo.aghsilinicustomer.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dotjoo.aghsilinicustomer.R
import com.dotjoo.aghsilinicustomer.data.remote.response.Cartitems
import com.dotjoo.aghsilinicustomer.databinding.ItemBasketBinding
import com.dotjoo.aghsilinicustomer.ui.lisener.ItemsInBasketClickListener
import com.dotjoo.aghsilinicustomer.util.Constants

class BasketAdapter(
    private val listener: ItemsInBasketClickListener
) : RecyclerView.Adapter<BasketAdapter.BasketViewHolder>() {

    var _binding: ItemBasketBinding? = null
    var ordersList = mutableListOf<Cartitems>()
        @SuppressLint("NotifyDataSetChanged") set(value) {
            field = value
            notifyDataSetChanged()
        }
    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketViewHolder {
        context = parent.context
        _binding = ItemBasketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BasketViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: BasketViewHolder, position: Int) {

        var currentItem = ordersList[position]
        holder.binding.tvTitle.setText(currentItem.name)
        holder.binding.tvService.setText("(" + currentItem.service_name + ")")
        holder.binding.tvPrice.setText(currentItem.price + " " + context.getText(R.string.sr))

        holder.binding.ivCancel.setOnClickListener {
            listener.onItemsRemoveLisener(currentItem, position)
            //removeItem(position)
        }
        holder.binding.tvNumAddItional.setText(currentItem.count.toString())

        if (currentItem.count == 1) {
            holder.binding.ivMinusAdditional.setImageResource(R.drawable.ic_minus_grey)

        } else {
            holder.binding.ivMinusAdditional.setImageResource(R.drawable.ic_minus_orange)
        }

        holder.binding.ivPlusAdditional.setOnClickListener {
            currentItem.count = currentItem.count + 1
            holder.binding.tvNumAddItional.setText(currentItem.count.toString())
            holder.binding.ivPlusAdditional.setImageResource(R.drawable.ic_add_orange)
            holder.binding.ivMinusAdditional.setImageResource(R.drawable.ic_minus_orange)
            listener.onItemsClickLisener(currentItem, Constants.PLUS)
        }
        holder.binding.ivMinusAdditional.setOnClickListener {
            if (currentItem.count <= 1) {

            } else {
                currentItem.count = currentItem.count - 1
                holder.binding.tvNumAddItional.setText(currentItem.count.toString())
                if (currentItem.count == 1) holder.binding.ivMinusAdditional.setImageResource(R.drawable.ic_minus_grey)

                listener.onItemsClickLisener(currentItem, Constants.MINUS)
            }
        }
    }

    fun removeItem(position: Int) {

        try {
            ordersList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, ordersList.size)
            notifyDataSetChanged()
        } catch (e: Exception) {

        }
    }

    override fun getItemCount(): Int = ordersList.size

    class BasketViewHolder(val binding: ItemBasketBinding) : RecyclerView.ViewHolder(binding.root)

}


