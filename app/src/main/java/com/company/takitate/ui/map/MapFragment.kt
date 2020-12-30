package com.company.takitate.ui.map

import android.app.Activity
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
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
import com.google.android.material.bottomsheet.BottomSheetBehavior


class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

  private lateinit var mapView: MapView
  private lateinit var behavior: BottomSheetBehavior<*>
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

    behavior = BottomSheetBehavior.from(view.findViewById<NestedScrollView>(R.id.bottomSheet))

    return view

  }

  override fun onMapReady(googleMap: GoogleMap?) {

    // LocationManagerに渡すCallback。FusedLocationProvider内のTaskAPIでは戻り値を指定できないため。
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

      if (googleMap != null) {
        googleMap.moveCamera(cu)
        googleMap.setOnMarkerClickListener(this)
      }
    }

    val lm = MyLocationManager(context = activity.applicationContext, activity = activity)
    lm.getLocation(getLastLocationCallback)

  }

  override fun onMarkerClick(p0: Marker?): Boolean {
    // behavior.peekHeight = 250
    // behavior.halfExpandedRatio = 0.5F
    // behavior.expandedOffset = 250
    behavior.expandedOffset = 1
    // TODO: BottomSheetView内に店舗詳細を表示
    return true
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
