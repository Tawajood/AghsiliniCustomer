package com.dotjoo.aghsilinicustomer.ui.adapter.laundry_info

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dotjoo.aghsilinicustomer.R
import com.dotjoo.aghsilinicustomer.data.remote.response.ServiceInLaundry
import com.dotjoo.aghsilinicustomer.databinding.ItemServiceBinding
import com.dotjoo.aghsilinicustomer.ui.lisener.ServiceClickListener

class ServiceAdapter(
    private val listener: ServiceClickListener
) : RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {
    var context: Context? = null
    var _binding: ItemServiceBinding? = null
    var ordersList = mutableListOf<ServiceInLaundry>()
        @SuppressLint("NotifyDataSetChanged") set(value) {
            field = value
            notifyDataSetChanged()
        }
    var lastDefaultPosition = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        context = parent.context
        _binding = ItemServiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ServiceViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {

        var currentItem = ordersList[position]
         holder.binding.tvTitle.setText(currentItem.name)
      //  holder.binding.tvTitle.setOnClickListener {}
        if (currentItem.choosen == true) {
            holder.binding.ivLogo.setAlpha(1f)
            context?.getColor(R.color.white)?.let { holder.binding.tvTitle.setTextColor(it) }
        } else {
            holder.binding.ivLogo.setAlpha(.5f)
            holder.binding.tvTitle.setAlpha(.5f)
            //context?.getColor(R.color.lightwhite)?.let { holder.binding.tvTitle.setTextColor(it) }

        }
        holder.binding.root.setOnClickListener {
            if (currentItem.choosen == true) {
Log.i("addesalready", currentItem.name.toString())
            } else {
                Log.i("addes", currentItem.name.toString())
                currentItem.choosen = true
                listener.onServiceClickLisener(currentItem)
                selectOneItemOnly(currentItem, position)
            }
        }

    }

    fun deleteItem(position: Int) {
        if (position == lastDefaultPosition) {
            lastDefaultPosition = -1
            //  PrefsHelper.clearAddress()
        }
        // addresesList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, ordersList.size)
        notifyDataSetChanged()
    }


    fun selectOneItemOnly(item: ServiceInLaundry, position: Int) {
        if (lastDefaultPosition != -1) {
            var lastDeafult = ordersList[lastDefaultPosition]

            updateItem(
                ServiceInLaundry(
                    lastDeafult.itemId,
                    lastDeafult.name,
                     lastDeafult.items   ,
                    false,

                    )
            )
        }
        updateItem(item)
        lastDefaultPosition = position

    }

    fun updateItem(item: ServiceInLaundry) {
        ordersList.indexOfFirst { item.itemId == it.itemId }.takeIf { it > -1 }?.let { pos ->
            ordersList[pos] = item
            notifyItemChanged(pos)
        }
    }

    override fun getItemCount(): Int = ordersList.size

    class ServiceViewHolder(val binding: ItemServiceBinding) : RecyclerView.ViewHolder(binding.root)

}


