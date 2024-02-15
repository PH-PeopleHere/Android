package com.peopleHere.people_here.Remote

import android.content.Context
import android.content.Intent
import android.util.Log
import com.peopleHere.people_here.ApplicationClass
import com.peopleHere.people_here.Login.LoginEmailNextActivity
import com.peopleHere.people_here.SignUp.SignUpActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// 여기에 비동기식 통신 코드 구현
interface CheckEmailCallback {
    fun onEmailAvailable(isAvailable: Boolean)
}

class AuthService(private val context: Context) {

    private val authService = ApplicationClass.retrofit.create(RetrofitInterface::class.java)
    private lateinit var mainView: MainView

    private var checkEmailCallback: CheckEmailCallback? = null
    private lateinit var courseContentsView: CourseContentsView
    private lateinit var upcomingDateView: UpcomingDateView
    private lateinit var bringCourseView: BringCourseView
    private lateinit var changeWishView: ChangeWishView
    private lateinit var showDatesView: ShowDatesView

    // 본인 코드에서 사용할 함수 정의
//    fun xxx(singUpView : SignUpView){
//        this.signUpView = signUpView
//    }
    private lateinit var signInView: SignInView//로그인
    fun setCheckEmailCallback(callback: CheckEmailCallback) {
        this.checkEmailCallback = callback
    }

    fun setSignInView(signInView: SignInView) {//아직 초기화가 안됐음 따라서 activity에서 해주기
        this.signInView = signInView//signupVIew 사용 하기 위해
    }

    fun signin(id: String, pw: String) {
        val request = SignInRequest(id, pw)//email이랑 pw를 넘기겠지
        authService.singin(request)
            .enqueue(object : Callback<BaseResponse<SignInResponse<JwtToken>>> {
                //익명이니 함수 구현 해줘야 함+enqueue 로 자동 비동기
                override fun onResponse(
                    call: Call<BaseResponse<SignInResponse<JwtToken>>>,
                    response: Response<BaseResponse<SignInResponse<JwtToken>>>
                ) {
                    var resp = response.body()//여기 token이랑 nickname두개 받는데 어케 처리>
                    Log.d("Signin response", resp.toString())//로그에 찍기
                    when (resp!!.status) {//nullable 하고code부분 받아
                        200 -> {//성공
                            val signInResponse = resp.result//ID및 토큰 가져오게
                            signInResponse?.let {
                                signInView.SignInSuccess()//여기는 그냥 성공만

                                //saveJwt(resp.result.UserId,resp.result.token.accessToken)//nickname,token대입

                                Log.d(
                                    "AuthService",
                                    "Nickname: ${resp.result.userId}, Token: ${resp.result.token.accessToken}"
                                )
                                //TODO:여기서 sharedrpeference에 넣고, loginActivity에서 가져와서 텍스트 전달?
                            }
                        }

                        else -> signInView.SignInFailure(resp.code, resp.message)//실패에 이걸 넘겨줌
                    }
                }

                override fun onFailure(
                    call: Call<BaseResponse<SignInResponse<JwtToken>>>,
                    t: Throwable
                ) {
                    Log.d("SignIn failed", t.toString())//실패했을때 어떤 이윤지 쓰로어블하게
                }
            })
    }

    fun setMainView(mainView: MainView) {
        this.mainView = mainView
    }

    fun setCourseContentsView(courseContentsView: CourseContentsView){
        this.courseContentsView = courseContentsView
    }

    fun setUpcomingDateView(upcomingDateView: UpcomingDateView){
        this.upcomingDateView = upcomingDateView
    }

    fun setBringCourseView(bringCourseView: BringCourseView){
        this.bringCourseView = bringCourseView
    }

    fun setChangeWishView(changeWishView: ChangeWishView){
        this.changeWishView = changeWishView
    }

    fun setShowDatesView(showDatesView: ShowDatesView){
        this.showDatesView = showDatesView
    }

    fun mainInfo() {
//        mainView.MainLoading()
        authService.mainInfo(0, 10, listOf("createdAt,asc")).enqueue(object : Callback<BaseResponse<MainResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<MainResponse>>,
                response: Response<BaseResponse<MainResponse>>
            ) {
//                Log.d("response", response.toString())
                if (response.isSuccessful) {
                    val resp = response.body()
//                    Log.d("Main Response Body", resp.toString())
//                    Log.d("Main Response Body result", resp?.result.toString())
                    when (resp!!.status) {
                        200 -> mainView.MainSuccess(resp.result.content)
                        else -> mainView.MainFailure(resp.status, resp.message)
                    }
                }
            }
            override fun onFailure(call: Call<BaseResponse<MainResponse>>, t: Throwable) {
                Log.d("Main Failed", t.toString())
            }

        })
    }

    fun checkEmail(email: String) {
        authService.checkEmail(email)
            .enqueue(object : Callback<BaseResponse<CheckEmailResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<CheckEmailResponse>>,
                    response: Response<BaseResponse<CheckEmailResponse>>
                ) {
                    val resp = response.body()
                    Log.d("response_error_checkemail",response.message())
                    resp?.let {
                        when (it.status) {
                            200 -> {
                                // 성공적인 응답 처리
                                val checkEmailResponse = it.result
                                checkEmailResponse?.let { response ->
                                    if (response.emailAvailable) {
                                        val intent = Intent(context, SignUpActivity::class.java)
                                        //intent.putExtra("email", binding.etEmail.text.toString())
                                        context.startActivity(intent)
                                    } else {
                                        val intent =
                                            Intent(context, LoginEmailNextActivity::class.java)
                                        //intent.putExtra("email", binding.etEmail.text.toString())
                                        context.startActivity(intent)
                                    }
                                    Log.d("CheckEmail", "qewewq")
                                    checkEmailCallback?.onEmailAvailable(response.emailAvailable)
                                }
                            }
                            else -> {
                                Log.d(
                                    "checkEmail_fail",
                                    "fail"
                                )
                                signInView.SignInFailure(it.code, it.message)
                            }
                        }
                    } ?: run {
                        Log.e("checkEmail", "Response body is null")

                    }
                }
                override fun onFailure(
                    call: Call<BaseResponse<CheckEmailResponse>>,
                    t: Throwable
                ) {
                    // 네트워크 실패 처리
                    Log.e("checkEmail", "Network request failed", t)
                }
            })
    }

    fun signup(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        gender: String,
        birth: String,
    ) {
        val request = SignUpRequest(
            email,
            password,
            firstName,
            lastName,
            gender,
            birth
        )//email이랑 pw를 넘기겠지
        authService.signup(request)
            .enqueue(object : Callback<BaseResponse<SignUpResponse>> {
                //익명이니 함수 구현 해줘야 함+enqueue 로 자동 비동기
                override fun onResponse(
                    call: Call<BaseResponse<SignUpResponse>>,
                    response: Response<BaseResponse<SignUpResponse>>
                ) {
                    Log.d("response_e", email)
                    Log.d("response_p", password)
                    Log.d("response_f", firstName)
                    Log.d("response_l", lastName)
                    Log.d("response_g", gender)
                    Log.d("response_b", birth)
                    Log.d("response_signup", response.toString())
                    val resp = response.body() ?: run {
                        Log.e("Signup response", "Response body is null")
                        return
                    }
                    when (resp.status) {
                        200 -> {
                            val signUpResponse = resp.result
                            signUpResponse?.let {
                                Log.d("Response_success", resp.result.userId.toString())
                            }
                        }
                        else -> signInView.SignInFailure(resp.code, resp.message)
                    }
                }
                override fun onFailure(
                    call: Call<BaseResponse<SignUpResponse>>,
                    t: Throwable
                ) {
                    Log.d("SignIn failed", t.toString())//실패했을때 어떤 이윤지 쓰로어블하게
                }
            })
    }

    fun courseContentsInfo(tourId: Int) {
//        mainView.MainLoading()
        authService.courseContentsInfo(tourId)
            .enqueue(object : Callback<BaseResponse<CourseContentsResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<CourseContentsResponse>>,
                    response: Response<BaseResponse<CourseContentsResponse>>
                ) {
//                Log.d("response", response.toString())
                    if (response.isSuccessful) {
                        val resp = response.body()
//                    Log.d("CourseContents Response Body", resp.toString())
//                    Log.d("CourseContents Response Body result", resp?.result.toString())
                        when (resp!!.status) {
                            200 -> courseContentsView.CourseContentsSuccess(resp.result)
                            else -> courseContentsView.CourseContentsFailure(
                                resp.status,
                                resp.message
                            )
                        }
                    }
                }

                override fun onFailure(
                    call: Call<BaseResponse<CourseContentsResponse>>,
                    t: Throwable
                ) {
                    Log.d("CourseContents Failed", t.toString())
                }

            })
    }

    fun upcomingDateInfo(tourId: Int) {
//        mainView.MainLoading()
        authService.upcomingDateInfo(tourId)
            .enqueue(object : Callback<BaseResponse<ArrayList<UpcomingDateResponse>>> {
                override fun onResponse(
                    call: Call<BaseResponse<ArrayList<UpcomingDateResponse>>>,
                    response: Response<BaseResponse<ArrayList<UpcomingDateResponse>>>
                ) {
//                Log.d("Upcoming response", response.toString())
                    if (response.isSuccessful) {
                        val resp = response.body()
                        Log.d("Upcoming Response Body", resp.toString())
                        Log.d("Upcoming Response Body result", resp?.result.toString())
                        when (resp!!.status) {
                            200 -> upcomingDateView.UpcomingDateSuccess(resp.result)
                            else -> upcomingDateView.UpcomingDateFailure(
                                resp.status,
                                resp.message
                            )
                        }
                    }
                }

                override fun onFailure(
                    call: Call<BaseResponse<ArrayList<UpcomingDateResponse>>>,
                    t: Throwable
                ) {
                    Log.d("Upcoming Failed", t.toString())
                }

            })
    }

    fun bringCourseInfo(id: Int, option: String) {
//        mainView.MainLoading()
        authService.bringCourseInfo(id, option)
            .enqueue(object : Callback<BaseResponse<ArrayList<BringCourseResponse>>> {
                override fun onResponse(
                    call: Call<BaseResponse<ArrayList<BringCourseResponse>>>,
                    response: Response<BaseResponse<ArrayList<BringCourseResponse>>>
                ) {
                    Log.d("BringCourse response", response.toString())
                    if (response.isSuccessful) {
                        val resp = response.body()
                        Log.d("BringCourse Response Body", resp.toString())
                        Log.d("BringCourse Response Body result", resp?.result.toString())
                        when (resp!!.status) {
                            200 -> bringCourseView.BringCourseSuccess(resp.result)
                            else -> bringCourseView.BringCourseFailure(
                                resp.status,
                                resp.message
                            )
                        }
                    }
                }

                override fun onFailure(
                    call: Call<BaseResponse<ArrayList<BringCourseResponse>>>,
                    t: Throwable
                ) {
                    Log.d("Upcoming Failed", t.toString())
                }

            })
    }

    fun changeWishInfo(tourId: Int) {
//        mainView.MainLoading()
        authService.changeWishInfo(tourId)
            .enqueue(object : Callback<BaseResponse<ChangeWishResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<ChangeWishResponse>>,
                    response: Response<BaseResponse<ChangeWishResponse>>
                ) {
                    Log.d("ChangeWish response", response.toString())
                    if (response.isSuccessful) {
                        val resp = response.body()
                        Log.d("ChangeWish Response Body", resp.toString())
                        Log.d("ChangeWish Response Body result", resp?.result.toString())
                        when (resp!!.status) {
                            200 -> changeWishView.ChangeWishSuccess()
                            else -> changeWishView.ChangeWishFailure(resp.status, resp.message)
                        }
                    }
                }

                override fun onFailure(
                    call: Call<BaseResponse<ChangeWishResponse>>,
                    t: Throwable
                ) {
                    Log.d("Upcoming Failed", t.toString())
                }

            })
    }

    fun showDatesInfo(tourId: Int) {
//        mainView.MainLoading()
        authService.showDatesInfo(tourId)
            .enqueue(object : Callback<BaseResponse<ShowDatesView>> {
                override fun onResponse(
                    call: Call<BaseResponse<ShowDatesView>>,
                    response: Response<BaseResponse<ShowDatesView>>
                ) {
                    Log.d("ChangeWish response", response.toString())
                    if (response.isSuccessful) {
                        val resp = response.body()
                        Log.d("ChangeWish Response Body", resp.toString())
                        Log.d("ChangeWish Response Body result", resp?.result.toString())
                        when (resp!!.status) {
                            200 -> changeWishView.ChangeWishSuccess()
                            else -> changeWishView.ChangeWishFailure(resp.status, resp.message)
                        }
                    }
                }

                override fun onFailure(
                    call: Call<BaseResponse<ShowDatesView>>,
                    t: Throwable
                ) {
                    Log.d("Upcoming Failed", t.toString())
                }

            })
    }

}