package com.peopleHere.people_here.MakingTour

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.peopleHere.people_here.Data.MakingTourAddListData
import com.peopleHere.people_here.R
import com.peopleHere.people_here.databinding.ActivityMakingTourAddListBinding
import com.peopleHere.people_here.databinding.DialogMakingTourAddListSequenceBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class MakingTourAddListActivity : AppCompatActivity() , OnMapReadyCallback{
    lateinit var binding: ActivityMakingTourAddListBinding
    private var googleMap: GoogleMap?= null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var addListAdapter: MakingTourAddListAdapter? = null
    private var addListData: ArrayList<MakingTourAddListData> = arrayListOf()
    private var isEditMode: Boolean = false
    private var sequenceDialog: Dialog? = null
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMakingTourAddListBinding.inflate(layoutInflater)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setContentView(binding.root)

        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // 데이터 받기
                val data: Intent? = result.data
                // 받은 데이터로 무엇인가를 함
                val newData = data?.getStringExtra("key") // 'key'는 받아올 데이터의 키
                // newData를 리스트에 추가
            }
        }

        initRecyclerView()
        initDummyData()
        setupItemTouchHelper()

        binding.btnMakingTourAddListEditSequence.setOnClickListener {
            initEditSequence()
        }

        binding.btnMakingTourAddListFinishSequence.setOnClickListener {
            initEditSequence()
        }

        binding.btnAddListNext.setOnClickListener {
            val intent = Intent(this@MakingTourAddListActivity, MakingTourTimeActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        if (isEditMode) {
            showExitEditDialog()
        } else {
            super.onBackPressed()
        }
    }

    private fun showExitEditDialog() {
        val dlgBinding = DialogMakingTourAddListSequenceBinding.inflate(layoutInflater)
        val dialogBuilder = AlertDialog.Builder(this)
        sequenceDialog = dialogBuilder.setView(dlgBinding.root).show()

        val density = resources.displayMetrics.density
        val widthPx = (272 * density).toInt()
        val heightPx = (160 * density).toInt()

        sequenceDialog?.window?.setLayout(widthPx, heightPx)
        sequenceDialog?.window?.setGravity(Gravity.CENTER)
        sequenceDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dlgBinding.btnKeepGoing.setOnClickListener {
            sequenceDialog?.dismiss()
        }

        dlgBinding.btnExit.setOnClickListener {
            sequenceDialog?.dismiss()
        }
    }


    private fun initEditSequence() {
        isEditMode = !isEditMode
        addListAdapter?.setEditMode(isEditMode)

        if (isEditMode){
            binding.btnMakingTourAddListEditSequence.isGone = true
            binding.btnMakingTourAddListFinishSequence.isVisible = true
        }else{
            binding.btnMakingTourAddListEditSequence.isVisible = true
            binding.btnMakingTourAddListFinishSequence.isGone = true
        }
    }

    private fun setupItemTouchHelper() {
        val itemTouchHelperCallback = object : ItemTouchHelper.Callback() {
            override fun isLongPressDragEnabled(): Boolean {
                return isEditMode  // edit mode 일때만 드래그
            }

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                return makeMovementFlags(dragFlags, 0)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition

                if (toPosition == addListData.size - 1) {
                    return false
                }

                addListAdapter?.onItemMove(fromPosition, toPosition)
                return true

            }


            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // 추후 swipe 기능 추가 되면 추가
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvMakingTourAddListPlace)
    }


    private fun initDummyData() {
        addListData.addAll(
            arrayListOf(
                MakingTourAddListData(1,R.drawable.img_example, "로니로티 건대점",0),
                MakingTourAddListData(2,R.drawable.img_example, "로니로티 홍대점인데 text가 길어질 때 어떻게 표시 되는지 확인해보는 데이터",1),
                MakingTourAddListData(3,R.drawable.img_example, "로니로티 숙대점",1),
                MakingTourAddListData(4,R.drawable.img_example, "로니로티 성대점",1),
                MakingTourAddListData(5,R.drawable.img_example, "로니로티 이대점",1),
                MakingTourAddListData(6,R.drawable.img_example, "로니로티 연대점",1),
                MakingTourAddListData(7,R.drawable.img_example, "로니로티 고대점",1),
//                MakingTourAddListData(8,R.drawable.img_example, "로니로티 중대점",1),
                MakingTourAddListData(8,R.drawable.img_example, "로니로티 중대점",2),
                )
        )
    }

    private fun initRecyclerView() {
        addListAdapter = MakingTourAddListAdapter(addListData)
        binding.rvMakingTourAddListPlace.adapter = addListAdapter
        binding.rvMakingTourAddListPlace.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL, false)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        val seoulCityHall = LatLng(37.5663, 126.9779)
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(seoulCityHall, 15f));
    }
}