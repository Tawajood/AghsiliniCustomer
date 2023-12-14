package com.dotjoo.aghsilinicustomer.ui.fragment.auth.walkThrough

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


class SectionsPagerAdapter(fragmentActivity: WalkThroughFragment) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return FirstFragment(0)
            1 -> return FirstFragment(1)
         }
        return FirstFragment(-1)
    }
}