package com.peopleHere.people_here.Main

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.peopleHere.people_here.Data.MainData
import com.peopleHere.people_here.R
import com.peopleHere.people_here.databinding.ItemMainTourListBinding
import com.peopleHere.people_here.Data.MainCourseData

class MainAdapter(val mainData : ArrayList<MainData>) : RecyclerView.Adapter<MainAdapter.ViewHolder>(){
    private lateinit var itemClickListener: OnItemClickListener
    private lateinit var heartClickListener : OnHeartClickListener
    private var booleanHeart : Boolean = false

    interface OnItemClickListener{
        fun onItemClick(tourListInfo: MainData)
    }

    interface OnHeartClickListener{
        fun onHeartClick(position : Int)
    }

    inner class ViewHolder(val binding : ItemMainTourListBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(tourListInfo : MainData){
            binding.tvMainTourListTitle.text = tourListInfo.tourName

            val hours = tourListInfo.time / 60
            val minutes = tourListInfo.time % 60
            binding.tvMainTourListTime.text = if (minutes > 0) {
                "${hours}시간 ${minutes}분"
            } else {
                "${hours}시간"
            }
            binding.clItemMainTourList.setOnClickListener {
                itemClickListener.onItemClick(tourListInfo)
            }
            binding.tvMainTourListLocation.text = tourListInfo.time.toString()

            binding.icMainTourHeart.setImageResource(initHeartImage(tourListInfo.wished))
            binding.icMainTourHeart.setOnClickListener {
                heartClickListener.onHeartClick(adapterPosition)
            }

            if (tourListInfo.places.size > 1) {
                val addCount = tourListInfo.places.size - 1
                binding.tvMainTourListLocation.text = "${tourListInfo.places[0].placeAddress} 외 ${addCount}개"
            } else if (tourListInfo.places.isNotEmpty()) {
                binding.tvMainTourListLocation.text = tourListInfo.places[0].placeAddress
            } else {
                binding.tvMainTourListLocation.text = "위치 정보 없음"
            }

            // 내부 RecyclerView 초기화 및 어댑터 설정
            val innerAdapter = MainCourseAdapter(tourListInfo.places, tourListInfo)
            binding.rvMainTourListCourse.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
            binding.rvMainTourListCourse.adapter = innerAdapter

            val innerAdapter2 = MainCategoryAdapter(tourListInfo.categoryNames)
            binding.rvMainTourListCategory.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
            binding.rvMainTourListCategory.adapter = innerAdapter2
        }
    }

    private fun initHeartImage(heartInfo: Boolean) : Int {
        booleanHeart = heartInfo
        val heartIconResId = if (booleanHeart) {
            R.drawable.ic_main_filled_heart
        } else {
            R.drawable.ic_main_empty_heart
        }

        return heartIconResId
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MainAdapter.ViewHolder {
        val binding = ItemMainTourListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainAdapter.ViewHolder, position: Int) {
        holder.bind(mainData[position])
    }

    fun setOnItemClickListener(onItemClickListener : OnItemClickListener){
        itemClickListener = onItemClickListener
    }

    fun setOnHeartClickListener(onHeartClickListener : OnHeartClickListener){
        heartClickListener = onHeartClickListener
    }

    override fun getItemCount(): Int = mainData.size
}