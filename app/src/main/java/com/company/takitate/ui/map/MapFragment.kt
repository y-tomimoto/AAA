package com.company.takitate.ui.map

import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.room.Room
import com.company.takitate.BuildConfig
import com.company.takitate.R
import com.company.takitate.data.repository.RecruitAPIRepository
import com.company.takitate.data.repository.driver.MyDatabase
import com.company.takitate.domain.entity.Reviewer
import com.company.takitate.domain.location.MyLocationManager
import com.company.takitate.viewmodel.MapBottomSheetViewModel
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import retrofit2.HttpException

// このFragmentは、BottomNavigationView内で展開されている
class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener{

  private lateinit var mapView: MapView
  private lateinit var behavior: BottomSheetBehavior<*>
  private lateinit var activity: Activity

  // プロパティデリゲートでviewModelを取得する
  private val viewModel:MapBottomSheetViewModel by activityViewModels()

  // Roomインスタンスを用意
  private lateinit var db: MyDatabase

  private lateinit var recruitAPIRepository: RecruitAPIRepository

  private var api_key: String? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    activity = requireActivity()

    // dbインスタンスを用意
    db = Room.databaseBuilder(
      activity,
      MyDatabase::class.java, "room-database"
    ).build()
    recruitAPIRepository = RecruitAPIRepository()
    val coroutineScope = CoroutineScope(context = Dispatchers.Main)
    val app = context?.packageManager?.getApplicationInfo("com.company.takitate",PackageManager.GET_META_DATA)
    val bundle = app?.metaData
    if (bundle != null) {
        api_key = bundle.getString("com.company.takitate.RECRUIT_API_KEY")
      }
    fun getRecruitAPIResponseData() {
      coroutineScope.launch {
        try {
          val recruitAPIResponseData = api_key?.let {
            recruitAPIRepository.getRestaurantsByGeocode(
              key = it, lat = 34.67, lng = 135.52, range = 5, format = "json", order= 4
            )
          }
          println(recruitAPIResponseData)
          // 検索したGitHubリポジトリを画面に表示する
        } catch (e: HttpException) {
          // リクエスト失敗時の処理を行う
            println("failed")
            println(e)
            println(e.message())
        }
      }
    }
    println("=====")
    getRecruitAPIResponseData()
    println("=====")
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
    // launch は Builder
    // GlobalScope はコルーチンスコープ
    GlobalScope.launch {
        var reviewer = Reviewer(birthday = DateTime(),reviewer_id = 0,handle = "john doe")
        db.reviewerDao().insertReviewer(reviewer)
    }

    // mapをtapした際、下記からbottomSheetを表示
    behavior.state = STATE_COLLAPSED
    // ViewModelにp0に格納した情報を反映
    // viewModel.updateBottomSheetText("map taped")
    viewModel.updateButton("map taped")

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
