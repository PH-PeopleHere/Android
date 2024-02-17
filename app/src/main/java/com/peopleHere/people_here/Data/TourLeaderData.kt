package com.peopleHere.people_here.Data

import com.google.gson.annotations.SerializedName

data class TourLeaderData (
    @SerializedName("userId") val userId : Int,
    @SerializedName("userName") val userName : String,
    @SerializedName("userImageUrl") val userImageUrl : String,
    @SerializedName("languages") val languages : List<String>
)