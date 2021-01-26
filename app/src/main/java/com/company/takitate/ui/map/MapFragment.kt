package com.company.takitate.ui.map

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.room.Room
import com.company.takitate.R
import com.company.takitate.data.repository.driver.MyDatabase
import com.company.takitate.domain.entity.Shop
import com.company.takitate.domain.location.MyLocationManager
import com.company.takitate.viewmodel.RecruitAPIResponseShopViewModel
import com.company.takitate.viewmodel.RecruitAPIResponseViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN


// このFragmentは、BottomNavigationView内で展開されている
class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener{

  private lateinit var mapView: MapView
  private lateinit var marker: Marker // For tag: https://developers.google.com/maps/documentation/android-sdk/marker?hl=ja
  private lateinit var behavior: BottomSheetBehavior<*>
  private lateinit var activity: Activity

  // プロパティデリゲートでviewModelを取得する
  private val RecruitAPIResponseFocusedShopViewModel:RecruitAPIResponseShopViewModel by activityViewModels()
  private val recruitAPIResponseViewModel:RecruitAPIResponseViewModel by activityViewModels()

  // Roomインスタンスを用意
  private lateinit var db: MyDatabase

  // onMapReady外でgoogleMapインスタンスをcallしたい
  private var _googleMap: GoogleMap? = null


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    activity = requireActivity()
    // dbインスタンスを用意
    db = Room.databaseBuilder(
      activity,
      MyDatabase::class.java, "room-database"
    ).build()
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
    behavior.state = STATE_HIDDEN

    return view

  }

  override fun onMapReady(googleMap: GoogleMap?) {

    _googleMap = googleMap

    // LocationManagerに渡すCallback。FusedLocationProvider内のTaskAPIでは戻り値を指定できないため。
    val getLastLocationCallback = fun(location: Location) {

      val currentPosition = LatLng(location.latitude, location.longitude)

      // ViewModelの値を現在地によって更新する。
      recruitAPIResponseViewModel.loadRestaurants(currentPosition)

      val cu = CameraUpdateFactory.newLatLngZoom(
        currentPosition, 16f
      )

      // ここをloopすれば良さそう
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

    // mapをtapした際、下記からbottomSheetを表示
    behavior.state = STATE_COLLAPSED

    if (p0 != null) {
      RecruitAPIResponseFocusedShopViewModel.updateFocusedShop(p0.tag as Shop)
    }

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

  private fun getMarkerIconFromDrawable(drawable: Drawable): BitmapDescriptor? {
    val canvas = Canvas()
    val bitmap = Bitmap.createBitmap(
      drawable.intrinsicWidth,
      drawable.intrinsicHeight,
      Bitmap.Config.ARGB_8888
    )
    canvas.setBitmap(bitmap)
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    drawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    // Reffer to : https://stackoverflow.com/questions/18053156/set-image-from-drawable-as-marker-in-google-map-version-2
    val circleDrawable = resources.getDrawable(R.drawable.restaurant_marker_24)
    val markerIcon = getMarkerIconFromDrawable(circleDrawable)

    recruitAPIResponseViewModel.recruitAPIResponse.observe(
      viewLifecycleOwner,
      Observer { restaurants ->
        for (value in restaurants.results.shop) {
          marker = _googleMap!!.addMarker(
            MarkerOptions()
              .position(LatLng(value.lat, value.lng))
              .icon(markerIcon)
          )
          marker.tag = value // setTagで、内部のmarker objectにpassされる仕様になっていると思う
        }
      })
  }
}
