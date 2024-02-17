package com.peopleHere.people_here.CourseContents

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.peopleHere.people_here.R
import com.peopleHere.people_here.databinding.ActivitySuccessRequestBinding

class SuccessRequestActivity : AppCompatActivity(){
    lateinit var binding : ActivitySuccessRequestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySuccessRequestBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}