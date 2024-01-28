package com.example.people_here.TourContents

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.people_here.databinding.ItemTourContentsImageBinding

class TourContentsImageAdapter(val context : Context, val imgList : MutableList<String>) : RecyclerView.Adapter<TourContentsImageAdapter.ViewHolder>() {
    inner class ViewHolder(val binding : ItemTourContentsImageBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(imgUrl : String){
            Glide.with(context)
                .load(imgUrl)
                .into(binding.itemIv)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTourContentsImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = imgList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(imgList[position])
    }
}