package com.peopleHere.people_here.CourseContents

import android.content.Context
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.peopleHere.people_here.Data.CourseReviewData
import com.peopleHere.people_here.Data.CourseScheduleData
import com.peopleHere.people_here.Remote.CourseContentsResponse
import com.peopleHere.people_here.Remote.UpcomingDateResponse
import com.peopleHere.people_here.databinding.ItemCourseComingDateBinding
import com.peopleHere.people_here.databinding.ItemCourseContentsReviewBinding
import com.peopleHere.people_here.databinding.ItemCourseContentsReviewInnerBinding
import com.peopleHere.people_here.databinding.ItemPossibleEnjoyDateInnerBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PossibleEnjoyInnerAdapter(
    private val comingData: ArrayList<UpcomingDateResponse>,
    private val tourTime: Int,
    var itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<PossibleEnjoyInnerAdapter.ViewHolder>() {

    private val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val outputDateFormat = SimpleDateFormat("M월 d일 (E)", Locale.getDefault())
    private val timeInputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    private val timeOutputFormat = SimpleDateFormat("a hh:mm", Locale.getDefault())

    interface OnItemClickListener {
        fun onItemClick(dateInfo: UpcomingDateResponse)
    }

    inner class ViewHolder(val binding: ItemPossibleEnjoyDateInnerBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(comingInfo: UpcomingDateResponse) {
            // 날짜 환산
            val date = inputDateFormat.parse(comingInfo.date) ?: return
            binding.tvEnjoyYear.text = outputDateFormat.format(date)

            // 시간 환산
            comingInfo.time?.let {
                val startTime = timeInputFormat.parse(it)
                startTime?.let { st ->
                    val calendar = Calendar.getInstance().apply {
                        time = st
                        add(Calendar.MINUTE, tourTime)
                    }
                    val endTime = calendar.time
                    binding.llTimeExist.visibility = View.VISIBLE
                    binding.tvTimeNone.visibility = View.GONE
                    binding.tvTimeExistFirst.text = timeOutputFormat.format(st)
                    binding.tvTimeExistSecond.text = timeOutputFormat.format(endTime)
                } ?: run {
                    // 시간 파싱 실패 처리
                    binding.llTimeExist.visibility = View.GONE
                    binding.tvTimeNone.visibility = View.VISIBLE
                }
            } ?: run {
                // time 필드가 null인 경우
                binding.llTimeExist.visibility = View.GONE
                binding.tvTimeNone.visibility = View.VISIBLE
            }

            binding.btnComingDateEnjoy.setOnClickListener {
                itemClickListener.onItemClick(comingInfo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPossibleEnjoyDateInnerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(comingData[position])
    }

    override fun getItemCount(): Int = comingData.size

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        itemClickListener = onItemClickListener
    }
}
