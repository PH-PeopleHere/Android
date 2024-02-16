package com.peopleHere.people_here.Login
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.peopleHere.people_here.databinding.ActivityLoginPhoneBinding

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class LoginPhoneActivity : AppCompatActivity() {
    // 구글로그인 result 상수
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var binding: ActivityLoginPhoneBinding
    private var checkContinue:Boolean=false
    lateinit var phoneNumberWithoutHyphen:String
    lateinit var phoneNumber:String
    var verificationId =""
    private val sms: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPhoneBinding.inflate(layoutInflater)
        setContentView(binding.root)





        //자동 하이푼 로직
        val editText = binding.etPhoneNumber
        editText.addTextChangedListener(PhoneNumberFormattingTextWatcher())
        ButtonOn()

        binding.cvEmail.setOnClickListener {

            val intent = Intent(this, LoginEmailActivity::class.java)
            startActivity(intent)

        }
        binding.cvContinue.setOnClickListener {
            if (checkContinue) {
                // 13글자인 경우 다음 activity로 이동하는 코드 작성
                // 예시로 다음 activity를 시작하는 코드를 작성합니다.
                phoneNumberWithoutHyphen = phoneNumberWithoutHyphen.substring(1) // 맨 앞의 "0" 제거
                phoneNumber= "+82$phoneNumberWithoutHyphen"

                val intent = Intent(this, VerifyPhoneActivity::class.java)
                intent.putExtra("phone_number",phoneNumber)//다음에서 체크하기 위해
                startActivity(intent)
/*
                Log.d("userphone",phoneNumber)
                val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {//인증번호
                        // 인증 코드 확인 완료
                        Log.d("usercode",credential.smsCode.toString())
                        Toast.makeText(this@LoginPhoneActivity, "성공", Toast.LENGTH_SHORT).show()
                    }
                    override fun onVerificationFailed(e: FirebaseException) {
                        // 인증 실패 처리
                        Toast.makeText(this@LoginPhoneActivity, "실패", Toast.LENGTH_SHORT).show()
                        Log.d("usercode_err", "인증 실패: ${e.message}")
                    }

                    override fun onCodeSent(verificationId: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
                        Toast.makeText(this@LoginPhoneActivity, "ㅇㅋ", Toast.LENGTH_SHORT).show()
                        Log.d("usercode_Codesent", "인증 ㅇㅋ")
                        // 사용자에게 코드 입력 UI를 표시하고, 입력받은 코드를 저장

                        // 저장된 인증 ID와 입력받은 코드로 자격 증명(credential) 생성
                        val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, "123456")
                        this@LoginPhoneActivity.verificationId = verificationId
                        Log.d("usercode_num", verificationId.toString())
                        startActivity(intent)
                        //여기로 와지긴하는데 왜
                    }

                }

                val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())//firebase인증
                    .setPhoneNumber(phoneNumber)       // 전화번호 설정
                    .setTimeout(60L, TimeUnit.SECONDS) // 타임아웃 설정
                    .setActivity(this@LoginPhoneActivity)       // 현재 액티비티 설정
                    .setCallbacks(callbacks)           // 인증 콜백 설정
                    .build()

                PhoneAuthProvider.verifyPhoneNumber(options)*/
                //아 여기서 startactivity 하면 callback 오기전에 바로 실행돼서 안 됐던 것 같다 ㅇㅋㅇㅋ
                //
                //이러면 sms 인증해야하는데 왜 어쨰서
            }
        }








        //TODO:Google 로그인
        /*auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(com.example.people_here.R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        binding.cvGmail.setOnClickListener {
            signInGoogle()
        }
*/
    }

  /*  private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)

    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResults(task)
            }
        }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if (account != null) {
                updateUI(account)
            }
        } else {
            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                val intent: Intent = Intent(this, SignInActivity::class.java)//예시로 해놓은것이니 고쳐야함
                intent.putExtra("email", account.email)
                Toast.makeText(this, "성공", Toast.LENGTH_SHORT).show()
                startActivity(intent)
            } else {
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
*/
    private fun ButtonOn() {
        binding.etPhoneNumber.addTextChangedListener(object : TextWatcher {
            var maxtext=""
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

                if (editable.length == 13) {//한글자 이상이면 검정색
                    val phoneNumberWithHyphen = editable.toString() // 하이푼이 포함된 전화번호 문자열
                    phoneNumberWithoutHyphen = phoneNumberWithHyphen.replace("-", "") // 하이푼 제거
                    binding.cvContinue.setBackgroundResource(com.peopleHere.people_here.R.drawable.making_tour_button_next)
                    checkContinue=true

                } else {//클릭 불가능 하게도 설정하기
                    binding.cvContinue.setBackgroundResource(com.peopleHere.people_here.R.drawable.making_tour_button_close)//설정 회색으로
                    checkContinue=false
                }
            }
        })
    }

}