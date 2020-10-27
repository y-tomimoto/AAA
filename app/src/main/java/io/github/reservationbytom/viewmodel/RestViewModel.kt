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
    private val myApplication: Application,
    private val restID: Int
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
                val rest = repository.getRest(
                    BuildConfig.GNAVI_API_KEY,
                    restID
                )
                if (rest.isSuccessful) {
                    restLiveData.postValue(rest.body()) // Observerが動作する
                }
            } catch (e: Exception) {
                Log.e("loadRest:Failed", e.stackTrace.toString())
            }
        }
    }

    fun setRest(rest: Rest) {
        this.rest.set(rest)
    }

    //IDの(DI)依存性注入ファクトリ
    class Factory(
        private val application: Application, private val restID: Int
    ) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RestViewModel(application, restID) as T
        }
    }


}