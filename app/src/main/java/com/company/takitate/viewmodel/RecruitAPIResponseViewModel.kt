package com.company.takitate.viewmodel

import android.app.Application
import android.content.pm.PackageManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.company.takitate.data.repository.RecruitAPIRepository
import com.company.takitate.domain.entity.RecruitAPIResponse
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RecruitAPIResponseViewModel(application: Application) : AndroidViewModel(application) {

  // LiveData
  private val _recruitAPIResponse: MutableLiveData<RecruitAPIResponse> = MutableLiveData()
  val recruitAPIResponse: LiveData<RecruitAPIResponse> get() = _recruitAPIResponse

  // Repository TODO: 後々コンストラクタとしてRepositoryはDIする
  private var recruitAPIRepository: RecruitAPIRepository = RecruitAPIRepository()

  // TODO: nullを許容しないように修正。Repository内で宣言したいが、implementした関数ではデフォルト引数が推奨されていないのでここで宣言
  private val apiKey : String by lazy {
    application.packageManager.getApplicationInfo("com.company.takitate", PackageManager.GET_META_DATA).metaData.getString("com.company.takitate.RECRUIT_API_KEY")?:"invalid_api_key"
  }

  // 外部通信によって取得
  fun loadRestaurants( location: LatLng ) = viewModelScope.launch { // 通常のCoroutine Scopeを採用すると、ViewModelが排除されるタイミングでメモリリークする
    try {
      // ViewModelを更新する
      _recruitAPIResponse.postValue( // バックグラウンドスレッドでは、postでViewModelをupdate
        recruitAPIRepository.getRestaurantsByGeocode(
          key = apiKey, lat = location.latitude, lng = location.longitude, range = 1, format = "json", order=4 , count=100
        )
      )
    } catch (e: HttpException) {
        println(e)
    }
  }
}


