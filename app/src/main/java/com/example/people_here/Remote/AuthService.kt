package com.example.people_here.Remote

import android.util.Log
import com.example.people_here.ApplicationClass
import com.example.people_here.Local.saveJwt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// 여기에 비동기식 통신 코드 구현
class AuthService {
    private val authService = ApplicationClass.retrofit.create(RetrofitInterface::class.java)
    private lateinit var mainView: MainView

    // 본인 코드에서 사용할 함수 정의
//    fun xxx(singUpView : SignUpView){
//        this.signUpView = signUpView
//    }
    private lateinit var signInView: SignInView//로그인

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

    fun mainInfo() {
        /* mainView.MainLoading()
         authService.mainInfo().enqueue(object : Callback<BaseResponse<MainResponse>>{
             override fun onResponse(
                 call: Call<BaseResponse<MainResponse>>,
                 response: Response<BaseResponse<MainResponse>>
             ) {
                 Log.d("response", response.toString())
                 if(response.isSuccessful){
                     val resp = response.body()
                     Log.d("Main Response Body", resp.toString())
                     Log.d("Main Response Body result", resp?.result.toString())
                     when(resp!!.code){
                         200 -> mainView.MainSuccess()
                     }
                 }
             }*/

        /*   override fun onFailure(call: Call<BaseResponse<MainResponse>>, t: Throwable) {
               Log.d("User Failed", "User Failed")
           }
*/
    }

    fun checkEmail(email:String) {
        authService.checkEmail(email)
            .enqueue(object : Callback<BaseResponse<CheckEmailResponse>> {
                //익명이니 함수 구현 해줘야 함+enqueue 로 자동 비동기
                override fun onResponse(
                    call: Call<BaseResponse<CheckEmailResponse>>,
                    response: Response<BaseResponse<CheckEmailResponse>>
                ) {
                    //여기가 실행 x인데?
                    var resp = response.body()//여기 token이랑 nickname두개 받는데 어케 처리>

                    Log.d("CheckEmail", resp.toString())//로그에 찍기

                    when (resp!!.status) {//nullable 하고code부분 받아
                        200 -> {//성공
                            val checkEmailResponse = resp.result//emailAvailable&message
                            checkEmailResponse?.let {

                                Log.d(
                                    "CheckEmail2",
                                    "Boolean: ${resp.result.emailAvailable}"
                                )
                                //TODO:여기서 sharedrpeference에 넣고, loginActivity에서 가져와서 텍스트 전달?
                            }
                        }

                        else -> signInView.SignInFailure(resp.code, resp.message)//400 즉 뭔가 오류
                    }
                }


                override fun onFailure(
                    call: Call<BaseResponse<CheckEmailResponse>>,
                    t: Throwable
                ) {
                    Log.d("email failed", t.toString())//실패했을때 어떤 이윤지 쓰로어블하게
                }
            })
    }
}
