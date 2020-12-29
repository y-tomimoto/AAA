package com.company.takitate.ui.map

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.company.takitate.R
import com.company.takitate.domain.location.MyLocationManager
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

  private lateinit var mapView: MapView
  private lateinit var activity: Activity

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    activity = requireActivity()
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    val view = inflater.inflate(R.layout.fragment_map, container, false)
    mapView = view.findViewById<MapView>(R.id.mapview)
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);
    return view

  }

  override fun onMapReady(googleMap: GoogleMap?) {

    val getLastLocationCallback = fun(location: Location) {

      val currentPosition = LatLng(location.latitude, location.longitude)
      val cu = CameraUpdateFactory.newLatLngZoom(
        currentPosition, 16f
      )

      googleMap?.apply {
        addMarker(
          MarkerOptions()
            .position(currentPosition)
        )
      }

      googleMap?.moveCamera(cu)
    }

    val lm = MyLocationManager(context = activity.applicationContext, activity = activity)
    lm.getLocation(getLastLocationCallback)

  }

  override fun onMarkerClick(p0: Marker?): Boolean {
    TODO("Not yet implemented")
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
}
