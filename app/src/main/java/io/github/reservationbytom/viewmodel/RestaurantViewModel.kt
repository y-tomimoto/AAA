package io.github.reservationbytom.viewmodel

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import io.github.reservationbytom.BuildConfig
import io.github.reservationbytom.service.model.Rest
import io.github.reservationbytom.service.repository.GNaviRepository
import kotlinx.coroutines.launch

class RestaurantViewModel(
    private val myApplication: Application
) : AndroidViewModel(myApplication) {
    private val repository = GNaviRepository.instance
    val restaurantLiveData: MutableLiveData<List<Rest>> = MutableLiveData()
    var restaurants = ObservableField<List<Rest>>() // 監視対象に指定

    init {
        loadRest()
    }

    private fun loadRest() {
        //viewModelScope->ViewModel.onCleared() のタイミングでキャンセルされる CoroutineScope
        viewModelScope.launch {
            try {
                val restaurants = repository.getRestaurants(
                    BuildConfig.GNAVI_API_KEY,
                    1,
                    33.3, // TODO: 外部から取得
                    33.3 // TODO: 外部から取得
                )
                if (restaurants.isSuccessful) {
                    restaurantLiveData.postValue(restaurants.body()?.rest)
                }
            } catch (e: Exception) {
                Log.e("loadProject:Failed", e.stackTrace.toString())
            }
        }
    }

    fun setRest(restaurants: List<Rest>) {
        this.restaurants.set(restaurants)
    }

    //IDの(DI)依存性注入ファクトリ
    class Factory(private val application: Application) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RestaurantViewModel(application) as T
        }
    }


}