package com.peopleHere.people_here.Login

import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.internal.ViewUtils.dpToPx
import com.peopleHere.people_here.ApplicationClass
import com.peopleHere.people_here.R
import com.peopleHere.people_here.Remote.AuthService
import com.peopleHere.people_here.Remote.SignInView
import com.peopleHere.people_here.databinding.ActivityLoginEmailNextBinding
import java.util.regex.Pattern

class LoginEmailNextActivity : AppCompatActivity(), SignInView {
    private lateinit var binding: ActivityLoginEmailNextBinding
    private var checkContinue: Boolean = false
    private var seeClicked: Boolean = false
    lateinit var questionPass: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginEmailNextBinding.inflate(layoutInflater)
        ButtonOn()
        var userEmail = ApplicationClass.mSharedPreferencesManager.getString("email", null)
        binding.tvEmailUser.text = userEmail//이메일 세팅
        binding.tvSee.setOnClickListener {
            if (seeClicked) {
                binding.etPassword.transformationMethod = null
                binding.tvSee.setText(R.string.activity_login_email_hide)
                seeClicked = false
            } else {
                // 비밀번호를 숨김으로 설정
                binding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.tvSee.setText(R.string.activity_login_email_see)
                seeClicked = true
            }
        }

        questionPass = binding.etPassword
        binding.cvContinue.setOnClickListener {
            if (checkContinue) {
                val authService = AuthService(this)//여기로 넘김
                authService.setSignInView(this)//자신이 상속해서 자신 넣어주기

                authService.signin(
                    userEmail.toString(),
                    binding.etPassword.text.toString()
                )//메소드 호출 따라서 엑티비에서 requset로 넘김


            }
        }
        binding.tvReset.setOnClickListener {
            val intent = Intent(this, PasswordResetActivity::class.java)
            startActivity(intent)

        }
        setContentView(binding.root)
    }


    fun isPasswordValid(): Boolean {
        val pattern =
            "^(?=.*[~!@#\$%^&*_+=`|\\{}:;\"'<>,.?/()-0123456789])[A-Za-z[0-9]~!@#\$%^&*_+=`|\\{}:;\"'<>,.?/()-]{0,50}$"
        val p = Pattern.matches(pattern, questionPass.text.toString().trim())
        return p
    }

    private fun ButtonOn() {
        binding.etPassword.addTextChangedListener(object : TextWatcher {
            var maxtext = ""
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                if (editable.length >= 8 && isPasswordValid()) {//8자면 이상이여야 하니 ㅇㅇ
                    binding.cvContinue.setBackgroundResource(com.peopleHere.people_here.R.drawable.making_tour_button_next)
                    checkContinue = true
                } else {//클릭 불가능 하게도 설정하기
                    binding.cvContinue.setBackgroundResource(com.peopleHere.people_here.R.drawable.making_tour_button_close)//설정 회색으로
                    checkContinue = false

                    binding.ivWrong.setImageResource(0)
                    val layoutParams = binding.ivWrong.layoutParams
                    layoutParams.width = dpToPx(0) // dpToPx() 메서드는 dp 값을 픽셀로 변환하는 함수입니다.
                    layoutParams.height = dpToPx(0)
                    binding.tvWrong.setText("")
                    binding.etPassword.setBackgroundResource(R.drawable.login_phone_et)
                    //틀리면 기본으로 돌리기
                }
                if (editable.isNotEmpty()) {
                    binding.tvAfterPassword.setText("비밀번호")
                } else {
                    binding.tvAfterPassword.setText("")
                }

            }
        })
    }

    override fun SignInLoading() {
        TODO("Not yet implemented")
    }

    override fun SignInSuccess() {
        //텍스트 전달해주기 login logout쪽
        Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()//흐름 꼭 이해하기 로직
        onBackPressed()//뒤 화면으로 가지게 해서 바뀌게
    }

    override fun SignInFailure() {
        //여기 만약 틀리면 image 넣고, text 넣고, size 조절
        val red3 = ContextCompat.getColor(this, R.color.Red3)

        binding.ivWrong.setImageResource(R.drawable.exclamation_mark)
        val layoutParams = binding.ivWrong.layoutParams
        layoutParams.width = dpToPx(18) // dpToPx() 메서드는 dp 값을 픽셀로 변환하는 함수입니다.
        layoutParams.height = dpToPx(18)
        binding.tvWrong.setText("잘못된 비밀번호입니다.")
        binding.tvWrong.setTextColor(red3)
        binding.etPassword.setBackgroundResource(R.drawable.reset_password)
    }

    fun dpToPx(dp: Int): Int {
        val density = Resources.getSystem().displayMetrics.density
        return (dp * density).toInt()
    }

}