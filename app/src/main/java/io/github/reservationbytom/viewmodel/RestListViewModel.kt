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

class RestListViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = GNaviRepository.instance

    // LiveDataについて: https://qiita.com/amay077/items/6e1c94305420a41ff7ed
    // TODO: ObservableField と比較検討
    private var restListLiveData: MutableLiveData<GNaviResponse> = MutableLiveData()

    init {
        loadRests()
    }

    private fun loadRests() {
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
                    restListLiveData.postValue(response.body()) // Observerが変更を検知する
                    Log.d("LIVE_DATA", "Updated.")
                }
            } catch (e: Exception) {
                e.stackTrace
            }
        }
    }
}