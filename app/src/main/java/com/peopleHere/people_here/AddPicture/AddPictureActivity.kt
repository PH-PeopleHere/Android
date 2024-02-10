package com.peopleHere.people_here.AddPicture

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.peopleHere.people_here.R
import com.peopleHere.people_here.AddPicture.PictureDB.PictureDB
import com.peopleHere.people_here.AddPicture.PictureDB.PictureEntity
import com.peopleHere.people_here.Data.AddPictureData
import com.peopleHere.people_here.TitleCategory.TitleActivity
import com.peopleHere.people_here.databinding.ActivityAddPictureBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AddPictureActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPictureBinding
    private var addPictureAdapter: AddPictureAdapter? = null
    var pictureDB: PictureDB? = null//없으면 null 로
    var uriString: String? = null
    val picturelist = arrayListOf<AddPictureData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddPictureBinding.inflate(layoutInflater)
        CreateTextView()//text는 딱 oncreate 처음 한 번에만 보이면 된다
        setupItemTouchHelper()

        val resourceUri_1 = Uri.parse("android.resource://$packageName/${R.drawable.photoaddition}")
        val resourceUri_2 =
            Uri.parse("android.resource://$packageName/${R.drawable.photoaddition2}")
        val resourceUri_full =
            Uri.parse("android.resource://$packageName/${R.drawable.photoaddition3}")


        pictureDB = PictureDB.getInstance(this) //인스턴스 생성

        //Data 받아와서 추가하는 부분
        val receivedIntent = intent
        if (receivedIntent != null) {
            // ArrayList 크기만큼 반복
            var i = 0
            while (receivedIntent.hasExtra("uri_$i")) {
                uriString = receivedIntent.getStringExtra("uri_$i").toString()
                Log.d("test1", uriString!!)
                picturelist.add(AddPictureData(uriString!!, 1))
                i++
            }
        }

        //얘는 리스트 에 있는걸 DB에 넣음


        //TODO: 이 부분을 그럼 어떻게 해야하지??? 문제는 DB가 비동기로 처리돼서 이걸 알 수가 없는데 흠
        
        if (picturelist.isEmpty()) {
            picturelist.add(AddPictureData(resourceUri_1.toString(), 0))
            Log.d("which", "1")
        } else {
            Log.d("which", picturelist.toString())

            picturelist.add(AddPictureData(resourceUri_2.toString(), 0))
            binding.btnNext.setBackgroundResource(R.drawable.add_list_next_button)
        }


        Log.d("ghkrdls3", picturelist.size.toString())


        addPictureAdapter = AddPictureAdapter(picturelist, this)
        binding.rvPictures.adapter = addPictureAdapter
        binding.rvPictures.layoutManager =
            GridLayoutManager(this, 2)
        addPictureAdapter!!.setOnItemClickListener(object :
            AddPictureAdapter.OnItemClickListener {
            override fun onItemClick(picturelist: AddPictureData) {
            }
        })
        binding.btnNext.setOnClickListener {
            val intent = Intent(this, TitleActivity::class.java)
            startActivity(intent)
            //finish()
        }
        addPictureAdapter!!.notifyItemInserted(picturelist.size)

        binding.btnAddPicture.setOnClickListener {
            val bottomsheet = LocationChooseFragment()
            bottomsheet.show(supportFragmentManager, bottomsheet.tag)
        }

        //얘는 RV notify해줘야 리사이클러뷰가 적용해서 할 듯 !!
        lifecycleScope.launch {
            // 백그라운드에서 실행되어야 하는 코드
            var pictures = withContext(Dispatchers.IO) {
                pictureDB!!.getPictureDao().getPicture()
            }
            // UI 업데이트는 Main 문맥에서 실행
            //백그라운드에서 가져와서 띄우는데 왜 두 번째에 나갔다 오면 이게 시행이 안되냐?
            withContext(Dispatchers.IO) {
                picturelist.addAll(pictures.map { pictureEntity ->
                    AddPictureData(
                        imageUrl = pictureEntity.pictureUri,
                        itemType = pictureEntity.itemType
                    )
                })
                //이걸 넣어야 가능하다
                addPictureAdapter!!.notifyDataSetChanged()
                Log.d("qwer",picturelist.size.toString())
                //개수가 저절로 줄어드는데 어쨰서죠??
                Log.d("dbqwer",pictures.size.toString())

            }
        }
        //DB에다가 추가 하는것 이건 그냥 써도 ㄱㅊ할듯
        uriString?.let {
            lifecycleScope.launch {
                // 백그라운드에서 실행되어야 하는 코드
                // UI 업데이트는 Main 문맥에서 실행
                withContext(Dispatchers.IO) {
                    pictureDB!!.getPictureDao().addPicture(PictureEntity(it, "건대 츠케멘", "Jungan", 1))
                }
            }
        }

        setContentView(binding.root)
    }


    override fun onStart() {//아마 fragment에서 dissmiss하면 여기로 와질듯?
        super.onStart()


        //실습보고 다시


    }

    override fun onResume() {
        super.onResume()

    }

    private fun CreateTextView() {
        //실험삼아 하나만 해 봅시당
        //arrayof로 해서 배열 만들고, 받아 오는대로 여기에 텍스트 추가해서 하면 될 듯?
        //마찬가지로 image하는데 이거 사진추가가 계속 위치가 바뀌는거 어떻게하는지 ㅋㅋ

        //결국, RoomDB로 받아서 거기에 imgaelist 추가추가 해서 불러오게 해야할 듯 지금은 서버 연동 전 이니까 shared로 해볼까

        val tv2 = TextView(this)
        tv2.setText("건대 화양식당")
        //for문으로 position에 따라 위치 수정해주면 될 듯 하다
        tv2.textSize = 14f
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
        )

        tv2.setTextColor(Color.parseColor("#9FA4A9"))

        binding.layoutLocation.addView(tv2)

    }

    private fun CreateImageView() {
        //TODO:arrayof로 해서 배열 만들고, 받아 오는대로 여기에 텍스트 추가해서 하면 될 듯?
        //마찬가지로 image하는데 이거 사진추가가 계속 위치가 바뀌는거 어떻게하는지 ㅋㅋ
        //여기서 image의 uri를 받아와서 iv로 감싸게 해서 텍스트처럼 추가하기

        val img1 = ImageView(this)
        //val imgString = ApplicationClass.mSharedPreferencesManager.getString("image", null)
        //val imgUri = Uri.parse(imgString)
        //Log.d("imgtest", imgUri.toString())
        //img1.setImageURI(imgUri)//이미지 띄웠으니 크기
        //나중에 roomdb로 바꿔서 하기


        val layoutParams = GridLayout.LayoutParams().apply {
            width = resources.getDimensionPixelSize(R.dimen.image_width)
            height = resources.getDimensionPixelSize(R.dimen.image_width) // 필요에 따라 높이도 설정
            // 그리드에서의 위치 설정 (rowSpec, colSpec)
            //추가되는거 무조건 0,0에다가

            //자동으로 밀리는게 아니라 겹쳐지네..?
            //근데 반복문 써서 위치 옮기면 터질텐디

            rowSpec = GridLayout.spec(0)
            columnSpec = GridLayout.spec(0)
        }

//        binding.layoutAddPic.addView(img1, layoutParams)

    }

    private fun setupItemTouchHelper() {
        val itemTouchHelperCallback = object : ItemTouchHelper.Callback() {
            //항상 드래그 가능
            override fun isLongPressDragEnabled(): Boolean {
                return true
            }

            //상하좌우
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val dragFlags =
                    ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                return makeMovementFlags(dragFlags, 0)
            }

            //포지션 바뀌게
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition

                if (toPosition == picturelist.size - 1) {
                    return false
                }
                addPictureAdapter?.onItemMove(fromPosition, toPosition)
                return true

            }


            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // 추후 swipe 기능 추가 되면 추가
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvPictures)
    }
}