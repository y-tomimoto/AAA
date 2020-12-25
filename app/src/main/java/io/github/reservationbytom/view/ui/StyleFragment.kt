package io.github.reservationbytom.view.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.widget.NestedScrollView
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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import io.github.reservationbytom.R
import io.github.reservationbytom.service.model.Mock
import io.github.reservationbytom.service.repository.MockRepository


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
    // bottomSheetを取得する
    val bottomSheetView = view.findViewById<NestedScrollView>(R.id.bottomSheet)
    val m = BottomSheetBehavior.from(bottomSheetView)
    m.state = BottomSheetBehavior.STATE_EXPANDED;
    println(m.peekHeight)
    m.peekHeight = 300
    println(m.peekHeight)
    println("=====")
    println(m.state)
    return view
  }

  override fun onMapReady(googleMap: GoogleMap?) {
    googleMap?.apply {
      val sydney = LatLng(-33.852, 151.211)
      addMarker(
        MarkerOptions()
          .position(sydney)
          .title("Marker in Sydney")
      )
    }

    val marker1 = googleMap?.addMarker(
      MarkerOptions()
        .position(LatLng(35.7078999,139.6335999))
        .title("test")
    )
    if (marker1 != null) {
      marker1.tag = "うぉらーーーーー"
    }
    // 現在地を取得する
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
          // mapViewの初期位置をrenderする
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
          // 初期位置から周辺のRestaurantの口コミを取得し、それをmapのコメントに反映する
          val m = MockRepository()
          val restList = m.getRestaurants(latitude, longitude) // ここはcoroutineで実行したい
          // restListをmapiconとしてぶっ刺しまくる
          if (googleMap != null) {
            addMarkers(googleMap, restList)
          }
        }
      }
      .addOnFailureListener {
        println("failed")
      }
  }

  fun addMarkers(gMap: GoogleMap, restList: List<Mock>): Unit {
    var latlng: LatLng
    restList.forEach{ rest ->
      println(rest.id)
      println(rest.name)
      latlng = LatLng(rest.latitude, rest.longitude)
      gMap.addMarker(
        MarkerOptions()
          .position(latlng)
          .title(rest.name)
      )
    }
  }

  override fun onResume() {
    super.onResume()
    mapView.onResume()
    // Onresume で call する必要がある: https://qiita.com/nein37/items/32613e9acd9558566c5e
    // 位置情報の表示が終わったあとで、bottomsheetを展開する
    // CustomBottomSheetFragment().showDialog(fragmentManager = this.childFragmentManager)
    // CustomBottomSheetFragment().showDialog(fragmentManager = this.parentFragmentManager)
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
    println(p0?.tag)
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
