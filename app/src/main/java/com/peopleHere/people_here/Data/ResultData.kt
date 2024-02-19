package com.peopleHere.people_here.Data

import com.google.gson.annotations.SerializedName

data class ResultData(
    @SerializedName("userId") val userId: Int,
    @SerializedName("userName") val userName: String,
    @SerializedName("userImageUrl") val userImageUrl: String,
    @SerializedName("id") val id: Int,
    @SerializedName("email") val email: String,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("address") val address: String,
    @SerializedName("birth") val birth: String,
    @SerializedName("job") val job: String,
    @SerializedName("almaMater") val almaMater: String,
    @SerializedName("hobby") val hobby: String,
    @SerializedName("pet") val pet: String,
    @SerializedName("favourite") val favourite: String,
    @SerializedName("status") val status: String,
    @SerializedName("languages") val languages: List<String>,
    @SerializedName("questions") val questions: QuestionsRequestData
)
