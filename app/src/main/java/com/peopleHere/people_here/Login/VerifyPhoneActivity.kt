package com.peopleHere.people_here.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.peopleHere.people_here.SignUp.SignUpActivity
import com.peopleHere.people_here.databinding.ActivityVerifyPhoneBinding

import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.peopleHere.people_here.ApplicationClass
import java.util.concurrent.TimeUnit

class VerifyPhoneActivity : AppCompatActivity() {
    var verificationId = ""
    lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityVerifyPhoneBinding
    var checkContinue: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyPhoneBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        val receivedIntent = intent

        var phoneNumberVerification = ApplicationClass.mSharedPreferencesManager.getString("phoneNumber_verification",null)//+82향태
        var phoneNumber =  ApplicationClass.mSharedPreferencesManager.getString("phoneNumber",null)// +82형태로 들어옴

        ButtonOn()

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {//인증번호
                // 인증 코드 확인 완료
                Log.d("usercode", credential.smsCode.toString())
                Toast.makeText(this@VerifyPhoneActivity, "성공", Toast.LENGTH_SHORT).show()
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // 인증 실패 처리
                Toast.makeText(this@VerifyPhoneActivity, "실패", Toast.LENGTH_SHORT).show()
                Log.d("usercode_err", "인증 실패: ${e.message}")
            }

            override fun onCodeSent(
                verificationId: String,
                forceResendingToken: PhoneAuthProvider.ForceResendingToken
            ) {
                Toast.makeText(this@VerifyPhoneActivity, "ㅇㅋ", Toast.LENGTH_SHORT).show()


                this@VerifyPhoneActivity.verificationId = verificationId
                Log.d("verify", verificationId)
                //버튼 클릭하면 밑에 함수 호출되게
                //여기로 와지긴하는데 왜
            }

        }
    //TODO:만약 폰 인증 없으면 이렇게 하기
        //번호는 잘 와지는데 흠
        if (phoneNumberVerification != null) {
            val options = PhoneAuthOptions.newBuilder(auth)//firebase인증
                .setPhoneNumber(phoneNumberVerification.toString())       // 전화번호 설정
                .setTimeout(60L, TimeUnit.SECONDS) // 타임아웃 설정
                .setActivity(this@VerifyPhoneActivity)       // 현재 액티비티 설정
                .setCallbacks(callbacks)           // 인증 콜백 설정
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
        binding.btnContinue.setOnClickListener {
            if (checkContinue) {//6자리면 작동되게 하는?
                val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(
                    verificationId,
                    binding.etNumber.text.toString()
                ) //인자값을 뭘 줘야하지 그럼 흠
                if (credential.smsCode.toString() == binding.etNumber.toString()) {
                    Log.d("response_phoneNumber_check",phoneNumber.toString())
                    //TODO: 여기에 phoneNUMBER을 넘겨줘야 하는데 그냥 같은거 하나 더 만들어?????어케?그냥 받자 시발 에바다
                    val intent = Intent(this, SignUpActivity::class.java)
                    intent.putExtra("phone",phoneNumber)
                    startActivity(intent)
                }
                signInWithPhoneAuthCredential(credential)//여기부터 다시
            }
        }


    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->

                Log.d("verifyPhone", task.toString())

                if (task.isSuccessful) {
                    val intent = Intent(this, SignUpActivity::class.java)
                    startActivity(intent)
                    Log.d("verifyPhone_Success", task.toString())

                } else {
                    Log.d("verifyPhone_fail", task.toString())
                    //인증실패
                }
            }
    }

    private fun ButtonOn() {
        binding.etNumber.addTextChangedListener(object : TextWatcher {
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

                if (editable.length == 6) {//6자면 검정색
                    val phoneNumberWithHyphen = editable.toString() // 하이푼이 포함된 전화번호 문자열
                    binding.btnContinue.setBackgroundResource(com.peopleHere.people_here.R.drawable.making_tour_button_next)
                    checkContinue = true
                } else {//클릭 불가능 하게도 설정하기
                    binding.btnContinue.setBackgroundResource(com.peopleHere.people_here.R.drawable.making_tour_button_close)//설정 회색으로
                    checkContinue = false
                }
            }
        })
    }
}