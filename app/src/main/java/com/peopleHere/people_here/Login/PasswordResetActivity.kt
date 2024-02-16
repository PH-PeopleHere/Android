package com.peopleHere.people_here.Login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.peopleHere.people_here.R
import com.peopleHere.people_here.Remote.AuthService
import com.peopleHere.people_here.databinding.ActivityLoginEmailBinding
import com.peopleHere.people_here.databinding.ActivityPasswordResetBinding
import java.util.regex.Pattern

class PasswordResetActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPasswordResetBinding
    lateinit var questionPass: TextView
    private var checkContinue: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordResetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ButtonOn()
        questionPass = binding.etPassword

        binding.cvContinue.setOnClickListener {
            if (checkContinue) {
                val authService = AuthService(this)//여기로 넘김
                authService.changePassword(binding.etPassword.toString())
                //이거 넘겨서 바꾸기

            }
        }

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

                }
                if (editable.isNotEmpty()) {
                    binding.tvAfterPassword.setText("비밀번호")
                } else {
                    binding.tvAfterPassword.setText("")
                }

                val orange5 = ContextCompat.getColor(this@PasswordResetActivity, R.color.Orange5)
                val gray5_5 = ContextCompat.getColor(this@PasswordResetActivity, R.color.Gray5_5)
                if (editable.isNotEmpty()) {
                    binding.tvAfterPassword.setText("비밀번호")
                } else {
                    binding.tvAfterPassword.setText("")
                }
                if (editable.length >= 8) {//비밀번호 8 자리 이상이면 체크되게 !!
                    binding.ivCheck1.setImageResource(R.drawable.checked)
                    binding.tvOver8.setTextColor(orange5)
                } else {
                    binding.ivCheck1.setImageResource(R.drawable.checked_no)
                    binding.tvOver8.setTextColor(gray5_5)
                }

                if (isPasswordValid()) {//ws
                    binding.ivCheck2.setImageResource(R.drawable.checked)
                    binding.tvChar.setTextColor(orange5)

                } else {

                    binding.ivCheck2.setImageResource(R.drawable.checked_no)
                    binding.tvChar.setTextColor(gray5_5)
                }

            }
        })
    }

    fun isPasswordValid(): Boolean {
        val pattern =
            "^(?=.*[~!@#\$%^&*_+=`|\\{}:;\"'<>,.?/()-0123456789])[A-Za-z[0-9]~!@#\$%^&*_+=`|\\{}:;\"'<>,.?/()-]{0,50}$"
        val p = Pattern.matches(pattern, questionPass.text.toString().trim())
        return p
    }
}