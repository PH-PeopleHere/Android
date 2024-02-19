package com.peopleHere.people_here.Profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.peopleHere.people_here.Data.ProfileInfo
import com.peopleHere.people_here.databinding.ItemProfileInfoBinding

class ProfileInfoAdapter(val wordList: ArrayList<ProfileInfo>) : RecyclerView.Adapter<ProfileInfoAdapter.ViewHolder>(){


    inner class ViewHolder(val binding: ItemProfileInfoBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(myProfileType : ProfileInfo){
            binding.tvProfileInfoType.text = myProfileType.category
            binding.tvProfileInfoContent.text = myProfileType.details
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProfileInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int  = wordList.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
     holder.bind(wordList[position])
    }
}