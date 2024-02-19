package com.peopleHere.people_here.Data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

//데이터 관리하고 쉽게 표현
data class ProfileInfo(
    val category:String,
    @SerializedName("details") val details: String

    ):Serializable
