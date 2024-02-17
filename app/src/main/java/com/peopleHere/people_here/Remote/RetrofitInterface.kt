package com.peopleHere.people_here.Remote

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
// 레트로핏 인터페이스, post, get, patch 등 정의
interface RetrofitInterface {
    @POST("api/users/login")//login부분
    fun singin(//로그인 함수 만들고
        @Body request:SignInRequest//토큰을 받아야하나?
    ): Call<BaseResponse<SignInResponse>>//이론때 핸던 것 중 call방식으로 받겠다
    @GET("api/tours")
    fun mainInfo(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: List<String>
    ) : Call<BaseResponse<MainResponse>>

    @GET("api/tours/{id}")
    fun courseContentsInfo(@Path("id") id : Int)
    : Call<BaseResponse<CourseContentsResponse>>

    @GET("api/tour-dates/{tourId}/dates")
    fun upcomingDateInfo(@Path("tourId") tourId : Int)
    : Call<BaseResponse<ArrayList<UpcomingDateResponse>>>

    @GET("api/users/{id}/tours")
    fun bringCourseInfo(@Path("id") id : Int, @Query("option") option : String)
            : Call<BaseResponse<ArrayList<BringCourseResponse>>>

    @POST("api/users/wishlist/{tourId}")
    fun changeWishInfo(@Path("tourId") tourId : Int)
            : Call<BaseResponse<ChangeWishResponse>>

    @GET("/api/users/check-email")
    fun checkEmail(@Query("email") email: String) : Call<BaseResponse<CheckEmailResponse>>

    @POST("/api/users/signup")
    fun signup(
        @Body request: SignUpRequest
    ):Call<BaseResponse<SignUpResponse>>

    @PUT("/api/users/password")
    fun changePassword(
        @Body request: String
    ): Call<ChangePasswordResponse>

    @GET("api/tour-dates/{tourDateId}/date")
    fun requestEnjoyInfo(@Path("tourDateId") tourDateId : Int)
            : Call<BaseResponse<RequestEnjoyResponse>>

    @POST("api/tour-dates/{tourDateId}/join")
    fun joinConfirmInfo(@Path("tourDateId") tourDateId : Int)
            : Call<BaseResponse<JoinConfirmResponse>>


}
