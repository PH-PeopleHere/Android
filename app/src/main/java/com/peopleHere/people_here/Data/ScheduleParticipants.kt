package com.peopleHere.people_here.Data

import com.google.gson.annotations.SerializedName

data class ScheduleParticipants (
    @SerializedName("userId") val id : Int,
    @SerializedName("userName") val name : String,
    @SerializedName("userImageUrl") val imageUrl : String
)