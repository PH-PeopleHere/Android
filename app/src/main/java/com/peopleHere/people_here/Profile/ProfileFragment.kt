package com.peopleHere.people_here.Profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.peopleHere.people_here.Local.getJwt

import com.peopleHere.people_here.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    private fun initProfileDataManager() {
        val token = getJwt()
        Log.d("token", token)
        if (token.isNotEmpty()) {
            Log.d("token", "있음")
        } else {
            Log.d("token 오류", "token 오류")
        }
    }

}