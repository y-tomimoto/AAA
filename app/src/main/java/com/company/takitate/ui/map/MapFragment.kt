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
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.GoogleMap
import com.google.android.libraries.maps.GoogleMap.OnMarkerClickListener
import com.google.android.libraries.maps.OnMapReadyCallback
import com.google.android.libraries.maps.SupportMapFragment
import com.google.android.libraries.maps.model.BitmapDescriptor
import com.google.android.libraries.maps.model.BitmapDescriptorFactory
import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.Marker
import com.google.android.libraries.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN


// このFragmentは、BottomNavigationView内で展開されている
class MapFragment : Fragment(), OnMapReadyCallback, OnMarkerClickListener,
    GoogleMap.OnCameraIdleListener {
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

    // 現在地を元にMarkerを管理したい
    private lateinit var mapCenterLatLng: LatLng

    // Markerのlist
    private var markerList: MutableList<Marker> = mutableListOf()

    // foreach用のshop
    private lateinit var markeredShop: Shop

    // Marker用に距離を測る
    private lateinit var distanceResults: FloatArray

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap ?: return // 外部の関数から、Mapを参照できるようScope変数として管理する
        // callbackを受け取って、現在値を用いた処理を実行するインスタンス
        val myLocationManager = MyLocationManager(requireActivity())
        // callbackを渡して、現在地を用いた一連の処理を実行する
        myLocationManager.getLocation(getLastLocationCallback)
        // onMarkerClickを有効にする
        map.setOnMarkerClickListener(this);
        // mapの移動を検知する
        map.setOnCameraIdleListener(this);
        // 初期値を管理する
        mapCenterLatLng = map.cameraPosition.target
    }

    // LocationManagerに渡すCallback。FusedLocationProvider内のTaskAPIでは戻り値を指定できないため。
    private val getLastLocationCallback = fun(location: Location) {
        // 現在地を取得
        val currentPosition = LatLng(location.latitude, location.longitude)

        // recruitAPIResponseViewModelが更新されたら、Markerを配置する
        recruitAPIResponseViewModel.recruitAPIResponse.observe(
            viewLifecycleOwner,
            Observer { restaurants ->
                // APIからのResponseで取得したShopリストに対してループ処理を実行する
                if (markerList.isEmpty()) {
                    // 初回はmarkerListが空なので、ResponseのShop全てをMarkerとしてaddする
                    for (response_shop in restaurants.results.shop) {
                        marker = map.addMarker(
                            MarkerOptions()
                                .position(LatLng(response_shop.lat, response_shop.lng))
                                .icon(getMarkerIconFromDrawable(circleDrawable))
                        )
                        // OnClick時に参照できるよう、markerにmetaDataを配置したい:  https://developers.google.com/maps/documentation/android-sdk/marker?hl=ja
                        marker.tag = response_shop
                        // 追加したmarkerを管理する
                        markerList.add(marker)
                    }
                } else {
                    //markerList内のMarkerに紐づくShopと、APIから取得したShopを照合する
                    restaurants.results.shop.forEach loop@{ response_shop ->
                        // Responseとして得たShopが、Markerとして配置されているShopと重複しないか確認する
                        markerList.forEach { markerInList ->
                            markeredShop = markerInList.tag as Shop // markerとして配置されているShop
                            // APIからのResponseのShopが、既にMarkerとして存在する場合は、処理をskipして次のloopに映る
                            if (response_shop.id == markeredShop.id) {
                                return@loop
                            }
                        }
                        // markerList内のどのShopと一致しなかった場合は、Markerをaddする
                        marker = map.addMarker(
                            MarkerOptions()
                                .position(LatLng(response_shop.lat, response_shop.lng))
                                .icon(getMarkerIconFromDrawable(circleDrawable))
                        )
                        // OnClick時に参照できるよう、markerにmetaDataを配置したい:  https://developers.google.com/maps/documentation/android-sdk/marker?hl=ja
                        marker.tag = response_shop
                        // 追加したmarkerを管理する
                        markerList.add(marker)
                    }
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

    override fun onCameraIdle() {
        val iterator =
            markerList.iterator() // https://stackoverflow.com/questions/50032000/how-to-avoid-concurrentmodificationexception-kotlin/50032174
        while (iterator.hasNext()) {
            // カメラが移動した直後、markerを確認する
            val item = iterator.next()
            markeredShop = item.tag as Shop
            val markerDistanceFromCenter =
                distance(map.cameraPosition.target, LatLng(markeredShop.lat, markeredShop.lng))
            if (300 < markerDistanceFromCenter) { // TODO: 300は定数として切り出す
                // markerが現在地よりも300m以上離れている場合は、markerを削除する
                item.remove()
                iterator.remove()
                markerList.remove(item)
                // markerList.remove(iterator)
            }
        }
        // Cameraの中心のLatLngを更新する
        mapCenterLatLng =
            map.cameraPosition.target // https://stackoverflow.com/questions/13904505/how-to-get-center-of-map-for-v2-android-maps
        // ViewModelをUpdateして、新たにmarkerを追加する
        recruitAPIResponseViewModel.loadRestaurants(mapCenterLatLng)

    }

    private fun distance(baseLatLng: LatLng, targetLatLng: LatLng): Float {
        distanceResults = FloatArray(1)
        Location.distanceBetween(
            baseLatLng.latitude,
            baseLatLng.longitude,
            targetLatLng.latitude,
            targetLatLng.longitude,
            distanceResults
        )
        return distanceResults[0] // 単位はメートル
    }
}
