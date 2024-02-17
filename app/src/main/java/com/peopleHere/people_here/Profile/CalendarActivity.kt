package com.peopleHere.people_here.Profile

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
import com.peopleHere.people_here.Data.CalendarData
import com.peopleHere.people_here.Local.getJwt
import com.peopleHere.people_here.R
import com.peopleHere.people_here.Remote.AuthService
import com.peopleHere.people_here.Remote.UpcomingDateResponse
import com.peopleHere.people_here.Remote.UpcomingDateView
import com.peopleHere.people_here.databinding.ActivityCalendarBinding
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
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private var lastClickedDate: String? = null
    private val tabList = arrayListOf("참여 차단", "참여 가능으로 설정")

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initBottomSheet()

        tabList.forEachIndexed { index, title ->
            val tab = binding.tlEnterDate.newTab()
            val tabView = LayoutInflater.from(this).inflate(R.layout.custom_text_view, null) as TextView
            tabView.text = title
            tab.customView = tabView
            binding.tlEnterDate.addTab(tab)

            // 초기 탭 선택 설정
            val firstTab = binding.tlEnterDate.getTabAt(0)
            binding.tlEnterDate.selectTab(firstTab)
            setTabColor(firstTab, true)
        }

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
            override fun onDateClick(date: String, month: Int, year: Int, time: String?) {
                if (time != null) {
                    // 시간 정보가 있으면 bottomSheet의 내용 업데이트
                    updateBottomSheetContent(date, month, year, time)
                }
                showCalendarDialog(date, month, year, time)
            }
        })

        binding.rvMonth.adapter = monthAdapter
        binding.rvMonth.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL, false)

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

    private fun initBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.clBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        bottomSheetBehavior.peekHeight = 0
    }

    private fun showCalendarDialog(date: String, month: Int, year: Int, time: String?) {
        showBottomSheet(date, month, year, time)

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }


    private fun showBottomSheet(date: String, month: Int, year: Int, time: String?) {
        updateBottomSheetContent(date, month, year, time)

        binding.tlEnterDate.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                setTabColor(tab, true)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                setTabColor(tab, false)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        val displayMetrics = resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels

        val peekHeightRatio = 64.0 / 800.0
        bottomSheetBehavior.peekHeight = (screenHeight * peekHeightRatio).toInt()
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

    }

    private fun setTabColor(tab: TabLayout.Tab?, isSelected: Boolean) {
        binding.tvExistTime.text = tabList[tab!!.position]
        val tabView = (binding.tlEnterDate.getChildAt(0) as ViewGroup).getChildAt(tab.position) as View

        tab.customView?.let { view ->
            val textView = view as TextView
            if (isSelected) {
                tabView.background = ResourcesCompat.getDrawable(resources, R.drawable.rectangle_line_orange3_12, null)
                textView.setTextColor(ContextCompat.getColor(this, R.color.Orange5))
            } else {
                textView.setTextColor(ContextCompat.getColor(this, R.color.Gray5))
                tabView.background = null
                binding.tvExistTime.setTextColor(ContextCompat.getColor(this, R.color.Gray5))
            }
        }
    }

    private fun updateBottomSheetContent(date: String, month: Int, year: Int, time: String?) {
        binding.tvDialogMonth.text = month.toString()
        binding.tvDialogDay.text = date
        if (time != null) {
            binding.tvExistTime.text = time // 예시로, time 형식에 맞게 변환 필요할 수 있음
        } else {
            // 기본 텍스트 설정 또는 다른 처리
            binding.tvExistTime.text = "No Time Set"
        }
    }

    override fun UpcomingDateLoading() {
        TODO("Not yet implemented")
    }

    override fun UpcomingDateSuccess(content: ArrayList<UpcomingDateResponse>) {
        upcomingData = content
        Log.d("받은 데이터", upcomingData.toString())

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