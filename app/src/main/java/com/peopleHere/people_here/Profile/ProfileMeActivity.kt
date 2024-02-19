package com.peopleHere.people_here.Profile

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.peopleHere.people_here.ApplicationClass
import com.peopleHere.people_here.Data.ProfileInfo
import com.peopleHere.people_here.Data.ProfileInfoData
import com.peopleHere.people_here.Data.ResultData
import com.peopleHere.people_here.Remote.RetrofitInterface
import com.peopleHere.people_here.databinding.ActivityProfileMeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileMeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileMeBinding
    private val wordList: ArrayList<ProfileInfo> = arrayListOf()
    private var profileInfoAdapter: ProfileInfoAdapter? = null
    private val authService = ApplicationClass.retrofit.create(RetrofitInterface::class.java)
    private val resultList: ArrayList<ResultData> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileMeBinding.inflate(layoutInflater)

        binding.ivProfileX.setOnClickListener {
            finish()
        }

        setContentView(binding.root)

        initWords()
        initRecyclerView()

        // api 통신 테스트
        getProfileInfo(1)

    }

    private fun initRecyclerView() {
        profileInfoAdapter = ProfileInfoAdapter(wordList)
        binding.clProfileMeInfo.adapter = profileInfoAdapter
        binding.clProfileMeInfo.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
    }

    private fun initWords() {

    }

    private fun getProfileInfo(id: Int) {
//        mainView.MainLoading()
        authService.getProfileInfo(id)
            .enqueue(object : Callback<ProfileInfoData> {
                override fun onResponse(
                    call: Call<ProfileInfoData>,
                    response: Response<ProfileInfoData>
                ) {
                    Log.d("ProfileGet response", response.toString())
                    if (response.isSuccessful) {
                        val resp = response.body()
                        Log.d("ProfileGet Response Body", resp.toString())
                        Log.d("ProfileGet Response Body result", resp?.result.toString())
                        when (resp!!.status) {
                            200 -> {
                                val profileResponse = resp.result
                                profileResponse?.let {
                                    Log.d("Response_profile_success", resp.result.toString())

                                    resp?.result?.let { resultData ->
                                        updateWordListWithResultData(resultData)
                                        // RecyclerView 어댑터에 데이터 변경 알림
                                        profileInfoAdapter?.notifyDataSetChanged()
                                    }
                                }
                            }
                        }
                    }
                }

                override fun onFailure(
                    call: Call<ProfileInfoData>,
                    t: Throwable
                ) {
                    Log.d("ProfileGet Failed", t.toString())
                }

            })
    }

    private fun updateWordListWithResultData(resultData: ResultData) {
        // resultList가 필요한 경우, 이 메서드 안에서 wordList를 업데이트
        wordList.clear() // 기존 데이터 클리어

        wordList.addAll(
            arrayListOf(
                ProfileInfo("구사언어", resultData.languages.joinToString(", ")),
                ProfileInfo("거주지", resultData.address),
                ProfileInfo("나이", resultData.birth),
                ProfileInfo("직업", resultData.job),
                ProfileInfo("출신학교", resultData.almaMater),
                ProfileInfo("취미", resultData.favourite),
                ProfileInfo("반려동물", resultData.pet),
                ProfileInfo("좋아하는 것", resultData.favourite)

            )
        )
    }

}