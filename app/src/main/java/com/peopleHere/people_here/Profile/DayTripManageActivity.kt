package com.peopleHere.people_here.Profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.peopleHere.people_here.Local.getJwt
import com.peopleHere.people_here.Remote.AuthService
import com.peopleHere.people_here.Remote.BringCourseResponse
import com.peopleHere.people_here.Remote.BringCourseView
import com.peopleHere.people_here.databinding.ActivityDayTripManageBinding

class DayTripManageActivity : AppCompatActivity() , BringCourseView {
    lateinit var binding : ActivityDayTripManageBinding
    private var activeAdapter : ActiveTripAdapter?= null
    private var inactiveAdapter : InactiveTripAdapter?= null
    private var deletedAdapter : DeletedAdapter?= null
    private var dayTripData : ArrayList<BringCourseResponse> = arrayListOf()
    private var tourId : Int = 0
    private var tourName : String ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDayTripManageBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

        binding.btnBack.setOnClickListener {
            finish()
        }

        initDataManager(1, "created")
        initRecyclerView()

        setContentView(binding.root)
    }

    private fun initRecyclerView() {
        binding.rvActive.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL, false)
        binding.rvInactive.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL, false)
        binding.rvStop.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL, false)

        binding.rvActive.layoutManager = BlockScrollManager(this)
        binding.rvInactive.layoutManager = BlockScrollManager(this)
        binding.rvStop.layoutManager = BlockScrollManager(this)
    }

    override fun BringCourseLoading() {
        TODO("Not yet implemented")
    }

    override fun BringCourseSuccess(content: ArrayList<BringCourseResponse>) {
        val activeData = content.filter { it.status == "ACTIVE" }
        val inactiveData = content.filter { it.status == "INACTIVE" }
        val deletedData = content.filter { it.status == "DELETED" }

        activeAdapter = ActiveTripAdapter(applicationContext, ArrayList(activeData))
        inactiveAdapter = InactiveTripAdapter(applicationContext, ArrayList(inactiveData))
        deletedAdapter = DeletedAdapter(applicationContext, ArrayList(deletedData))

        binding.rvActive.adapter = activeAdapter
        binding.rvInactive.adapter = inactiveAdapter
        binding.rvStop.adapter = deletedAdapter

        activeAdapter!!.setOnItemClickListener(object : ActiveTripAdapter.OnItemClickListener{
            override fun onItemClick(dayTripInfo : BringCourseResponse) {
                val intent = Intent(this@DayTripManageActivity, CalendarActivity::class.java)
                tourId = dayTripInfo.tourId
                tourName = dayTripInfo.tourName
                intent.putExtra("tourId",tourId)
                intent.putExtra("tourName",tourName)
                startActivity(intent)
            }
        })

        inactiveAdapter!!.setOnItemClickListener(object : InactiveTripAdapter.OnItemClickListener{
            override fun onItemClick(dayTripInfo : BringCourseResponse) {
                val intent = Intent(this@DayTripManageActivity, CalendarActivity::class.java)
                tourId = dayTripInfo.tourId
                intent.putExtra("tourId",tourId)
                startActivity(intent)
            }
        })

        deletedAdapter!!.setOnItemClickListener(object : DeletedAdapter.OnItemClickListener{
            override fun onItemClick(dayTripInfo : BringCourseResponse) {
                val intent = Intent(this@DayTripManageActivity, CalendarActivity::class.java)
                tourId = dayTripInfo.tourId
                intent.putExtra("tourId",tourId)
                startActivity(intent)
            }
        })

        if(content.isNotEmpty()){
            binding.svTripExist.visibility = View.VISIBLE
            binding.clTripNone.visibility = View.GONE
        } else{
            binding.svTripExist.visibility = View.GONE
            binding.clTripNone.visibility = View.VISIBLE
        }
    }

    override fun BringCourseFailure(status: Int, message: String) {
        Log.d("BringError1", status.toString())
        Log.d("BringError2", message)
    }

    private fun initDataManager(id : Int, option : String) {
        val token = getJwt()
        Log.d("token",token)
        if(token.isNotEmpty()){
            val authService = AuthService(this)
            authService.setBringCourseView(this)
            authService.bringCourseInfo(id, option)
        }else{
            Log.d("token 오류","token 오류")
        }
    }

}