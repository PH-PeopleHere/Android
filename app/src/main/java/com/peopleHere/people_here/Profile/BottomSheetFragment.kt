package com.peopleHere.people_here.Profile
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayout
import com.peopleHere.people_here.R
import com.peopleHere.people_here.Remote.UpcomingDateResponse
import com.peopleHere.people_here.databinding.DialogCourseManageBinding

class CalendarBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: DialogCourseManageBinding? = null
    private val tabList = arrayListOf("참여 차단", "참여 가능으로 설정")
    private lateinit var date : String
    private var month : Int = 0
    private var year : Int = 0
    private var share : Boolean = false
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DialogCourseManageBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        date = arguments?.getString("date") ?: ""
        month = arguments?.getInt("month") ?: 0
        year = arguments?.getInt("year") ?: 0

        binding.tvDialogMonth.text = month.toString()
        binding.tvDialogDay.text = date

        tabList.forEachIndexed { index, title ->
            val tab = binding.tlEnterDate.newTab()
            val tabView = LayoutInflater.from(context).inflate(R.layout.custom_text_view, null) as TextView
            tabView.text = title
            tab.customView = tabView
            binding.tlEnterDate.addTab(tab)

            // 초기 탭 선택 설정
            val firstTab = binding.tlEnterDate.getTabAt(0)
            binding.tlEnterDate.selectTab(firstTab)
            setTabColor(firstTab, true)
        }

        // TabLayout의 탭 선택 리스너 설정
        binding.tlEnterDate.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                setTabColor(tab, true)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                setTabColor(tab, false)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        binding.icEditTime.setOnClickListener {
            val intent = Intent(requireActivity(), SetTimeActivity()::class.java)
            intent.putExtra("date",date)
            intent.putExtra("month",month)
            intent.putExtra("year",year)
            startActivity(intent)
        }
    }

    private fun setTabColor(tab: TabLayout.Tab?, isSelected: Boolean) {
        binding.tvExistTime.text = tabList[tab!!.position]
        val tabView = (binding.tlEnterDate.getChildAt(0) as ViewGroup).getChildAt(tab.position) as View

        tab.customView?.let { view ->
            val textView = view as TextView
            if (isSelected) {
                tabView.background = ResourcesCompat.getDrawable(resources, R.drawable.rectangle_line_orange3_12, null)
                textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.Orange5))
            } else {
                textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.Gray5))
                tabView.background = null
                binding.tvExistTime.setTextColor(ContextCompat.getColor(requireContext(), R.color.Gray5))
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet!!)

        // BottomSheet의 최소 높이 설정
        bottomSheetBehavior.peekHeight = 300

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            // 슬라이드 동작 처리
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(date: String, month: Int, year: Int): CalendarBottomSheetFragment {
            return CalendarBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString("date", date)
                    putInt("month", month)
                    putInt("year", year)
                }
            }
        }
    }
}
