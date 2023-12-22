package com.example.people_here.Search

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabLayoutVPAdapter(fragment : MainFragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> MainTourListFragment()
            else -> MainTourCourseFragment()
        }
    }
}