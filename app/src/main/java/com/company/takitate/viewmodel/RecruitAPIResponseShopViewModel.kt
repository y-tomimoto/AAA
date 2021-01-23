package com.company.takitate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.company.takitate.domain.entity.Shop

class RecruitAPIResponseShopViewModel: ViewModel() {

  private val _focusedShop: MutableLiveData<Shop> = MutableLiveData()
  val focusedShop: LiveData<Shop> = _focusedShop

  fun updateFocusedShop (shop: Shop) {
    _focusedShop.postValue(shop)
  }

}
