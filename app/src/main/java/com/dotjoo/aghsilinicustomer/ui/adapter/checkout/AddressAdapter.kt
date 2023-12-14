package com.dotjoo.aghsilinicustomer.ui.adapter.checkout

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dotjoo.aghsilinicustomer.R
import com.dotjoo.aghsilinicustomer.data.remote.response.Address
  import com.dotjoo.aghsilinicustomer.databinding.ItemAddressCheckoutBinding
import com.dotjoo.aghsilinicustomer.ui.lisener.OnAddressClickLisener

class AddressAdapter(
   val lisener: OnAddressClickLisener
) : RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {


    var context: Context? = null
    var _binding: ItemAddressCheckoutBinding? = null
    var ordersList = mutableListOf<Address>()
        @SuppressLint("NotifyDataSetChanged") set(value) {
            field = value
            notifyDataSetChanged()
        }
var lastDefaultPosition =0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        context = parent.context
        _binding =
            ItemAddressCheckoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return AddressViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {

        var currentItem = ordersList[position]
        holder.binding.tvAddress.setText(currentItem.address)
        context?.let {
            holder.binding.ivLoc.setColorFilter(ContextCompat.getColor(it, R.color.blue), android.graphics.PorterDuff.Mode.MULTIPLY);
        }
        if (currentItem.choosen == true) {
          /*  context?.resources?.getColor(R.color.light_grey)
                ?.let { holder.binding.cardLocation.setBackgroundColor(it) }*/
            holder.binding.cardLocation.background= context?.resources?.getDrawable(R.drawable.bg_rounded_grey)

            context?.resources?.getColor(R.color.black)?.let {

             holder . binding . tvAddress . setTextColor (it)
        }
        } else {

            holder.binding.cardLocation.background= context?.resources?.getDrawable(R.drawable.bg_rounded_corner)
            context?.resources?.getColor(R.color.grey)?.let {

                holder . binding . tvAddress . setTextColor (it)
            }
        }
        holder.binding.root.setOnClickListener {
            if (currentItem.choosen == true) {
             } else {
                 currentItem.choosen = true
                lisener.onAddressClickLisener(currentItem)
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


    fun selectOneItemOnly(item: Address, position: Int) {
        if (lastDefaultPosition != -1) {
            var lastDeafult = ordersList[lastDefaultPosition]

            updateItem(
                Address(

                       lastDeafult.id       ,
                       lastDeafult.userId   ,
                       lastDeafult.lon      ,
                       lastDeafult.lat      ,
                       lastDeafult.address  ,
                       lastDeafult.current  ,
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
        ordersList.indexOfFirst { item.id == it.id }.takeIf { it > -1 }?.let { pos ->
            ordersList[pos] = item
            notifyItemChanged(pos)
        }
    }  
    override fun getItemCount(): Int = ordersList.size

    class AddressViewHolder(val binding: ItemAddressCheckoutBinding) :
        RecyclerView.ViewHolder(binding.root)

}


