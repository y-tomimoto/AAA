package io.github.reservationbytom

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.MaterialDatePicker
import javax.xml.datatype.DatatypeConstants.MONTHS


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

        val dp_fragmet = MaterialDatePicker.Builder.datePicker().build().apply {
            addOnPositiveButtonClickListener { time: Long ->

            }
        }
        // ここでpickerフラ面とを生成して、
        val showdp = view.findViewById<EditText>(R.id.showdp)
        showdp.setFocusable(false) // これでtapしても、反応しないように設定している。この値は、Calendarwidgetからのみ変更できるようにする、
//        showdp.setOnClickListener {
//            dp_fragmet.show(childFragmentManager, "Tag")
//        }

        return view
    }
}
