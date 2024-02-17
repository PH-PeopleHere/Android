package com.peopleHere.people_here.Main

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
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.peopleHere.people_here.Data.MainSearchData
import com.peopleHere.people_here.R
import com.peopleHere.people_here.databinding.ActivityMainSearchBinding
import com.peopleHere.people_here.databinding.ItemMainSearchOnSearchingBinding

class MainSearchActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainSearchBinding
    private var mainSearchData: ArrayList<MainSearchData> = arrayListOf()
    private var mainSearchRecentAdapter: MainSearchRecentAdapter? = null
    private lateinit var mainSearchOnSearchingAdapter: MainSearchRecentAdapter
    private lateinit var placesClient: PlacesClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainSearchBinding.inflate(layoutInflater)

        // api 키 입력
        Places.initialize(applicationContext, "AIzaSyB3WLa8PNBaMOK2OQm1U64Hb6RetEu-E1g")
        // places client 생성
        placesClient = Places.createClient(this)

        initRecyclerView()

        binding.ivMainSearchArrow.setOnClickListener {
            initBackStack()
        }

        // 자동완성 텍스트 와쳐
        binding.etMainSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

//                binding.clMainSearchRecent.visibility = View.VISIBLE
//                binding.rvMainSearchOnSearching.visibility = View.GONE

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                binding.clMainSearchRecent.visibility = View.GONE
//                binding.rvMainSearchOnSearching.visibility = View.VISIBLE
                fetchPlaceSuggestions(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // 자동완성 검색 결과를 표시하는 RecyclerView 초기화
        initSearchOnSearchingRecyclerView()

        setContentView(binding.root)
    }

    private fun initSearchOnSearchingRecyclerView() {
        mainSearchOnSearchingAdapter = MainSearchRecentAdapter(arrayListOf())
        binding.rvMainSearchOnSearching.adapter = mainSearchOnSearchingAdapter
        binding.rvMainSearchOnSearching.layoutManager = LinearLayoutManager(this)
    }

    private fun fetchPlaceSuggestions(query: String) {
        val token = AutocompleteSessionToken.newInstance()
        val request = FindAutocompletePredictionsRequest.builder()
            .setTypeFilter(TypeFilter.ESTABLISHMENT)
            .setSessionToken(token)
            .setQuery(query)
            .build()

        placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
            val suggestions = response.autocompletePredictions.map { prediction ->
                MainSearchData(
                    R.drawable.img_example_place, prediction.getPrimaryText(null).toString(),
                    prediction.getSecondaryText(null).toString()
                )
            }

            mainSearchRecentAdapter?.updateData(ArrayList(suggestions))
            binding.cvMainSearchOnSearching.visibility =
                View.VISIBLE // 자동완성 결과 RecyclerView를 보이게 설정
        }.addOnFailureListener { exception ->
            Log.e("MainSearchActivity", "Place not found: ${exception.message}")
            // place 못찾을 때 오류
        }


    }

    private fun initBackStack() {
        finish()
    }


    private fun initRecyclerView() {
        mainSearchRecentAdapter = MainSearchRecentAdapter(mainSearchData)
        binding.rvMainSearchRecent.adapter = mainSearchRecentAdapter
        binding.rvMainSearchRecent.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL, false)
    }


}