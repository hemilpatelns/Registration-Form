package com.example.neostart.util

import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.neostart.R

//fun <T : Enum<T>> Spinner.setEnumAdapter(
//    enumClass: Class<T>,
//    displayItemSelector: (T) -> String,
//    onItemSelected: (T) -> Unit
//) {
//    // Getting all enum values and displaying them
//    val items = enumClass.enumConstants?.map { displayItemSelector(it) } ?: emptyList()
//
//    // Creating an ArrayAdapter using the enum names
//    val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, items)
//    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//    this.adapter = adapter
//
//    // Handling Spinner selection
//    this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//            val selectedName = parent?.getItemAtPosition(position) as String
//            val selectedEnum = enumClass.enumConstants?.find { displayItemSelector(it) == selectedName }
//            selectedEnum?.let {
//                onItemSelected(it)
//            }
//        }
//
//        override fun onNothingSelected(parent: AdapterView<*>?) {
//            TODO("Not yet implemented")
//        }
//
//    }
//}

fun <T> Spinner.setAdapter(
    items: List<T>,
    onItemSelected: (T) -> Unit
){

}

fun <T : Enum<T>> Spinner.setEnumAdapter(
    enumClass: Class<T>,
    displayItemSelector: (T) -> String,
    onItemSelected: (T) -> Unit
) {
    // Get all enum values and their display names
    val items = enumClass.enumConstants?.map { displayItemSelector(it) } ?: emptyList()

    // Create an ArrayAdapter using the enum names
    val adapter = object : ArrayAdapter<String>(context, R.layout.item_spinner_option, items) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = super.getView(position, convertView, parent) as TextView
            customizeView(view, position)
            return view
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = super.getDropDownView(position, convertView, parent) as TextView
            customizeView(view, position)
            return view
        }

        private fun customizeView(view: TextView, position: Int) {
            val item = getItem(position)
            view.text = item
            // Change color based on the display item
            if (item == displayItemSelector(enumClass.enumConstants?.find { it.name == "NONE" }!!)) {
                view.setTextColor(ContextCompat.getColor(context, R.color.hintColor)) // Change color as needed
                view.setTypeface(view.typeface, Typeface.ITALIC)
            }else{
                view.setTextColor(ContextCompat.getColor(context, R.color.black))
                view.typeface = Typeface.DEFAULT
            }
        }
    }

    adapter.setDropDownViewResource(R.layout.layout_spinner_item)
    this.adapter = adapter

    // Set default selection to "None"
//    val noneIndex = items.indexOf(displayItemSelector(enumClass.enumConstants?.find { it.name == "NONE" }!!))
//    this.setSelection(noneIndex)

    // Handle spinner selection
    this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val selectedName = parent?.getItemAtPosition(position) as String
            val selectedEnum = enumClass.enumConstants?.find { displayItemSelector(it) == selectedName }
            selectedEnum?.let {
                onItemSelected(it)
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            // Handle the case when nothing is selected if needed
        }
    }
}