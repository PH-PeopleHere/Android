package com.peopleHere.people_here.MyTour

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.peopleHere.people_here.R
import com.peopleHere.people_here.databinding.ActivityMakingCourseSearchBinding

class MakingCourseSearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMakingCourseSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMakingCourseSearchBinding.inflate(layoutInflater)

        binding.ivMainSearchArrow.setOnClickListener {
            finish()
        }

        setContentView(binding.root)

        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, "AIzaSyB3WLa8PNBaMOK2OQm1U64Hb6RetEu-E1g")
        }

        val autocompleteFragment =
            (supportFragmentManager.findFragmentById(R.id.search_fragment) as AutocompleteSupportFragment)
                .setPlaceFields(listOf(Place.Field.LAT_LNG, Place.Field.NAME))

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                place.latLng?.let { latLng ->
                    binding.dummyText.text = latLng.toString()
                    Toast.makeText(
                        this@MakingCourseSearchActivity,
                        latLng.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onError(status: Status) {
                Log.e("PlacesError", status.statusMessage ?: "")
            }
        })
    }
}