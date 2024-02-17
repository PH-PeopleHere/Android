package com.peopleHere.people_here.CourseContents

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.peopleHere.people_here.Data.TourLeaderData
import com.peopleHere.people_here.Local.getJwt
import com.peopleHere.people_here.Remote.AuthService
import com.peopleHere.people_here.Remote.JoinConfirmResponse
import com.peopleHere.people_here.Remote.JoinConfirmView
import com.peopleHere.people_here.Remote.RequestEnjoyResponse
import com.peopleHere.people_here.Remote.RequestEnjoyView
import com.peopleHere.people_here.databinding.ActivityRequestEnjoyBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RequestEnjoyActivity : AppCompatActivity() , RequestEnjoyView , JoinConfirmView {
    lateinit var binding : ActivityRequestEnjoyBinding
    private var dates : String ?= null
    private var tourDatesId : Int = 0
    private var requestData : RequestEnjoyResponse? = null
    private var leaderData : TourLeaderData ?= null
    private var success : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRequestEnjoyBinding.inflate(layoutInflater)

        binding.tvService.text = Html.fromHtml("<u>피플히어의 서비스 정책<u>")

        dates = intent.getStringExtra("dates")
        tourDatesId = intent.getIntExtra("tourDatesId", 0)

        initDataManager(tourDatesId)

        Log.d("dates",dates.toString())
        Log.d("tourDatesId",tourDatesId.toString())

        binding.tvService.setOnClickListener {
            val intent = Intent(this, AppServiceActivity::class.java)
            startActivity(intent)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnPossibleEnjoy.setOnClickListener {
            requestConfirmManager(tourDatesId)
        }

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    private fun requestConfirmManager(tourDatesId: Int) {
        val token = getJwt()
        Log.d("token",token)
        if(token.isNotEmpty()){
            val authService = AuthService(this)
            authService.setJoinConfirmView(this)
            Log.d("tourDatesId",tourDatesId.toString())
            authService.joinConfirmInfo(tourDatesId)
        }else{
            Log.d("token 오류","token 오류")
        }
    }

    private fun initDataManager(tourDatesId: Int) {
        val token = getJwt()
        Log.d("token",token)
        if(token.isNotEmpty()){
            val authService = AuthService(this)
            authService.setRequestEnjoyView(this)
            Log.d("tourDatesId",tourDatesId.toString())
            authService.requestEnjoyInfo(tourDatesId)
        }else{
            Log.d("token 오류","token 오류")
        }
    }

    override fun RequestEnjoyLoading() {
        TODO("Not yet implemented")
    }

    override fun RequestEnjoySuccess(content: RequestEnjoyResponse) {
        requestData = content
        leaderData = content.tourLeader

        initRequestEnjoyInfo(requestData!!, leaderData!!)
    }

    private fun initRequestEnjoyInfo(
        requestData: RequestEnjoyResponse,
        leaderData: TourLeaderData
    ) {

        val hours = requestData.tourTime / 60
        val minutes = requestData.tourTime % 60
        binding.tvTotalTime.text = if (minutes > 0) {
            "${hours}시간 ${minutes}분"
        } else {
            "${hours}시간"
        }

        Glide.with(this)
            .load(requestData.places.map { it.placeImages[0] })
            .into(binding.ivMainTourListUser)

        Glide.with(this)
            .load(leaderData.userImageUrl)
            .into(binding.ivMainTourListUser)

        if (requestData.places.size > 1) {
            val addCount = requestData.places.size - 1
            binding.tvRegion.text = "${requestData.places[0].placeAddress} 외 ${addCount}개"
        } else if (requestData.places.isNotEmpty()) {
            binding.tvRegion.text = requestData.places[0].placeAddress
        } else {
            binding.tvRegion.text = "위치 정보 없음"
        }

        binding.tvTitle.text = requestData.tourName
        binding.tvWillMeetName.text = leaderData.userName
        binding.tvMeetingPeopleLanguages.text = leaderData.languages.toString()

        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputDateFormat = SimpleDateFormat("yyyy년 M월 d일 (E)", Locale.getDefault())
        val date = inputDateFormat.parse(requestData.date) ?: return
        binding.tvDateDay.text = outputDateFormat.format(date)

        if(requestData.time == null){
            binding.llTimeExist.visibility = View.GONE
            binding.tvDateTime.visibility = View.VISIBLE
        }else {
            binding.llTimeExist.visibility = View.VISIBLE
            binding.tvDateTime.visibility = View.GONE

            val inputDateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            val outputDateFormat = SimpleDateFormat("a hh:mm", Locale.getDefault())
            val startTime = inputDateFormat.parse(requestData.time!!)
            val calendar = Calendar.getInstance().apply {
                time = startTime ?: return
                add(Calendar.MINUTE, requestData.tourTime)
            }
            val endTime = calendar.time

            binding.tvTimeExistFirst.text = outputDateFormat.format(startTime)
            binding.tvTimeExistSecond.text = outputDateFormat.format(endTime)
        }
    }

    override fun RequestEnjoyFailure(status: Int, message: String) {
        Log.d("리퀘에러1",status.toString())
        Log.d("리퀘에러2",message)
    }

    override fun JoinConfirmLoading() {
        TODO("Not yet implemented")
    }

    override fun JoinConfirmSuccess(content: JoinConfirmResponse) {
        Log.d("성공했냐", content.message)
        initActivity(true)
    }

    override fun JoinConfirmFialure(status: Int, message: String) {
        Log.d("요청에러1",status.toString())
        Log.d("요청에러2",message)

        initActivity(false)
    }

    private fun initActivity(success: Boolean) {
        if(success){
            val intent = Intent(this, SuccessRequestActivity::class.java)
            startActivity(intent)
        }else{
            return
        }
    }
}