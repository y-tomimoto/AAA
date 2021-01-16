package com.company.takitate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MapBottomSheetViewModel: ViewModel() {

  // 実際に値がsetされる
  private val _bottomSheetText: MutableLiveData<String> =
    MutableLiveData<String>().also { mutableLiveData ->
      mutableLiveData.value = "Ready"
    }

  // この値をlayout内でreadしている
  val bottomSheetText: LiveData<String> // ライフサイクルに応じて自動的に購読解除してくれる通知プロパティ
    get() { // このgetはinitializer
      return _bottomSheetText
    }

  // 外部からViewModelを更新する際は、この関数をcallする。
  fun updateBottomSheetText (str:String) {
    println("fire updateBottomSheetText!")
    _bottomSheetText.value = str
  }

  // 略
  private val _buttonText: MutableLiveData<String> =
    MutableLiveData<String>().also { mutableLiveData ->
      mutableLiveData.value = "Ready"
    }

  val buttonText: LiveData<String>
    get() = _buttonText

  fun updateButton(string: String) {
    _buttonText.value = string // .value は setValueのsyntax suger。このスレッド以外では参照されない。
  }

}
