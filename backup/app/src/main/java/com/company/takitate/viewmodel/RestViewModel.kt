package com.company.takitate.viewmodel

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.company.takitate.BuildConfig
import com.company.takitate.service.model.Rest
import com.company.takitate.service.repository.GNaviRepository
import kotlinx.coroutines.launch

class RestViewModel(
    private val myApplication: Application,
    private val restID: Int
) : AndroidViewModel(myApplication) {
    private val repository = GNaviRepository.instance
    val restLiveData: MutableLiveData<Rest> = MutableLiveData()
    private var rest = ObservableField<Rest>() // LiveDataとは異なり、更新時に値が異なっている場合にも監視関数が発火する : https://blog.amay0777.net/blog/2017/12/05/58e589780ccea6fd5470/

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
        @Suppress("UNCHECKED_CAST") // 使用しないParameterでwarnを出さないように指定: https://rmakiyama.hatenablog.com/entry/2018/01/26/140944
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RestViewModel(application, restID) as T
        }
    }


}
