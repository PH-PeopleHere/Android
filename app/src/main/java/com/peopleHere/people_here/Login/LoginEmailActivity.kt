package com.peopleHere.people_here.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.TextView
import androidx.databinding.DataBindingUtil.setContentView
import com.peopleHere.people_here.ApplicationClass
import com.peopleHere.people_here.R
import com.peopleHere.people_here.Remote.AuthService
import com.peopleHere.people_here.Remote.CheckEmailCallback
import com.peopleHere.people_here.databinding.ActivityLoginEmailBinding
import java.util.regex.Pattern


class LoginEmailActivity : AppCompatActivity(), CheckEmailCallback {
    private lateinit var binding: ActivityLoginEmailBinding
    private var checkContinue: Boolean = false
    val emailValidation =
        "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    lateinit var questionEmail: TextView
    private val authService = AuthService(this)//여기로 넘김

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginEmailBinding.inflate(layoutInflater)
        ButtonOn()
        binding.cvContinue.setOnClickListener {
            if (checkContinue) {
                //여기서 server에서 받아서 false면 signUp으로 가고 true면 LoginEmailNext로 보내기
                val email = binding.etEmail.text.toString()//아이디보냄
                ApplicationClass.mSharedPreferencesManager.edit().putString("email",email).commit()//
                authService.checkEmail(email)//메소드 호출 따라서 엑티비에서 requset로 넘김
            }
        }
        binding.cvPhone.setOnClickListener {
            val intent = Intent(this, LoginPhoneActivity::class.java)
            startActivity(intent)
            finish()
        }
        questionEmail = binding.etEmail
        setContentView(binding.root)
    }

    fun checkEmail(): Boolean {
        var email = questionEmail.text.toString().trim() //공백제거
        val p = Pattern.matches(emailValidation, email) // 서로 패턴이 맞닝?
        return p
    }

    private fun ButtonOn() {
        binding.etEmail.addTextChangedListener(object : TextWatcher {
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

                //checkEmail()
                if (checkEmail()) {
                    binding.cvContinue.setBackgroundResource(com.peopleHere.people_here.R.drawable.making_tour_button_next)
                    checkContinue = true
                } else {//클릭 불가능 하게도 설정하기
                    binding.cvContinue.setBackgroundResource(com.peopleHere.people_here.R.drawable.making_tour_button_close)//설정 회색으로
                    checkContinue = false
                }
            }
        })
    }

    override fun onEmailAvailable(isAvailable: Boolean) {
        Log.d("CheckEmail","돌아가냐?")

    }
}