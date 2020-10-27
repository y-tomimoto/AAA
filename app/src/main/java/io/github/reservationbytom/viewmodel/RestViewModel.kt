package io.github.reservationbytom.viewmodel

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import io.github.reservationbytom.BuildConfig
import io.github.reservationbytom.service.model.Rest
import io.github.reservationbytom.service.repository.GNaviRepository
import kotlinx.coroutines.launch

class RestViewModel(
    private val myApplication: Application
    // private val restID: String // コンストラクタとしてIDを受け取るかどうか検討
) : AndroidViewModel(myApplication) {
    private val repository = GNaviRepository.instance
    private val restLiveData: MutableLiveData<Rest> = MutableLiveData()
    private var rest = ObservableField<Rest>() // 監視対象に指定

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
                    restLiveData.postValue(restaurants.body()?.rest) // Observerが動作する
                }
            } catch (e: Exception) {
                Log.e("loadProject:Failed", e.stackTrace.toString())
            }
        }
    }

    fun setRest(restaurants: List<Rest>) {
        this.rest.set(restaurants)
    }

    //IDの(DI)依存性注入ファクトリ
    class Factory(private val application: Application) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RestViewModel(application) as T
        }
    }


}