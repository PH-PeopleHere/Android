package com.peopleHere.people_here.Profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.peopleHere.people_here.databinding.FragmentProfileBinding
import com.peopleHere.people_here.Data.ProfileData
import com.peopleHere.people_here.Local.getJwt
//import com.peopleHere.people_here.Remote.ProfileService
//import com.peopleHere.people_here.Remote.ProfileView

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
        get() = requireNotNull(_binding)

    private lateinit var profileData: ProfileData

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)


        return binding.root


    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        initProfileDataManager()
    }

    private fun initProfileDataManager() {
        val token = getJwt()
        Log.d("token", token)
        if (token.isNotEmpty()) {
            //val profileService = ProfileService()
            // 서버 통신 뷰 연결
            //profileService.setProfileView(this)
            // 서버 통신
            //profileService.getProfile(1)
            Log.d("token", "있음")
        } else {
            Log.d("token 오류", "token 오류")
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

//    override fun profileLoading() {
//    }
//
//    override fun profileSuccess(result: ProfileData) {
//        profileData = result
//        Log.d("profileFragment", profileData.toString())
//    }
}