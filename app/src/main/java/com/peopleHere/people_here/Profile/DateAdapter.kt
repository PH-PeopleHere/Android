package com.peopleHere.people_here.Profile

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.peopleHere.people_here.Data.CalendarData
import com.peopleHere.people_here.R
import com.peopleHere.people_here.Remote.UpcomingDateResponse
import com.peopleHere.people_here.databinding.ItemCalendarDateBinding
import java.text.SimpleDateFormat
import java.util.Locale

class DateAdapter(
    val dayList: CalendarData,
    val month: Int,
    val year: Int,
    private var dateClickListener: OnDateClickListener,
    private var upcomingData: List<UpcomingDateResponse>,
    private val context: Context,
) : RecyclerView.Adapter<DateAdapter.ViewHolder>() {
    private var selectedPosition = -1 // 현재 선택된 아이템
//    private var previousSelectedStatus = false // 이전에 선택된 아이템의 상태

    interface OnDateClickListener {
        fun onDateClick(date: String, month: Int, year: Int, time: String?)
    }

    inner class ViewHolder(val binding: ItemCalendarDateBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val item = dayList.day[position]
            binding.tvDay.text = item

            if (item != "") {
                val formattedDate = String.format("%04d-%02d-%02d", year, month, item.toInt())

                val dateData = upcomingData.find{ it.date == formattedDate }
                val dateStatus = upcomingData.find { it.date == formattedDate }?.status
                val dateTime = upcomingData.find { it.date == formattedDate }?.time
                val participantData =
                    upcomingData.find { it.date == formattedDate && it.status == "AVAILABLE" }

                if (selectedPosition == position) {
                    // 현재 아이템
                    binding.clCalendarDay.setBackgroundResource(R.drawable.calendar_gray6_12)
                    binding.tvDay.setTextColor(Color.WHITE)
                    binding.cvUserImage.visibility = View.GONE

                } else {
                    // 다른 아이템
                    binding.root.setBackgroundColor(Color.TRANSPARENT)
                    if (dateStatus == "AVAILABLE") {
                        if (participantData != null && participantData.participants.isNotEmpty()) {
                            binding.tvDay.setTextColor(
                                ContextCompat.getColor(context, R.color.Orange5)
                            )
                            binding.clCalendarDay.setBackgroundResource(R.drawable.circle_orange2)
                            binding.tvTime.visibility = View.GONE
                            binding.cvUserImage.visibility = View.VISIBLE

                            Glide.with(context)
                                .load(participantData.participants[0].imageUrl)
                                .into(binding.ivUserImage)

                        } else {
                            binding.tvDay.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.Orange5
                                )
                            )
                            binding.tvTime.visibility = View.VISIBLE
                            binding.cvUserImage.visibility = View.GONE
                            if (dateTime == null) {
                                binding.tvTime.text = "시간협의"
                            } else {
                                val inputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                                val outputFormat = SimpleDateFormat("a hh:mm", Locale.getDefault())
                                val date = inputFormat.parse(dateTime)
                                date?.let {
                                    val formattedTime = outputFormat.format(it)
                                    binding.tvTime.text = formattedTime
                                }
                            }
                        }
                    } else {
                        binding.tvDay.setTextColor(Color.BLACK)
                        binding.cvUserImage.visibility = View.GONE
                    }

                    binding.root.setOnClickListener {
                        val dateTime = upcomingData.find { it.date == formattedDate }?.time
                        if (selectedPosition == position) {
                            selectedPosition = -1 // 선택 해제
                        } else {
                            selectedPosition = position // 새 아이템 선택
                        }
                        notifyDataSetChanged()
                        dateClickListener.onDateClick(item, month, year, dateTime)
                    }
                }
            } else {
                binding.root.setOnClickListener(null)
                binding.root.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCalendarDateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = dayList.day.size
}
