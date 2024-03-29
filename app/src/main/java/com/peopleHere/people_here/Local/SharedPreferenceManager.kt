package com.peopleHere.people_here.Local

import android.content.SharedPreferences
import com.peopleHere.people_here.ApplicationClass

fun getJwt(): String {
    val prefs: SharedPreferences = ApplicationClass.mSharedPreferencesManager
   return prefs.getString("token", null) ?: ""

//    아이디 : Test1234@konkuk.ac.kr
//    비밀번호 : Test123!!
//    userId : 6
}
fun removeJwt() {
    ApplicationClass.mSharedPreferencesManager.edit().apply {
        remove("token")
        apply()
    }
}

fun saveJwt(token: String) {
    ApplicationClass.mSharedPreferencesManager.edit().apply {
        putString("token", token)
        apply()
    }
}
