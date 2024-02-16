package com.peopleHere.people_here.MyTour

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.peopleHere.people_here.R
import com.peopleHere.people_here.databinding.ActivityMakingCourseSearchBinding

class MakingCourseSearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMakingCourseSearchBinding
    private lateinit var placesClient: PlacesClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMakingCourseSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}