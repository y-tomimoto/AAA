package com.company.takitate.service.repository

import com.company.takitate.service.model.Mock

class MockRepository {
  fun getRestaurants(latitude: Double, longitude: Double): List<Mock>  {
    val m1 = Mock(name = "test1",id = 1, latitude= latitude + 0.002, longitude= longitude  )
    val m2 = Mock(name = "test2",id = 2,  latitude= latitude, longitude= longitude + 0.002 )
    return mutableListOf(m1, m2)
  }
}
