package com.peopleHere.people_here.Data

import com.google.gson.annotations.SerializedName

data class ProfileInfoData(
    @SerializedName("success") val success: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ResultData
)
