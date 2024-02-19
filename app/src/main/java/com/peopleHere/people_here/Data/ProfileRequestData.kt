package com.peopleHere.people_here.Data

import com.google.gson.annotations.SerializedName

data class ProfileRequestData(
    @SerializedName("imageRequest") val imageRequest: ImageRequestData,
    @SerializedName("questions") val questions: QuestionsRequestData,
    @SerializedName("languages") val languages: List<Int>,
    @SerializedName("address") val address: String,
    @SerializedName("job") val job: String,
    @SerializedName("almaMater") val almaMater: String,
    @SerializedName("hobby") val hobby: String,
    @SerializedName("pet") val pet: String,
    @SerializedName("favourite") val favourite: String,
    @SerializedName("content") val content: String
)

data class ImageRequestData(
    @SerializedName("encodingString") val encodingString: String,
    @SerializedName("originalFileName") val originalFileName: String
)

data class QuestionsRequestData(
    @SerializedName("additionalProp1") val additionalProp1: String,
    @SerializedName("additionalProp2") val additionalProp2: String,
    @SerializedName("additionalProp3") val additionalProp3: String
)