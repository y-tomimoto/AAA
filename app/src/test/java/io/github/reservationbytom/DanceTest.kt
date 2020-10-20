package io.github.reservationbytom

import org.junit.Test

import org.junit.Assert.*

class DanceTest {

    @Test
    fun test1() {
        val result: Int = Dance().test()
        assertEquals(1,result)
    }
}