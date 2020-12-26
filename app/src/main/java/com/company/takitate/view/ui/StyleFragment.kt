package com.company.takitate.view.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.company.takitate.R
import com.company.takitate.databinding.FragmentStyleBinding
import com.company.takitate.service.model.Mock
import com.company.takitate.service.repository.MockRepository
import com.company.takitate.viewmodel.BottomSheetViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [FirstFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StyleFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
  // GoogleMapと、店のリストを表示するFragmentになる。

  private lateinit var viewModel: BottomSheetViewModel
  private lateinit var binding:FragmentStyleBinding
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

    // bottomSheetにViewModelを付与
    viewModel = ViewModelProvider(this).get(BottomSheetViewModel::class.java) // AndroidViewModelを継承した場合、インスタンス化はViewModelProvider経由で行う
    println(inflater)
    println(R.layout.fragment_style)
    println(container)
    binding = DataBindingUtil.inflate(inflater,R.layout.fragment_style,container,false) // bindingクラスを生成
    binding.vm = viewModel // viewModelをsetしている
    binding.lifecycleOwner = this // LiveDataが値の更新を検知するように設定する

//    viewModel.bottomSheetText.observe(viewLifecycleOwner, Observer {
//      println("updated")
//    })
//    viewModel.updateBottomSheetText("test")
//    viewModel.updateBottomSheetText("dadadadada")
//    println(viewModel.bottomSheetText.value)

    // MapView
    mapView = binding.mapview
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);

    // BottomSheet
    val bottomSheetView = binding.bottomSheet
    val m = BottomSheetBehavior.from(bottomSheetView)
    m.state = BottomSheetBehavior.STATE_EXPANDED;
    m.peekHeight = 300
    return binding.root

//    val view = inflater.inflate(R.layout.fragment_style, container, false)
//    mapView = view.findViewById<MapView>(R.id.mapview)
//    mapView.onCreate(savedInstanceState);
//    mapView.getMapAsync(this);
//    val bottomSheetView = view.findViewById<NestedScrollView>(R.id.bottomSheet)
//    val m = BottomSheetBehavior.from(bottomSheetView)
//    m.state = BottomSheetBehavior.STATE_EXPANDED;
//    println(m.peekHeight)
//    m.peekHeight = 300
//    println(m.peekHeight)
//    println("=====")
//    println(m.state)
//    return view

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
      println("権限ないよ")
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
        println(location) // なぜかここがnullだぜ。
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
        } else {
          // 一度電源切れたらこうなるみたい: https://qiita.com/outerlet/items/78941b0b352c7003c01f
          println("nullだぜ。だから自分で取得してやるぜ。")

          // requestを生成
          val request = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(500)
            .setFastestInterval(300)

          // 生成したrequestを投げる
          fusedLocationClient
            .requestLocationUpdates(
              request,
              object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                  // (b)LocationUpdates で得た時の処理
                  // 現在地だけ欲しいので、1回取得したらすぐに外す
                  fusedLocationClient.removeLocationUpdates(this)
                }
              },
              null
            )
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
    println("fire") // なぜか発火しない。
    p0?.title?.let { viewModel.updateButton(it) }
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
