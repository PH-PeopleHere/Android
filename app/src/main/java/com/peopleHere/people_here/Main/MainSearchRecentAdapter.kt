package com.peopleHere.people_here.Main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.peopleHere.people_here.Data.MainSearchData
import com.peopleHere.people_here.databinding.ItemMainSearchBinding

class MainSearchRecentAdapter(var mainSearchData : ArrayList<MainSearchData>) : RecyclerView.Adapter<MainSearchRecentAdapter.ViewHolder>() {

        inner class ViewHolder(val binding: ItemMainSearchBinding) :
            RecyclerView.ViewHolder(binding.root) {

            fun bind(searchRecentInfo: MainSearchData) {
                binding.ivMainSearchRecent.setImageResource(searchRecentInfo.searchImage)
                binding.tvMainSearchRecentRegion.text = searchRecentInfo.searchRegion
                binding.tvMainSearchRecentPlace.text = searchRecentInfo.searchPlace
            }
        }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MainSearchRecentAdapter.ViewHolder {
        val binding = ItemMainSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainSearchRecentAdapter.ViewHolder, position: Int) {
        holder.bind(mainSearchData[position])
    }

    override fun getItemCount(): Int = mainSearchData.size

    fun updateData(newData: ArrayList<MainSearchData>) {
        mainSearchData = newData
        notifyDataSetChanged()
    }
}