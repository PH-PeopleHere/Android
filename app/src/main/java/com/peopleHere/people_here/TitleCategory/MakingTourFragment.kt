package com.peopleHere.people_here.TitleCategory

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.peopleHere.people_here.MakingTour.HelpDiaLog
import com.peopleHere.people_here.R
import com.peopleHere.people_here.CourseContents.CourseContentsActivity
import com.peopleHere.people_here.databinding.FragmentMakingTourBinding
import com.peopleHere.people_here.Login.LoginPhoneActivity

class MakingTourFragment : Fragment() {
    var isclicked: Int = 0
    var cardNumCheck:Int=0
    private lateinit var binding: FragmentMakingTourBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMakingTourBinding.inflate(layoutInflater)

        //TODO: 눌렀을 때 색상 바뀌게(체크박스 사용하자)
        binding.cvFoodTour.setOnClickListener{
            val intent = Intent(requireActivity(), CourseContentsActivity::class.java)
            startActivity(intent)
        }
        binding.btnNext.setOnClickListener {
            val intent = Intent(requireActivity(), LoginPhoneActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }


    override fun onResume() {
        binding.ibCheck.setOnClickListener {//issue:다른 화면 나갔다 오면 check 사라지는데 이것도 구현해야하는지??
            if (isclicked == 0) {
                binding.ibCheck.setImageResource(R.drawable.active_18)
                isclicked = 1//클릭되면 1로 변경
            } else {
                binding.ibCheck.setImageResource(R.drawable.inactive_18)
                isclicked = 0//클릭다시하면 1로 변경
            }
        }
        tourSelect()








        binding.ibHelp.setOnClickListener {
            onDialogBtnClicked()
        }
        super.onResume()
    }

    private fun tourSelect() {
        binding.cvRoadTour.setOnClickListener {
            //누르면 카드 뷰 검정색&쉐이프 적용,텍스트 뷰 하얗게
            cardNumCheck++//하나씩 증가 시켜줘서 3개까지 ok
            binding.cvRoadTour.background = resources.getDrawable(R.drawable.making_tour_button_next)
            binding.tvRoadTour.setTextColor(Color.parseColor("#FFFFFF"))
            //하나라도 눌리면 background색 검정으로
            binding.btnNext.setBackgroundResource(R.drawable.making_tour_button_next)
        }
    }



    fun onDialogBtnClicked() {//눌리면 다이어로그 보이게
        val helpDialog = HelpDiaLog(requireContext())
        helpDialog.show()
    }
}