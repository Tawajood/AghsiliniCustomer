package com.dotjoo.aghsilinicustomer.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dotjoo.aghsilinicustomer.R
import com.dotjoo.aghsilinicustomer.data.remote.response.Address
import com.dotjoo.aghsilinicustomer.databinding.ItemAddressBinding
import com.dotjoo.aghsilinicustomer.ui.lisener.OnAllAddressClickLisener

class AllAddressAdapter(
    val lisener: OnAllAddressClickLisener
) : RecyclerView.Adapter<AllAddressAdapter.AllAddressViewHolder>() {
    var lastDefaultPosition = -1

    var context: Context? = null
    var _binding: ItemAddressBinding? = null
    var addressList = mutableListOf<Address>()
        @SuppressLint("NotifyDataSetChanged") set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllAddressViewHolder {
        context = parent.context
        _binding = ItemAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return AllAddressViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: AllAddressViewHolder, position: Int) {

        var currentItem = addressList[position]
        holder.binding.tvAddress.setText(currentItem.address)

        if (currentItem.current == 1) {
            lastDefaultPosition = position
            context?.resources?.getColor(R.color.black)
                ?.let { holder.binding.tvAddress.setTextColor(it) }
        } else {
            context?.resources?.getColor(R.color.grey_dark)
                ?.let { holder.binding.tvAddress.setTextColor(it) }

        }
        holder.binding.cardCancel.setOnClickListener {
            lisener.onRemoveAddressClickLisener(currentItem, position)
        }
  holder.binding.root.setOnClickListener {
lisener.onDefaultAddressClickLisener(currentItem)
  }

    }

    fun deleteItem(item: Address, position: Int) {
        if (position == lastDefaultPosition) {
            lastDefaultPosition = -1
            //  PrefsHelper.clearAddress()
        }
        addressList.indexOfFirst { item.id == it.id }.takeIf { it > -1 }?.let { pos ->
            addressList.removeAt(position)

            notifyItemRemoved(pos)
            notifyItemRangeChanged(pos, addressList.size)
            notifyDataSetChanged()        }


    }


    fun selectOneItemOnly(item: Address, position: Int) {
        if (lastDefaultPosition != -1) {
            var lastDeafult = addressList[lastDefaultPosition]

            updateItem(
                Address(

                    lastDeafult.id,
                    lastDeafult.userId,
                    lastDeafult.lon,
                    lastDeafult.lat,
                    lastDeafult.address,
                    lastDeafult.current,
                    lastDeafult.createdAt,
                    lastDeafult.updatedAt,

                    false,

                    )
            )
        }
        updateItem(item)
        lastDefaultPosition = position

    }

    fun updateItem(item: Address) {
        addressList.indexOfFirst { item.id == it.id }.takeIf { it > -1 }?.let { pos ->
            addressList[pos] = item
            notifyItemChanged(pos)
        }
    }

    override fun getItemCount(): Int = addressList.size

    class AllAddressViewHolder(val binding: ItemAddressBinding) :
        RecyclerView.ViewHolder(binding.root)

}


