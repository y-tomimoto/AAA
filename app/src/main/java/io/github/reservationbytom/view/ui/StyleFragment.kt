package io.github.reservationbytom.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.github.reservationbytom.R


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FirstFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StyleFragment : Fragment(), OnMapReadyCallback {
  // TODO: Rename and change types of parameters
  private var param1: String? = null
  private var param2: String? = null
  private lateinit var mMap: GoogleMap
  private lateinit var mapView: MapView
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let {
      param1 = it.getString(ARG_PARAM1)
      param2 = it.getString(ARG_PARAM2)
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    val view = inflater.inflate(R.layout.fragment_style, container, false)
    mapView = view.findViewById<MapView>(R.id.mapview)
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);
    return inflater.inflate(R.layout.fragment_style, container, false)
  }

  companion object {
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FirstFragment.
     */
    // TODO: Rename and change types and number of parameters
    @JvmStatic
    fun newInstance(param1: String, param2: String) =
      FirstFragment().apply {
        arguments = Bundle().apply {
          putString(ARG_PARAM1, param1)
          putString(ARG_PARAM2, param2)
        }
      }
  }
  override fun onMapReady(googleMap: GoogleMap?) {
    println("fire!!!!!!!!!!!!!!!!")
    googleMap?.apply {
      val sydney = LatLng(-33.852, 151.211)
      addMarker(
        MarkerOptions()
          .position(sydney)
          .title("Marker in Sydney")
      )
    }
  }

  override fun onResume() {
    super.onResume()
    mapView.onResume()
  }

  override fun onPause() {
    super.onPause()
    mapView.onPause()
  }

  override fun onDestroy() {
    super.onDestroy()
    mapView.onDestroy()
  }

  override fun onLowMemory() {
    super.onLowMemory()
    mapView.onLowMemory()
  }

//  override fun onMapReady(googleMap: GoogleMap?) {
//    println("fire!!!!!!!!!!!!!!!!")
//    if (googleMap != null) {
//      mMap = googleMap
//    }
//
//    // Add a marker in Sydney and move the camera
//    val sydney = LatLng(-34.0, 151.0)
//    mMap.addMarker(
//      MarkerOptions()
//        .position(sydney)
//        .title("Marker in Sydney")
//    )
//    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
//
//    // Use fields to   define the data types to return.
//    val placeFields: List<Place.Field> = Arrays.asList(Place.Field.NAME)
//
//    // Use the builder to create a FindCurrentPlaceRequest.
//    val request = FindCurrentPlaceRequest.builder(placeFields).build()
//
//    // Call findCurrentPlace and handle the response (first check that the user has granted permission).
//
//    // Initialize Places.
//    Places.initialize(getApplicationContext(), BuildConfig.GOOGLE_MAP_API_KEY)
//
//    // Create a new Places client instance.
//    val placesClient = Places.createClient(getApplicationContext())
//
//    if (ContextCompat.checkSelfPermission(
//        getApplicationContext(),
//        ACCESS_FINE_LOCATION
//      ) == PackageManager.PERMISSION_GRANTED
//    ) {
//      placesClient.findCurrentPlace(request).addOnSuccessListener { response ->
//        for (placeLikelihood in response.getPlaceLikelihoods()) {
//          println(placeLikelihood.place.name)
//        }
//      }.addOnFailureListener { exception ->
//        if (exception is ApiException) {
//          val apiException = exception as ApiException
//          println(apiException.statusCode)
//        }
//      }
//    } else {
//      // A local method to request required permissions;
//      // See https://developer.android.com/training/permissions/requesting
//      // getLocationPermission()
//      println("getLocationPermission")
//
//    }
//
//  }
}
