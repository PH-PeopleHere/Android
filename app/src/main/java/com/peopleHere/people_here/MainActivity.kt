package com.peopleHere.people_here

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.peopleHere.people_here.ApplicationClass.Companion.X_ACCESS_TOKEN
import com.peopleHere.people_here.Main.MainFragment
import com.peopleHere.people_here.MakingTour.MakingTourAddListActivity
import com.peopleHere.people_here.MyTour.MakingCourseFragment
import com.peopleHere.people_here.Profile.ProfileFirstFragment
import com.peopleHere.people_here.MyTour.MakingCourseSearchActivity
import com.peopleHere.people_here.Profile.DayTripManageActivity
import com.peopleHere.people_here.Profile.ProfileFragment
import com.peopleHere.people_here.TitleCategory.MakingTourFragment
import com.peopleHere.people_here.WishList.WishFragment
import com.peopleHere.people_here.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("token_check",X_ACCESS_TOKEN)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, MainFragment()).commit()
                    return@setOnItemSelectedListener true
                }

                R.id.menu_wish -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_frm, WishFragment())
                        .commit()
                    return@setOnItemSelectedListener true
                }


//                R.id.menu_making_course -> {//코스 만들기 고쳤다아
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.main_frm, MakingCourseFragment()).commit()
//                    return@setOnItemSelectedListener true
//                }

                R.id.menu_making_course -> {//코스 만들기 고쳤다아
                    val intent = Intent(this, MakingTourAddListActivity::class.java)
                    startActivity(intent)
                    return@setOnItemSelectedListener true
                }

                R.id.menu_message -> {//
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, MakingTourFragment()).commit()
                    return@setOnItemSelectedListener true
                }


                R.id.menu_profile -> {//코스 만들기 고쳤다아
                    val intent = Intent(this, DayTripManageActivity::class.java)
                    startActivity(intent)
                    return@setOnItemSelectedListener true
                }

//                R.id.menu_profile -> {
//                    if(X_ACCESS_TOKEN=="Authorization"){
//                        supportFragmentManager.beginTransaction()
//                            .replace(R.id.main_frm, ProfileFirstFragment()).commit()
//                        return@setOnItemSelectedListener true
//
//                    }else{
//                        supportFragmentManager.beginTransaction()
//                            .replace(R.id.main_frm, ProfileFragment()).commit()
//                        return@setOnItemSelectedListener true
//
//                    }
//                }

                else -> {
                    return@setOnItemSelectedListener true
                }
            }
        }
        binding.navigation.selectedItemId = R.id.menu_home
    }
}
