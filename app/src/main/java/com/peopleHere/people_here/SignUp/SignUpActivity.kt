package com.peopleHere.people_here.SignUp

import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.widget.TextView
import androidx.core.content.ContextCompat

import androidx.core.widget.addTextChangedListener
import com.google.android.material.internal.ViewUtils.dpToPx
import com.peopleHere.people_here.ApplicationClass
import com.peopleHere.people_here.ForCommunicate
import com.peopleHere.people_here.R
import com.peopleHere.people_here.Remote.AuthService
import com.peopleHere.people_here.databinding.ActivitySignUpBinding
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity(), ForCommunicate {
    private lateinit var binding: ActivitySignUpBinding
    var isclicked1: Int = 0
    var isclicked2: Int = 0
    lateinit var questionPass: TextView
    private var seeClicked: Boolean = false
    var checkfirstname: Boolean = false
    var checklastname: Boolean = false
    var checkBirth: Boolean = false
    var checkSex: Boolean = false
    var checkPass1: Boolean = false
    var checkPass2: Boolean = false
    var checkContinue: Boolean = false
    private val authService = AuthService(this)//여기로 넘김
    var sendingGender: String? = null
    var sendingbirth: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)

        binding.cvBirth.setOnClickListener {
            val birthFragment = BirthFragment(this)
            birthFragment.show(supportFragmentManager, birthFragment.tag)
        }

        binding.cvSex.setOnClickListener {
            val birthFragment = SelectSexFragment(this)
            birthFragment.show(supportFragmentManager, birthFragment.tag)//얘도 정보 어떻게 받아올지 생각하기
        }


        binding.etEmail.setText(ApplicationClass.mSharedPreferencesManager.getString("email", null))
        binding.tvAfterEmail.setText("이메일")
        questionPass = binding.etPassword
        ButtonOn()//비밀번호쪽
        CheckClicked()//마케팅쪽
        PasswordVaild()//비밀번호 ok?
        forFirstName()
        forLastName()
        observeFields()

        binding.cvContinue.setOnClickListener {
            if (checkContinue) {
                //TODO:예외 TOAST처리
                authService.signup(
                    binding.etEmail.text.toString(),
                    binding.etPassword.text.toString(),
                    binding.etFirstName.text.toString(),
                    binding.etLastName.text.toString(),
                    "1940-01-01",
                    sendingGender.toString(),
                )
                val intent = Intent(this, AlarmOkActivity::class.java)
                //TODO:서버에다가 회원가입 정보 다 넣기
                startActivity(intent)
            }
        }
        setContentView(binding.root)
    }

    private fun forNext() {
        if (checkfirstname && checklastname && checkBirth && checkSex && checkPass1 && checkPass2 && isclicked1 == 1) {
            binding.cvContinue.setBackgroundResource(com.peopleHere.people_here.R.drawable.making_tour_button_next)
            checkContinue = true
        } else {//클릭 불가능 하게도 설정하기
            binding.cvContinue.setBackgroundResource(com.peopleHere.people_here.R.drawable.making_tour_button_close)//설정 회색으로
            checkContinue = false
        }
    }

    private fun PasswordVaild() {
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
    }

    private fun CheckClicked() {
        binding.ivCheckNo1.setOnClickListener {
            if (isclicked1 == 0) {
                binding.ivCheckNo1.setImageResource(R.drawable.active_18)
                isclicked1 = 1//클릭되면 1로 변경
                forNext()
            } else {
                binding.ivCheckNo1.setImageResource(R.drawable.inactive_18)
                isclicked1 = 0//클릭다시하면 1로 변경
                forNext()
            }
        }
        binding.ivCheckNo2.setOnClickListener {
            if (isclicked2 == 0) {
                binding.ivCheckNo2.setImageResource(R.drawable.active_18)
                isclicked2 = 1//클릭되면 1로 변경
            } else {
                binding.ivCheckNo2.setImageResource(R.drawable.inactive_18)
                isclicked2 = 0//클릭다시하면 1로 변경
            }
        }
    }

    fun isPasswordValid(): Boolean {
        val pattern =
            "^(?=.*[+×÷=/_<>!@#\$%^&*'\":;,?`~\\|{}€£¥₩°•○●□■♤♡◇♧☆▪¤《》¡¿0123456789])[A-Za-z[0-9]+×÷=/_<>!@#\$%^&*'\":;,?`~\\|{}€£¥₩°•○●□■♤♡◇♧☆▪¤《》¡¿]{0,50}$"
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
                val orange5 = ContextCompat.getColor(this@SignUpActivity, R.color.Orange5)
                val gray5_5 = ContextCompat.getColor(this@SignUpActivity, R.color.Gray5_5)
                if (editable.isNotEmpty()) {
                    binding.tvAfterPassword.setText("비밀번호")
                } else {
                    binding.tvAfterPassword.setText("")
                }
                if (editable.length >= 8) {//비밀번호 8 자리 이상이면 체크되게 !!
                    binding.ivCheck1.setImageResource(R.drawable.checked)
                    binding.tvOver8.setTextColor(orange5)
                    checkPass1 = true
                    forNext()
                } else {
                    binding.ivCheck1.setImageResource(R.drawable.checked_no)
                    binding.tvOver8.setTextColor(gray5_5)
                    checkPass1 = false

                    binding.ivWrong.setImageResource(0)
                    val layoutParams = binding.ivWrong.layoutParams
                    layoutParams.width = dpToPx(0) // dpToPx() 메서드는 dp 값을 픽셀로 변환하는 함수입니다.
                    layoutParams.height = dpToPx(0)
                    binding.tvWrong.setText("")
                    binding.etPassword.setBackgroundResource(R.drawable.login_phone_et)

                }

                if (isPasswordValid()) {//ws
                    binding.ivCheck2.setImageResource(R.drawable.checked)
                    binding.tvChar.setTextColor(orange5)
                    checkPass2 = true
                    forNext()
                } else {
                    checkPass2 = false
                    binding.ivCheck2.setImageResource(R.drawable.checked_no)
                    binding.tvChar.setTextColor(gray5_5)
                }
            }
        })
    }

    private fun forFirstName() {
        binding.etFirstName.addTextChangedListener(object : TextWatcher {
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
                checkfirstname = editable.isNotEmpty()
                if (editable.isNotEmpty()) {
                    binding.tvAfterFirstName.setText("이름(예: 길동)")
                } else {
                    binding.tvAfterFirstName.setText("")
                }
            }
        })
    }


    private fun forLastName() {
        binding.etLastName.addTextChangedListener(object : TextWatcher {
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
                checklastname = editable.isNotEmpty()
                if (editable.isNotEmpty()) {
                    binding.tvAfterLastName.setText("성(예: 홍)")
                } else {
                    binding.tvAfterLastName.setText("")
                }
            }
        })
    }

    override fun callBackExample(year: Int, month: Int, day: Int) {
        val red3 = ContextCompat.getColor(binding.root.context, R.color.Red3)

        var userBirth = "$year/$month/$day"
        binding.tvBirth.text = userBirth
        val colorGray8 = ContextCompat.getColor(this, R.color.Gray8)
        binding.tvBirth.setTextColor(colorGray8)
        binding.tvAfterBirth.text = "생년월일(YYYY/MM/DD)"
        //2006보다 크면 안됩니다아아아아아ㅏ
        sendingbirth="$year-$month-$day"
        if (year > 2005) {
            checkBirth = false
            binding.cvBirth.strokeWidth =
                (1.5f * Resources.getSystem().displayMetrics.density).toInt()
            binding.cvBirth.setStrokeColor(red3)

        } else {
            checkBirth = true
            binding.cvBirth.strokeWidth =
                (0 * Resources.getSystem().displayMetrics.density).toInt()
            //줄무늬 사라지게
        }
    }


    override fun selected(sex: String) {
        binding.tvSex.text = sex
        sendingGender = when (sex) {
            "남성" -> {
                "MALE"
            }
            "여성" -> {
                "FEMALE"
            }
            else -> {
                "OTHER"
            }
        }
        val colorGray8 = ContextCompat.getColor(this, R.color.Gray8)
        binding.tvSex.setTextColor(colorGray8)
        binding.tvAfterSex.text = "성별"
        checkSex = true
    }

    private fun observeFields() {
        binding.etFirstName.addTextChangedListener { updateFields() }
        binding.etLastName.addTextChangedListener { updateFields() }
        binding.etPassword.addTextChangedListener { updateFields() }
        // 다른 필드에 대한 TextWatcher도 추가할 수 있습니다.
    }
    private fun updateFields() {
        checkfirstname = binding.etFirstName.text.isNotEmpty()
        checklastname = binding.etLastName.text.isNotEmpty()
        checkBirth = binding.tvBirth.text.isNotEmpty()
        checkSex = binding.tvSex.text.isNotEmpty()
        checkPass1 = binding.etPassword.text.length >= 8
        checkPass2 = isPasswordValid()
        forNext()
    }
    fun dpToPx(dp: Int): Int {
        val density = Resources.getSystem().displayMetrics.density
        return (dp * density).toInt()
    }

}