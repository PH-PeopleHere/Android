package com.peopleHere.people_here.MyTour

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.peopleHere.people_here.Data.MainSearchData
import com.peopleHere.people_here.Main.MainSearchRecentAdapter
import com.peopleHere.people_here.R
import com.peopleHere.people_here.databinding.ActivityMainSearchBinding

class MakingCourseSearchActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainSearchBinding //ActivityMainSearchBinding 타입의 binding 변수를 나중에 초기화할 것임을 나타내며, 이 변수를 통해 액티비티의 레이아웃에 있는 뷰들에 안전하게 접근
    private var mainSearchData: ArrayList<MainSearchData> =
        arrayListOf() //mainSearchData라는 이름의 비공개 가변 리스트를 생성하고, 이 리스트는 MainSearchData 타입의 객체들을 담을 수 있으며, 처음에는 비어있는 상태로 시작
    private var mainSearchRecentAdapter: MainSearchRecentAdapter? =
        null //mainSearchRecentAdapter라는 널이 될 수 있는 MainSearchRecentAdapter 타입의 가변 변수를 선언하고, 초기에는 널 값으로 설정하는 것을 의미합니다
    private lateinit var placesClient: PlacesClient //PlacesClient 타입의 placesClient 변수를 클래스 내부에서만 접근 가능하도록 선언하며, 실제 객체의 할당은 선언 시점이 아닌 나중에 이루어질 것임을 명시

    override fun onCreate(savedInstanceState: Bundle?) { //Activity가 생성될 때 호출되는 메소드를 재정의하는 코드이며, 이 메소드는 애플리케이션의 초기 설정과 리소스 준비 작업을 위해 중요
        super.onCreate(savedInstanceState) //오버라이드(재정의)된 onCreate 메소드 내에서 안드로이드 시스템에 의해 정의된 Activity의 기본 초기화 및 상태 복원 로직을 수행하도록 보장하는 중요한 단계입니다. 이는 Activity가 올바르게 초기화되고, 필요한 경우 이전 상태를 복원할 수 있도록 합니다.
        binding =
            ActivityMainSearchBinding.inflate(layoutInflater) //현재 액티비티의 레이아웃을 인플레이트(활성화)하여 뷰 객체를 생성하고, 이를 binding 변수에 할당하는 과정을 나타

        // api 키 입력
        Places.initialize(
            applicationContext,
            "AIzaSyB3WLa8PNBaMOK2OQm1U64Hb6RetEu-E1g"
        ) //Android 애플리케이션에서 Google Places API를 사용할 수 있도록 초기화하는 과정을 나타냅니다. 이를 통해 개발자는 사용자의 위치와 관련된 정보를 검색하고, 위치 기반의 기능을 앱에 통합
        // places client 생성
        placesClient =
            Places.createClient(this) //재 액티비티의 컨텍스트를 사용하여 Google Places API의 클라이언트 인스턴스를 생성하고, 이를 placesClient 변수에 할당하는 과정을 나타냅니다.

        initRecyclerView() //RecyclerView를 초기화하는 데 사용하는 사용자 정의 메서드

        binding.ivMainSearchArrow.setOnClickListener {   //ivMainSearchArrow 이미지 뷰(검색 화면의 화살표 이미지)에 클릭 이벤트 리스너를 설정하는 과정을 나타냄
            initBackStack() // Android에서 백 스택은 사용자가 애플리케이션 내에서 이전 화면으로 돌아갈 수 있게 해주는 메커니즘
        }

        // 자동완성 텍스트 와쳐
        binding.etMainSearch.addTextChangedListener(object :
            TextWatcher { // 사용자가 EditText에 입력하는 텍스트의 변경 사항을 감지하고, 이에 대응하여 정의된 작업을 실행하기 위한 리스너를 추가하는 과정을 나타냅니다.
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) { //사용자가 EditText에 입력하는 텍스트가 변경되기 전에 호출되는 메소드로, 텍스트 변경 이벤트를 처리하기 위한 초기 작업을 수행하는 데 사용
                binding.clMainSearchRecent.visibility = View.VISIBLE
                binding.rvMainSearchOnSearching.visibility = View.GONE
                // 코드는 사용자가 최근 검색한 내용을 보여주는 clMainSearchRecent 레이아웃을 화면에 보이게 하고, 검색 중에 보여주는 rvMainSearchOnSearching 레이아웃을 화면에서 숨기는 작업을 수행

            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) { //텍스트 필드 내에서 텍스트 변경 이벤트가 발생할 때마다 호출되며, 이를 통해 개발자는 텍스트가 변경될 때 필요한 로직을 실행
                // 사용자가 검색을 시작할 때 최근 검색 목록을 숨기고 검색 결과 목록을 보이게 하는 UI 변경을 수행
                binding.clMainSearchRecent.visibility = View.GONE
                binding.rvMainSearchOnSearching.visibility = View.VISIBLE
                fetchPlaceSuggestions(s.toString()) //용자의 입력을 문자열로 변환하여 fetchPlaceSuggestions 함수에 전달하고, 이 함수는 해당 입력에 기반한 장소 또는 검색어 제안을 가져오는 역할을 수행
            }

            override fun afterTextChanged(s: Editable?) {} //텍스트 필드에서 텍스트가 변경된 후에 호출되는 메소드를 정의합니다. 이 메소드는 텍스트 변경 이벤트에 대한 후처리 작업을 구현하는 데 사용될 수 있으며,
        })

        setContentView(binding.root) //뷰 바인딩을 사용하여 레이아웃을 인플레이트하고, 그 결과로 얻은 루트 뷰를 현재 액티비티의 컨텐츠 뷰로 설정하는 과정을 나타냅니다.
    } //onCreate 함수 종료

    private fun fetchPlaceSuggestions(query: String) { //사용자의 검색 쿼리를 매개변수로 받아 장소 제안을 검색하고 결과를 처리하는 사용자 정의 함수를 선언합니다.
        val token =
            AutocompleteSessionToken.newInstance() // Google Places API의 자동완성 기능을 사용하기 전에 새로운 세션 토큰을 생성하는 것
        val request =
            FindAutocompletePredictionsRequest.builder() //Google Places API의 자동완성 예측을 검색하기 위한 요청 객체를 구성하기 시작하는 과정을 나타냅니다. 이 과정을 통해 개발자는 사용자의 검색 쿼리에 기반한 장소 예측을 검색하는 요청을 정의하고 실행할 수 있습니다.
                .setTypeFilter(TypeFilter.ESTABLISHMENT) // 이 메서드는 요청의 타입 필터를 설정합니다. TypeFilter.ESTABLISHMENT는 검색 결과를 사업장(예: 가게, 사무실 등)으로 제한하겠다는 것을 나타냅니다.
                .setSessionToken(token) //이 메서드는 앞서 생성된 세션 토큰(token)을 요청에 설정합니다. 세션 토큰은 같은 사용자 세션 내의 자동완성 요청을 연관시키는 데 사용
                .setQuery(query) //이 메서드는 사용자가 입력한 검색 쿼리 문자열(query)을 요청에 설정합니다. 이 문자열은 자동완성 예측을 생성하는 데 사용
                .build() //마지막으로, build 메서드를 호출하여 모든 설정을 적용한 FindAutocompletePredictionsRequest 객체를 생성합니다. 이 객체는 이후에 Google Places API에 실제 요청을 보내는 데 사용됩니다.

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response -> // 사용자의 검색 쿼리에 대한 자동완성 예측을 Google Places API로부터 검색하고, 요청이 성공적으로 완료되었을 때 실행될 로직을 정의
                val suggestions =
                    response.autocompletePredictions.map { prediction -> //이 코드는 Google Places API로부터 받은 자동완성 예측 결과를 MainSearchData 객체의 리스트로 변환하여, 각 예측 결과에 대한 주요 텍스트, 보조 텍스트, 그리고 예제 이미지 리소스를 포함
                        MainSearchData(
                            R.drawable.img_example_place,
                            prediction.getPrimaryText(null).toString(),
                            prediction.getSecondaryText(null).toString()
                        )
                    }

                mainSearchRecentAdapter?.updateData(ArrayList(suggestions))
            }.addOnFailureListener { exception ->
            Log.e("MakingCourseSearchActivity", "Place not found: ${exception.message}")
            // place 못찾을 때 오류
        }
    }// fetchPlaceSuggestions 함수 종료

    private fun initBackStack() {
        finish()
    } //initBackStack 종료


    private fun initRecyclerView() { // 리사이클러뷰를 초기화하고 설정하는 로직을 포함하는 사용자 정의 함수를 선언
        mainSearchRecentAdapter =
            MainSearchRecentAdapter(mainSearchData) //리사이클러뷰에서 사용될 어댑터를 생성하고, 어댑터가 관리할 데이터 세트를 초기화하는 과정을 나타냅니다.
        binding.rvMainSearchRecent.adapter =
            mainSearchRecentAdapter //특정 리사이클러뷰에 대해 어댑터를 설정하여, 해당 어댑터가 관리하는 데이터를 리사이클러뷰에서 표시할 수 있도록 합니다.
        binding.rvMainSearchRecent.layoutManager =
            LinearLayoutManager( // rvMainSearchRecent 리사이클러뷰에 대해 LinearLayoutManager를 레이아웃 매니저로 설정하여, 리사이클러뷰의 항목들이 세로 방향으로 정방향으로 배치되도록 합니다.
                this,
                LinearLayoutManager.VERTICAL, false
            )
    } //initRecyclerView() 종료


}// MakingCourseSearchActivity class 종료