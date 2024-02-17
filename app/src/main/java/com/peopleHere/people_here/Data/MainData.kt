package com.peopleHere.people_here.Data

import com.google.gson.annotations.SerializedName

data class MainData (
    @SerializedName("tourId") val tourId: Int,
    @SerializedName("tourName") val tourName: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("userName") val userName: String,
    @SerializedName("userImageUrl") val userImageUrl: String,
    @SerializedName("tourTime") val time: Int,
    @SerializedName("tourContent") val content : String,
    @SerializedName("places") val places: ArrayList<MainCourseData>,
    @SerializedName("categoryNames") val categoryNames: List<String>,
    @SerializedName("participants") val participants: List<Any>,
    @SerializedName("status") val status: String,
    @SerializedName("wished") var wished : Boolean
//    var mainTourListTitle : String,
//    var mainTourListTime : String,
//    var mainTourHeart : Boolean,
//    var mainTourListRegion : ArrayList<String>,
//    var mainTourListCourses: ArrayList<MainCourseData>,
)

//data class MainPlace(
//    @SerializedName("id") val id: Int,
//    @SerializedName("content") val content: String,
//    @SerializedName("imageUrls") val imageUrls: List<String>,
//    @SerializedName("address") val address: String,
//    @SerializedName("order") val order: Int
//)