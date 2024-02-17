package com.peopleHere.people_here.Data

import android.graphics.Point
import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName

data class MainCourseData(
    @SerializedName("placeId") val placeId: Int,
    @SerializedName("placeName") val placeName: String,
    @SerializedName("placeImages") val placeImages: List<String>,
    @SerializedName("placeAddress") val placeAddress: String,
    @SerializedName("latLng") val latLng : LatLng,
    @SerializedName("placeOrder") val placeOrder: Int
)