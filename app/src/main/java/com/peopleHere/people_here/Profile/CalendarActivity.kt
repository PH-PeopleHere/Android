package com.peopleHere.people_here.Profile

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import com.peopleHere.people_here.Data.CalendarData
import com.peopleHere.people_here.Local.getJwt
import com.peopleHere.people_here.Main.MainAdapter
import com.peopleHere.people_here.Remote.AuthService
import com.peopleHere.people_here.Remote.UpcomingDateResponse
import com.peopleHere.people_here.Remote.UpcomingDateView
import com.peopleHere.people_here.databinding.ActivityCalendarBinding
import com.peopleHere.people_here.databinding.DialogCourseManageBinding
import com.peopleHere.people_here.databinding.DialogMakingTourAddListSequenceBinding
import java.util.Calendar

class CalendarActivity : AppCompatActivity() , UpcomingDateView{
    lateinit var binding : ActivityCalendarBinding
    private var calendarList : ArrayList<CalendarData> = arrayListOf()
    private var monthAdapter : MonthAdapter ?= null
    private var dateAdapter : DateAdapter ?= null
    private var calendarDialog : Dialog?= null
    private var tourId : Int = 0
    private var tourName : String ?= null
    private var upcomingData : ArrayList<UpcomingDateResponse> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        tourId = intent.getIntExtra("tourId",0)
        tourName = intent.getStringExtra("tourName")
        initDataManager(tourId)
        Log.d("tourId",tourId.toString())

        binding.rvWeek.layoutManager = GridLayoutManager(this,7)
        binding.rvWeek.adapter = WeekAdapter(arrayListOf("일","월","화","수","목","금","토"))
        binding.tvTitle.text = tourName

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun initRecyclerView(upcomingData : ArrayList<UpcomingDateResponse>) {
        val cal = Calendar.getInstance()
        val currentYear = cal.get(Calendar.YEAR)
        val currentMonth = cal.get(Calendar.MONTH) + 1 // Calendar.MONTH는 0부터 시작하므로 +1

        // 현재 월부터 12개월간의 데이터 생성
        for (i in 0 until 12) {
            val month = (currentMonth + i) % 12
            val year = currentYear + (currentMonth + i) / 12
            val dayList = calculateDays(year, month)

            calendarList.add(CalendarData(if (month == 0) year - 1 else year,
                if (month == 0) 12 else month,
                dayList))

        }

        monthAdapter = MonthAdapter(calendarList, upcomingData, this@CalendarActivity, object : DateAdapter.OnDateClickListener {
            override fun onDateClick(date: String, month: Int, year: Int) {
                showCalendarDialog(date, month, year)
            }
        })

        binding.rvMonth.adapter = monthAdapter
        binding.rvMonth.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL, false)

    }

    private fun showCalendarDialog(date: String, month: Int, year: Int) {
        val bottomSheetFragment = CalendarBottomSheetFragment.newInstance(date, month, year)
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
    }


    private fun calculateDays(year: Int, month: Int) : ArrayList<String> {
        val dayList = ArrayList<String>()
        val cal = Calendar.getInstance()

        // 설정된 년도와 월로 캘린더 설정, 1일로 설정
        cal.set(year, month - 1, 1)

        // 1 = 일요일, 2 = 월요일
        val startDayOfWeek = cal.get(Calendar.DAY_OF_WEEK)

        for (i in 1 until startDayOfWeek) {
            dayList.add("") // 공백으로 채움
        }

        // 해당 월의 마지막 날짜
        val lastDay = cal.getActualMaximum(Calendar.DATE)

        for (i in 1..lastDay) {
            dayList.add(i.toString())
        }

        return dayList
    }

    override fun UpcomingDateLoading() {
        TODO("Not yet implemented")
    }

    override fun UpcomingDateSuccess(content: ArrayList<UpcomingDateResponse>) {
        upcomingData = content
        Log.d("받은 데이터", upcomingData.toString())
//        classifiedData = classifyDataByMonth(content)

//        binding.tvReviewCount.text = upcomingData.size.toString()
//        enjoyAdapter?.notifyDataSetChanged()


        initRecyclerView(upcomingData)
    }

    override fun UpcomingDateFailure(status: Int, message: String) {
        Log.d("Calendar Activity 통신 오류1", status.toString())
        Log.d("Calendar Activity 통신 오류2", message)
    }

    private fun initDataManager(tourId : Int) {
        val token = getJwt()
        Log.d("token",token)
        if(token.isNotEmpty()){
            val authService = AuthService(this)
            authService.setUpcomingDateView(this)
            Log.d("tourId",tourId.toString())
            authService.upcomingDateInfo(tourId)
        }else{
            Log.d("token 오류","token 오류")
        }
    }

}