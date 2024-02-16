package com.peopleHere.people_here.CourseContents

import android.content.Intent
import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import com.peopleHere.people_here.databinding.ActivityRequestEnjoyBinding

class RequestEnjoyActivity : AppCompatActivity() {
    lateinit var binding : ActivityRequestEnjoyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRequestEnjoyBinding.inflate(layoutInflater)

        binding.tvService.text = Html.fromHtml("<u>피플히어의 서비스 정책/u>")

        binding.tvService.setOnClickListener {
            val intent = Intent(this, AppServiceActivity::class.java)
            startActivity(intent)
        }

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}