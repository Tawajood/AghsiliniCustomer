package com.dotjoo.aghsilinicustomer.ui.adapter.order

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.dotjoo.aghsilinicustomer.R
import com.dotjoo.aghsilinicustomer.data.remote.response.Order
import com.dotjoo.aghsilinicustomer.databinding.ItemOrderBinding
import com.dotjoo.aghsilinicustomer.ui.adapter.order.OrderType.NEW
import com.dotjoo.aghsilinicustomer.ui.lisener.OnOrderClickListener
import com.dotjoo.aghsilinicustomer.util.Constants
import com.dotjoo.aghsilinicustomer.util.ext.loadImage

object OrderType {
    val NEW = 0
    val CURRNET = 1
    val FINISHED = 2

}

class OrderAdapter(
   private val listener: OnOrderClickListener
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {
    lateinit var context: Context
    var _binding: ItemOrderBinding? = null
    var ordersList = mutableListOf<Order>()
        @SuppressLint("NotifyDataSetChanged") set(value) {
            field = value
            notifyDataSetChanged()
        }
    var type: Int = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        _binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return OrderViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        var currentItem = ordersList[position]


        holder.binding.tvId.setText("#${currentItem.id}")
        holder.binding.tvDate.setText("#${currentItem.created_at}")
        holder.binding.tvLaundryName.setText(currentItem.laundry)
        holder.binding.ivLogo.loadImage(currentItem.logo, isCircular = true)
        holder.binding.tvUrgent.isVisible=(currentItem.argent == 1)
        holder.binding.root.setOnClickListener {
            listener.onItemsClickLisener(currentItem)
        }
        when (currentItem.status) {
            Constants.NEW -> {
                holder.binding.tvStatus.setText(
                    context.resources.getString(R.string.new_)
                )
                holder.binding.tvStatus.setTextColor(context.resources.getColor(R.color.orange))
            }

            Constants.DRIVER_IN_WAY -> {
                holder.binding.tvStatus.setText(
                    context.resources.getString(R.string.driver_on_way)
                )
                holder.binding.tvStatus.setTextColor(context.resources.getColor(R.color.orange))
            }

            Constants.WAITING_DRIVER -> {
                holder.binding.tvStatus.setText(
                    context.resources.getString(R.string.waiting_driver)
                )
                holder.binding.tvStatus.setTextColor(context.resources.getColor(R.color.orange))
            }

            Constants.DRIVER_RECIVE_FROM_CUSTOMER -> {
                holder.binding.tvStatus.setText(
                    context.resources.getString(R.string.driver_recive_from_customer)
                )
                holder.binding.tvStatus.setTextColor(context.resources.getColor(R.color.orange))
            }

            Constants.LAUNDRY_RECIVE -> {
                holder.binding.tvStatus.setText(
                    context.resources.getString(R.string.laundry_recive)
                )
                holder.binding.tvStatus.setTextColor(context.resources.getColor(R.color.orange))
            }

            Constants.START_PREPARE -> {
                holder.binding.tvStatus.setText(
                    context.resources.getString(R.string.start_prepare)
                )
                holder.binding.tvStatus.setTextColor(context.resources.getColor(R.color.orange))
            }

            Constants.END_PREPARED -> {
                holder.binding.tvStatus.setText(
                    context.resources.getString(R.string.end_prepared)
                )
                holder.binding.tvStatus.setTextColor(context.resources.getColor(R.color.orange))
            }

            Constants.DRIVER_RECIVE_FROM_LAUNDRY -> {
                holder.binding.tvStatus.setText(
                    context.resources.getString(R.string.driver_recive_from_laundry)
                )
                holder.binding.tvStatus.setTextColor(context.resources.getColor(R.color.orange))
            }
            Constants.DRIVER_IN_WAY_TO_LAUNDRY -> {
                 holder  .binding.tvStatus.setText(context.resources.getString(R.string.driver_on_way))
                holder.binding.tvStatus.setTextColor(context.resources.getColor(R.color.orange))

            }

            Constants.COMPLETED -> {
                holder.binding.tvStatus.setText(
                    context.resources.getString(R.string.deliverd)
                )
                holder.binding.tvStatus.setTextColor(context.resources.getColor(R.color.blue))
            }
        }
    }


    override fun getItemCount(): Int = ordersList.size

    class OrderViewHolder(val binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root)


}


