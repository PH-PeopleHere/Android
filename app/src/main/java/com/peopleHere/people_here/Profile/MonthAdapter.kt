package com.peopleHere.people_here.Profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.peopleHere.people_here.Data.CalendarData
import com.peopleHere.people_here.Remote.UpcomingDateResponse
import com.peopleHere.people_here.databinding.ItemCalendarMonthBinding

class MonthAdapter(val calendarData : ArrayList<CalendarData>,
                   val upComingData : List<UpcomingDateResponse>,
                   val context : CalendarActivity,
                   val dateClickListener: DateAdapter.OnDateClickListener)
    : RecyclerView.Adapter<MonthAdapter.ViewHolder>() {

    inner class ViewHolder(val binding : ItemCalendarMonthBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : CalendarData){
            binding.tvMonth.text = item.month.toString()
            binding.tvYear.text = item.year.toString()

            binding.rvDate.layoutManager = GridLayoutManager(binding.rvDate.context, 7)
//            binding.rvDate.adapter = DateAdapter(item.day)

            binding.rvDate.adapter = DateAdapter(item, item.month, item.year, dateClickListener, upComingData, context)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCalendarMonthBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(calendarData[position])
    }

    override fun getItemCount() : Int{
        return calendarData.size
    }
}
