package com.peopleHere.people_here.CourseContents

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.peopleHere.people_here.databinding.ActivityAppServiceBinding

class AppServiceActivity : AppCompatActivity() {
    lateinit var binding : ActivityAppServiceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAppServiceBinding.inflate(layoutInflater)

        binding.btnBack.setOnClickListener {
            finish()
        }

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}