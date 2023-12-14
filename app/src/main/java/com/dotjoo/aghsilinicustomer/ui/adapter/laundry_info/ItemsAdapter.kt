package com.dotjoo.aghsilinicustomer.ui.adapter.laundry_info

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dotjoo.aghsilinicustomer.R
 import com.dotjoo.aghsilinicustomer.data.remote.response.ItemsInService
import com.dotjoo.aghsilinicustomer.data.remote.response.ServiceInLaundry
import com.dotjoo.aghsilinicustomer.databinding.ItemItemsBinding
import com.dotjoo.aghsilinicustomer.ui.lisener.ItemsInLaundryClickListener
import com.dotjoo.aghsilinicustomer.util.Constants

class ItemsAdapter(
    private val listener: ItemsInLaundryClickListener
) : RecyclerView.Adapter<ItemsAdapter.ItemsViewHolder>() {

    var _binding: ItemItemsBinding? = null
    var ordersList = mutableListOf<ItemsInService>()
        @SuppressLint("NotifyDataSetChanged") set(value) {
            field = value
            notifyDataSetChanged()
        }
var context :Context ? = null
    var urgent: Boolean = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
       context = parent.context
        _binding = ItemItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemsViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {

        var currentItem = ordersList[position]
holder.binding.tvNumAddItional.setText(currentItem.count.toString())
holder.binding.tvTitle.setText(currentItem.name.toString())
        if (urgent) holder.binding.tvPrice.setText(
            currentItem.argentPrice.toString() + context?.getString(
                R.string.sr
            )
        )
        else holder.binding.tvPrice.setText(currentItem.price.toString() + context?.getString(R.string.sr))     /*   holder.binding.root.setOnClickListener {
            //  listener.onActiveOrderClickListener(currentItem)
        }*/
        holder.binding.ivPlusAdditional.setOnClickListener {
            currentItem.count = currentItem.count?.plus(1)
            holder.binding.tvNumAddItional.setText(currentItem.count.toString())
            holder.binding.ivPlusAdditional.setImageResource(R.drawable.ic_add_orange)
            holder.binding.ivMinusAdditional.setImageResource(R.drawable.ic_minus_orange)
            listener.onItemsClickLisener(currentItem,Constants.PLUS)
        }
        holder.binding.ivMinusAdditional.setOnClickListener {
            if (currentItem.count!! < 1) {

            } else {
                currentItem.count = currentItem.count?.minus(1)
                holder.binding.tvNumAddItional.setText(currentItem.count.toString())
                if (currentItem.count == 0) holder.binding.ivMinusAdditional.setImageResource(R.drawable.ic_minus_grey)

                listener.onItemsClickLisener(currentItem, Constants.MINUS)
            }
        }
    }
    fun updateItem(item: ItemsInService) {
        ordersList.indexOfFirst { item.id == it.id }.takeIf { it > -1 }?.let { pos ->
            ordersList[pos] = item
            notifyItemChanged(pos)
        }
    }
    override fun getItemCount(): Int = ordersList.size

    class ItemsViewHolder(val binding: ItemItemsBinding) : RecyclerView.ViewHolder(binding.root)

}


