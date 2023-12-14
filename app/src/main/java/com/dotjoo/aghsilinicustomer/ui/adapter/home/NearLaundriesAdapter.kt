package com.dotjoo.aghsilinicustomer.ui.adapter.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.dotjoo.aghsilinicustomer.R
import com.dotjoo.aghsilinicustomer.data.remote.response.Laundry
import com.dotjoo.aghsilinicustomer.databinding.ItemNearLaundryBinding
import com.dotjoo.aghsilinicustomer.ui.adapter.home.Cardsize.DevisionCard
import com.dotjoo.aghsilinicustomer.ui.lisener.LaundryClickListener
import com.dotjoo.aghsilinicustomer.util.ext.loadImage
import com.dotjoo.aghsilinicustomer.util.ext.roundTo
import java.util.Locale

object Cardsize {
    val DevisionCard= 0
    val FullCard= 1

}
class NearLaundriesAdapter(
    private val listener: LaundryClickListener,
    var type: Int
) : RecyclerView.Adapter<NearLaundriesAdapter.NearLaundriesViewHolder>(),
    Filterable {

lateinit var context:Context
    var _binding: ItemNearLaundryBinding? = null
    var ordersListFilterd: ArrayList<Laundry>  = arrayListOf()
    var ordersList = mutableListOf<Laundry>()
        @SuppressLint("NotifyDataSetChanged") set(value) {
            field = value
            notifyDataSetChanged()
            ordersListFilterd = ordersList as ArrayList<Laundry>

        }

    init {
        ordersListFilterd = ordersList as ArrayList<Laundry>
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NearLaundriesViewHolder {
     context= parent.context
        _binding = ItemNearLaundryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//type == DevisionCard &&
        if (type == DevisionCard && ordersList.size > 1) {

            val layoutParams: ViewGroup.LayoutParams = _binding!!.root.layoutParams
            layoutParams.width = (parent.width * 0.80).toInt()
            _binding!!.root.setLayoutParams(layoutParams)
        }
        return NearLaundriesViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: NearLaundriesViewHolder, position: Int) {

        var currentItem = ordersListFilterd[position]
        holder.binding.tvDistence.text = currentItem?.distance?.toDoubleOrNull()?.roundTo(2).toString() + " "+context.resources.getString(
            R.string.km)
        holder.binding.tvLaundryName.text = currentItem?.name
        holder.binding.tvLaundryAddress.text = currentItem?.address
        holder.binding.tvRating.text = currentItem?.rate
        holder.binding.ivLogo.loadImage(currentItem?.logo, isCircular = true)

        holder.binding.root.setOnClickListener {

            currentItem?.let { it1 -> listener.onInfoClickLisener(it1) }
        }
        holder.binding.tvRating.setOnClickListener {
            currentItem?.let { it1 -> listener.onRateClickLisener(it1) }
        }


    }


    var filterr = false

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    ordersListFilterd = ordersList as ArrayList<Laundry>
                    filterr= false
                } else {
                    val resultList = ArrayList<Laundry>()
                    for (row in ordersList) {
                        if (row.name?.lowercase(Locale.ROOT)
                                ?.contains(charSearch.lowercase(Locale.ROOT)) == true
                        ) {
                            resultList.add(row)
                        }
                    }
                    filterr= true
                    ordersListFilterd = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = ordersListFilterd
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                ordersListFilterd = results?.values as ArrayList<Laundry>
                notifyDataSetChanged()
            }
        }
    }
    override fun getItemCount(): Int = ordersListFilterd.size

    class NearLaundriesViewHolder(val binding: ItemNearLaundryBinding) :
        RecyclerView.ViewHolder(binding.root)

}


