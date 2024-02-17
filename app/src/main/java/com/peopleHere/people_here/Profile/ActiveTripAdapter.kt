package com.peopleHere.people_here.Profile

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.peopleHere.people_here.Data.CalendarData
import com.peopleHere.people_here.Main.MainAdapter
import com.peopleHere.people_here.R
import com.peopleHere.people_here.Remote.BringCourseResponse
import com.peopleHere.people_here.databinding.ItemCalendarDateBinding
import com.peopleHere.people_here.databinding.ItemDayTripBinding

class ActiveTripAdapter(val context : Context, val dayTripData : ArrayList<BringCourseResponse>) : RecyclerView.Adapter<ActiveTripAdapter.ViewHolder>() {
    private lateinit var itemClickListener : OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(dayTripInfo : BringCourseResponse)
    }


    inner class ViewHolder(val binding : ItemDayTripBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : BringCourseResponse) {
            if (item.status == "ACTIVE") {
                binding.tvTitle.text = item.tourName
                if (item.places.isNotEmpty() && item.places[0].placeImages.isNotEmpty()) {
                    val imageUrl = item.places[0].placeImages[0]
                    Glide.with(context)
                        .load(imageUrl)
                        .into(binding.ivTripImage)
                } else {
                    // 이미지 URL이 비어있는 경우, 기본 이미지 설정
                    binding.ivTripImage.setImageResource(R.drawable.img_example2)
                }

                binding.tvMainTourListTime.text = item.time

                if (item.places.size > 1) {
                    val addCount = item.places.size - 1
                    binding.tvMainTourListLocation.text = "${item.places[0].placeAddress} 외 ${addCount}개"
                } else if (item.places.isNotEmpty()) {
                    binding.tvMainTourListLocation.text = item.places[0].placeAddress
                } else {
                    binding.tvMainTourListLocation.text = "위치 정보 없음"
                }

                binding.btnShowDate.setOnClickListener {
                    itemClickListener.onItemClick(item)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDayTripBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dayTripData[position])
    }

    fun setOnItemClickListener(onItemClickListener : OnItemClickListener){
        itemClickListener = onItemClickListener
    }

    override fun getItemCount(): Int {
        return dayTripData.count { it.status == "ACTIVE" }
    }



}
