package io.github.reservationbytom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment


class ReservationMain : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_reservation)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(
            R.layout.activity_reservation,
            container,
            false
        )
        val ctx = context
        val dropdown = view.findViewById<Spinner>(R.id.spinner)
        val items = arrayOf("1", "2", "three")
        val adapter = ctx?.let { ArrayAdapter(it,android.R.layout.simple_spinner_dropdown_item,items) }
        dropdown.adapter = adapter
        return view
    }
}
