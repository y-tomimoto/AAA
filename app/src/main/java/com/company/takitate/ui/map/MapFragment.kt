package com.company.takitate.ui.map

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.company.takitate.R
import com.company.takitate.domain.entity.Shop
import com.company.takitate.domain.location.MyLocationManager
import com.company.takitate.viewmodel.RecruitAPIResponseShopViewModel
import com.company.takitate.viewmodel.RecruitAPIResponseViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.GoogleMap
import com.google.android.libraries.maps.OnMapReadyCallback
import com.google.android.libraries.maps.SupportMapFragment
import com.google.android.libraries.maps.GoogleMap.OnMarkerClickListener
import com.google.android.libraries.maps.model.*


// このFragmentは、BottomNavigationView内で展開されている
class MapFragment : Fragment(), OnMapReadyCallback, OnMarkerClickListener {
  // OnMapReady等以外からmapインスタンスを参照するためのScope変数
  private lateinit var map: GoogleMap

  // プロパティデリゲートでviewModelを取得する
  private val recruitAPIResponseFocusedShopViewModel: RecruitAPIResponseShopViewModel by activityViewModels()
  private val recruitAPIResponseViewModel: RecruitAPIResponseViewModel by activityViewModels()
  private val ZOOM_LEVEL = 16f

  // MarkerIcon
  private lateinit var circleDrawable: Drawable

  // For tag: https://developers.google.com/maps/documentation/android-sdk/marker?hl=ja
  private lateinit var marker: Marker

  // OnMarkerClickから参照するためにScope変数として宣言
  private lateinit var behavior: BottomSheetBehavior<*>

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    super.onCreate(savedInstanceState)
    val view = inflater.inflate(R.layout.fragment_map, container, false)
    // MapFragmentを配置: https://github.com/googlemaps/android-samples/blob/master/ApiDemos/kotlin/app/src/v3/java/com/example/kotlindemos/MyLocationDemoActivity.kt
    val mapFragment: SupportMapFragment? =
      childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment // 公式のrepoを参考にし、supportMapFragment.findFragmentById とするとError https://stackoverflow.com/questions/36618154/android-supportmapfragment-inside-fragment
    mapFragment?.getMapAsync(this)
    // BottomSheetを配置
    behavior = BottomSheetBehavior.from(view.findViewById<NestedScrollView>(R.id.bottomSheet))
    behavior.state = STATE_HIDDEN
    // Reffer to : https://stackoverflow.com/questions/18053156/set-image-from-drawable-as-marker-in-google-map-version-2
    circleDrawable = resources.getDrawable(R.drawable.restaurant_marker_36)
    circleDrawable.setTint(ContextCompat.getColor(requireActivity(), R.color.primary))
    return view
  }

  override fun onMapReady(googleMap: GoogleMap?) {
    map = googleMap ?: return // 外部の関数から、Mapを参照できるようScope変数として管理する
    // callbackを受け取って、現在値を用いた処理を実行するインスタンス
    val myLocationManager = MyLocationManager(requireActivity())
    // callbackを渡して、現在地を用いた一連の処理を実行する
    myLocationManager.getLocation(getLastLocationCallback)
    // onMarkerClickを有効にする
    map.setOnMarkerClickListener(this);
  }

  // LocationManagerに渡すCallback。FusedLocationProvider内のTaskAPIでは戻り値を指定できないため。
  private val getLastLocationCallback = fun(location: Location) {
    // 現在地を取得
    val currentPosition = LatLng(location.latitude, location.longitude)

    // recruitAPIResponseViewModelが更新されたら、Markerを配置する
    recruitAPIResponseViewModel.recruitAPIResponse.observe(
      viewLifecycleOwner,
      Observer { restaurants ->
        for (value in restaurants.results.shop) {
          marker = map.addMarker(
            MarkerOptions()
              .position(LatLng(value.lat, value.lng))
              .icon(getMarkerIconFromDrawable(circleDrawable))
          )
          // OnClick時に参照できるよう、markerにmetaDataを配置したい:  https://developers.google.com/maps/documentation/android-sdk/marker?hl=ja
          marker.tag = value
        }
      })

    // recruitAPIResponseViewModelの値を現在地によって更新する。
    recruitAPIResponseViewModel.loadRestaurants(currentPosition)

    // 現在地を元に画角を調整・markerを追加
    with(map) {
      moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, ZOOM_LEVEL))
      addMarker(MarkerOptions().position(currentPosition))
    }
  }

  // 参考: https://github.com/googlemaps/android-samples/blob/master/ApiDemos/kotlin/app/src/v3/java/com/example/kotlindemos/MarkerDemoActivity.kt
  override fun onMarkerClick(marker: Marker?): Boolean {
    marker ?: return false
    marker.tag ?: return false // 現在地を示すmarkerをtapしたときは以下の処理を実行しない
    // FocusされているShopを管理する。ここで管理したShopがReview記載対象になる。
    recruitAPIResponseFocusedShopViewModel.updateFocusedShop(marker.tag as Shop)
    // mapをtapした際、下記からbottomSheetを表示
    behavior.state = STATE_COLLAPSED
    return false
  }

  // Markerアイコンを変更する : https://stackoverflow.com/questions/18053156/set-image-from-drawable-as-marker-in-google-map-version-2
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
}
