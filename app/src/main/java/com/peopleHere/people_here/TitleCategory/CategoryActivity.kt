package com.peopleHere.people_here.TitleCategory

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.peopleHere.people_here.IntroduceCaution.IntroduceActivity
import com.peopleHere.people_here.MakingTour.HelpDiaLog
import com.peopleHere.people_here.R
import com.peopleHere.people_here.databinding.ActivityCategoryBinding
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.peopleHere.people_here.ApplicationClass
import com.peopleHere.people_here.Data.CategoryData
import com.peopleHere.people_here.Remote.AuthService


class CategoryActivity : AppCompatActivity() {
    var isclicked: Int = 0
    var cardNumCheck: Int = 0
    var time = 0
    var text : String ?= null
    private lateinit var binding: ActivityCategoryBinding
    private var categorylist: ArrayList<CategoryData> = arrayListOf()
    private var categoryadapter: CategoryAdapter? = null
    private lateinit var authService: AuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        //TODO: 눌렀을 때 색상 바뀌게(체크박스 사용하자)
        initDummyData()
        IbCheckReset()
        binding.btnBack.setOnClickListener {
            finish()
        }
        authService = AuthService(this)

        time = intent.getIntExtra("time",0)
        text = intent.getStringExtra("text")

        categoryadapter = CategoryAdapter(categorylist, isclicked)
        binding.rvCategory.adapter = categoryadapter
        binding.rvCategory.layoutManager =
            GridLayoutManager(this, 3)
        categoryadapter!!.setOnItemClickListener(object :
            CategoryAdapter.OnItemClickListener {
            override fun onItemClick(categoryData: CategoryData) {
                if(isclicked==1){
                    isclicked=0
                    binding.ibCheck.setImageResource(R.drawable.inactive_18)
                }
                updateButtonBackground(1)
            }

            override fun onItemNumChanged(num: Int) {
                updateButtonBackground(num)

            }

        })

        setContentView(binding.root)

    }

    private fun updateButtonBackground(clickedItemNum: Int) {
        IbCheckReset()
        if (isclicked == 1 || clickedItemNum > 0) { //isClicked는 어케함ㅋㅋ
            binding.btnNext.setBackgroundResource(R.drawable.add_list_next_button)
            binding.btnNext.setOnClickListener {

                authService.simpleProfileInfo()
                val intent = Intent(this, IntroduceActivity::class.java)//화면전환
                val categoryNames = arrayListOf<String>("거리 구경","쇼핑")
                intent.putExtra("time",time)
                intent.putExtra("text",text)
                startActivity(intent)
            }
        } else {
            binding.btnNext.setBackgroundResource(R.drawable.category_next_btn)

            setContentView(binding.root)
        }
    }

    override fun onResume() {

        super.onResume()
    }

    private fun IbCheckReset() {
        binding.ibCheck.setOnClickListener {//issue:다른 화면 나갔다 오면 check 사라지는데 이것도 구현해야하는지??
            if (isclicked == 0) {
                binding.ibCheck.setImageResource(R.drawable.active_18)
                isclicked = 1//클릭되면 1로 변경
                for (i in categorylist.indices) {
                    categorylist[i].isClicked = false
                }
                categoryadapter?.CountItem = 0
                categoryadapter?.notifyDataSetChanged()

                ApplicationClass.pcategoryNames?.clear()
                Log.d("APP_pactegory",ApplicationClass.pcategoryNames.toString())
            } else {
                binding.ibCheck.setImageResource(R.drawable.inactive_18)
                isclicked = 0//클릭다시하면 1로 변경
            }

            if (isclicked == 1) {
                binding.btnNext.setBackgroundResource(R.drawable.add_list_next_button)
                binding.btnNext.setOnClickListener {
                    //확인하기 위해 이렇게 해놨습니다
                }
            } else {
                binding.btnNext.setBackgroundResource(R.drawable.category_next_btn)
            }

        }
    }

        private fun initDummyData() {
            categorylist.addAll(
                arrayListOf(
                    CategoryData("거리 구경", R.drawable.person_walking, false),
                    CategoryData("맛집·카페", R.drawable.fork_and_knifeat, false),
                    CategoryData("쇼핑", R.drawable.shoppingbags, false),
                    CategoryData("관광명소", R.drawable.camera, false),
                    CategoryData("전시", R.drawable.framedpicture, false),
                    CategoryData("공연", R.drawable.mask, false),
                    CategoryData("역사와 유적", R.drawable.hanok, false),
                    CategoryData("전통문화", R.drawable.koreangat, false),
                    CategoryData("유원지", R.drawable.carouselhorse, false),
                    CategoryData("힐링", R.drawable.desert, false),
                    CategoryData("피크닉", R.drawable.basket_with_baguette_32, false),
                    CategoryData("오락", R.drawable.mike, false),
                    CategoryData("스포츠 액티비티", R.drawable.parachute, false),
                    CategoryData("파티", R.drawable.party, false),
                    CategoryData("펍앤바", R.drawable.cocktail, false)
                )
            )
        }

    }