package com.peopleHere.people_here.Profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.peopleHere.people_here.Login.LoginEmailActivity
import com.peopleHere.people_here.Login.LoginPhoneActivity
import com.peopleHere.people_here.R
import com.peopleHere.people_here.databinding.FragmentProfileFirstBinding

class ProfileFirstFragment : Fragment() {
    private lateinit var binding:FragmentProfileFirstBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentProfileFirstBinding.inflate(layoutInflater)

        binding.btnYes.setOnClickListener {
            val intent = Intent(requireContext(), LoginPhoneActivity::class.java)
            startActivity(intent)
        }


        return binding.root
    }


}