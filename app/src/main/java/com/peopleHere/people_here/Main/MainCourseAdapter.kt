package com.peopleHere.people_here.Main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.peopleHere.people_here.Data.MainCourseData
import com.peopleHere.people_here.databinding.ItemMainTourListCourse1Binding
import com.peopleHere.people_here.databinding.ItemMainTourListCourse2Binding
import com.bumptech.glide.Glide
import com.peopleHere.people_here.Data.MainData
import com.peopleHere.people_here.R

class MainCourseAdapter(private val mainCourseData: ArrayList<MainCourseData>, private val mainData: MainData) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_FIRST_ITEM = 0
        private const val TYPE_OTHER_ITEM = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) TYPE_FIRST_ITEM else TYPE_OTHER_ITEM
    }

    inner class FirstItemViewHolder(val binding: ItemMainTourListCourse1Binding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tourListInfo: MainCourseData, tourListInfo2 : MainData) {
//            binding.ivMainTourListUser.setImageResource(context, binding.ivMainTourListUser, tourListInfo)
//            binding.tvMainTourListUser.text = tourListInfo.content
            setImage1(binding.root.context, binding.ivMainTourListUser, tourListInfo2)
            binding.tvMainTourListUser.text = tourListInfo2.userName
        }
    }

    private fun setImage2(context: Context, imageView: ImageView, tourListInfo: MainCourseData) {
        if(tourListInfo.placeImages[0] == "string") {
            imageView.setImageResource(R.drawable.img_example_contents)
        } else {
            Glide.with(context)
                .load(tourListInfo.placeImages[0])
                .into(imageView)
        }
    }

    private fun setImage1(context: Context, imageView: ImageView, tourListInfo: MainData) {
        if(tourListInfo.userImageUrl == "string") {
            imageView.setImageResource(R.drawable.img_example_user)
        } else {
            Glide.with(context)
                .load(tourListInfo.userImageUrl)
                .into(imageView)
        }
    }


    inner class OtherItemViewHolder(val binding: ItemMainTourListCourse2Binding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tourListInfo: MainCourseData) {
//            binding.ivMainTourListUserCourse.setImageResource(tourListInfo.id)
//            binding.tvItemMainTourListRegion.text = tourListInfo.address
            setImage2(binding.root.context, binding.ivMainTourListUserCourse, tourListInfo)
            binding.tvItemMainTourListRegion.text = tourListInfo.placeName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_FIRST_ITEM -> {
                val binding = ItemMainTourListCourse1Binding.inflate(inflater, parent, false)
                FirstItemViewHolder(binding)
            }
            else -> {
                val binding = ItemMainTourListCourse2Binding.inflate(inflater, parent, false)
                OtherItemViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = mainCourseData[position]
        val item2 = mainData
        when (holder) {
            is FirstItemViewHolder -> holder.bind(item, item2)
            is OtherItemViewHolder -> holder.bind(item)
        }
    }

    override fun getItemCount(): Int = mainCourseData.size
}
