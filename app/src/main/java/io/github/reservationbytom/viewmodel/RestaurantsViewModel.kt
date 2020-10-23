package io.github.reservationbytom.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import io.github.reservationbytom.BuildConfig
import io.github.reservationbytom.service.model.GNaviResponse
import io.github.reservationbytom.service.repository.GNaviRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class RestaurantsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = GNaviRepository.instance

    // postValue で LiveData を受け取る
    private var projectListLiveData: MutableLiveData<GNaviResponse> = MutableLiveData()

    init {
        loadRestaurants()
    }

    private fun loadRestaurants() {
        // Coroutine that will be canceled when the ViewModel is cleared.
        viewModelScope.launch {
            try {
                val response: Response<GNaviResponse> = repository.getRestaurants(
                    BuildConfig.GNAVI_API_KEY,
                    1,
                    33.3, // TODO: 外部から取得
                    33.3 // TODO: 外部から取得
                )
                if (response.isSuccessful) {
                    projectListLiveData.postValue(response.body())
                    Log.d("LIVE_DATA", "Updated.")
                }
            } catch (e: Exception) {
                e.stackTrace
            }
        }
    }
}