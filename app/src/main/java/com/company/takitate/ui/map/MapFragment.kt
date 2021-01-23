package com.company.takitate.ui.map

import android.app.Activity
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
import com.company.takitate.domain.entity.Reviewer
import com.company.takitate.domain.entity.Shop
import com.company.takitate.domain.location.MyLocationManager
import com.company.takitate.viewmodel.RecruitAPIResponseShopViewModel
import com.company.takitate.viewmodel.RecruitAPIResponseViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.joda.time.DateTime

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
    // launch は Builder
    // GlobalScope はコルーチンスコープ
    GlobalScope.launch {
        var reviewer = Reviewer(birthday = DateTime(), reviewer_id = 0, handle = "john doe")
        db.reviewerDao().insertReviewer(reviewer)
    }

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

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    recruitAPIResponseViewModel.recruitAPIResponse.observe(
      viewLifecycleOwner,
      Observer { restaurants ->
        for (value in restaurants.results.shop) {
            marker = _googleMap!!.addMarker(
              MarkerOptions()
                .position(LatLng(value.lat, value.lng))
            )
          marker.tag = value // setTagで、内部のmarker objectにpassされる仕様になっていると思う
        }
      })
  }
}
