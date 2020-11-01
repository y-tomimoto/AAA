package io.github.reservationbytom

import com.google.gson.GsonBuilder
import io.github.reservationbytom.service.repository.GNaviRepository
import io.github.reservationbytom.service.repository.GNaviService
import io.github.reservationbytom.service.repository.HTTPS_API_GNAVI_URL
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }
}
