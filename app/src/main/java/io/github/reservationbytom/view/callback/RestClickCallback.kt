package io.github.reservationbytom.view.callback

import io.github.reservationbytom.service.model.Rest

interface RestClickCallback {
    fun onClick(rest: Rest)
}
