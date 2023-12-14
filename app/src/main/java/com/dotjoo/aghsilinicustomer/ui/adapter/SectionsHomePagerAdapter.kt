package com.dotjoo.aghsilinicustomer.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dotjoo.aghsilinicustomer.databinding.ItemSlidingImagesHomeBinding
import com.dotjoo.aghsilinicustomer.util.ext.loadImage


class SectionsHomePagerAdapter(private val context: Context, private val viewPager2: ViewPager2, var urls: MutableList<String>) :
    RecyclerView.Adapter<SectionsHomePagerAdapter.WishListViewHolder>() {
    var _binding: ItemSlidingImagesHomeBinding? = null


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WishListViewHolder {
        _binding =
            ItemSlidingImagesHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WishListViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: WishListViewHolder, position: Int) {
        var currentItem = urls[position]


          /*  Glide.with(context)
                .load(currentItem)
                .into(holder.binding.image)
*/

   holder.binding.image.loadImage(currentItem)

        //  holder.imageView.setImageResource(urls[position])
        if (position == urls.size  ) {
      viewPager2.post(runnable)
        }
    }

    private val runnable = Runnable {
        urls.clear()
        urls.addAll(urls)
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int = urls.size

    class WishListViewHolder(val binding: ItemSlidingImagesHomeBinding) :
        RecyclerView.ViewHolder(binding.root)

}


