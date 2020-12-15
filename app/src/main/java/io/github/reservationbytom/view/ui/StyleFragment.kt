package io.github.reservationbytom.view.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import io.github.reservationbytom.R


/**
 * A simple [Fragment] subclass.
 * Use the [FirstFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StyleFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

  private lateinit var mMap: GoogleMap
  private lateinit var mapView: MapView
  // 位置情報を取得できるクラス: https://developer.android.com/training/location/retrieve-current
  private lateinit var fusedLocationClient: FusedLocationProviderClient

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.fragment_style, container, false)
    mapView = view.findViewById<MapView>(R.id.mapview)
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);
    return view
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
    val activity_context = requireActivity().applicationContext
    fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity_context)
    if (ActivityCompat.checkSelfPermission(
        activity_context,
        Manifest.permission.ACCESS_FINE_LOCATION
      ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
        activity_context,
        Manifest.permission.ACCESS_COARSE_LOCATION
      ) != PackageManager.PERMISSION_GRANTED
    ) {
      // TODO: Consider calling
      //    ActivityCompat#requestPermissions
      // here to request the missing permissions, and then overriding
      //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
      //                                          int[] grantResults)
      // to handle the case where the user grants the permission. See the documentation
      // for ActivityCompat#requestPermissions for more details.
      return
    }
    fusedLocationClient.lastLocation
      .addOnSuccessListener { location: Location? ->
        // Got last known location. In some rare situations this can be null.
        println(location)
        if (location != null) {
          val latitude = location.latitude
          val longitude = location.longitude
          googleMap?.apply {
            val test = LatLng(latitude, longitude)
            addMarker(
              MarkerOptions()
                .position(test)
                .title("Marker in Japan")
            )
          }
          val cu = CameraUpdateFactory.newLatLngZoom(
            LatLng(latitude, longitude), 16f
          )
          if (googleMap != null) {
            googleMap.moveCamera(cu)
            googleMap.setOnMarkerClickListener(this)
          }
        }
      }
      .addOnFailureListener {
        println("failed")
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

  override fun onMarkerClick(p0: Marker?): Boolean {
    println(p0?.title)
    println("dada")
    return true
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
//  }
}
