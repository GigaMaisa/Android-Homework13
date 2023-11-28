package com.example.homework13.recycler

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.homework13.R
import com.example.homework13.databinding.RecyclerInputItemBinding
import java.text.SimpleDateFormat
import java.util.Locale

class RegistrationFormRecyclerAdapter :
    ListAdapter<List<Field>, RegistrationFormRecyclerAdapter.FieldsViewHolder>(DiffCallback()) {
    private val mapOfElements = mutableMapOf<Int, TextView>()

    fun setData(list: List<List<Field>>) {
        submitList(list)
    }

    fun getMapOfElements(): Map<Int, TextView> {
        return mapOfElements
    }

    inner class FieldsViewHolder(private val binding: RecyclerInputItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(fields: List<Field>) {
            val container = binding.root
            container.removeAllViews()

            fields.forEach { field: Field ->
                when (field.fieldType) {
                    "input" -> {
                        val editText = EditText(container.context).apply {
                            Log.d("FIELDFOREACHBLIAD", field.toString())

                            hint = field.hint
                            when (field.keyboard) {
                                "text" -> inputType = 0x00000001
                                "number" -> inputType = 0x00000002
                            }

                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                            )
                        }

                        container.addView(editText)
                        if (field.required) {
                            mapOfElements[field.fieldId] = editText
                        }
                    }

                    "chooser" -> {
                        if (field.hint == "Gender") {
                            val genderSpinner = Spinner(container.context).apply {
                                ArrayAdapter.createFromResource(
                                    container.context,
                                    R.array.gender_options,
                                    android.R.layout.simple_spinner_item
                                ).also { adapter ->
                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                    this.adapter = adapter
                                }
                                layoutParams = LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                )

                            }
                            container.addView(genderSpinner)

                            genderSpinner.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        parent: AdapterView<*>?,
                                        view: View?,
                                        position: Int,
                                        id: Long
                                    ) {
                                        parent?.getItemAtPosition(position)
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {

                                    }
                                }
                        } else if (field.hint == "Birthday") {
                            val birthdayPicker = TextView(container.context).apply {
                                text = "Choose Birthday"
                                textSize = 15.0F
                                layoutParams = LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                )
                            }
                            container.addView(birthdayPicker)
                            birthdayPicker.setOnClickListener {
                                val dialog = DatePickerDialog(
                                    container.context,
                                    { _, year, month, dayOfMonth ->
                                        birthdayPicker.text =
                                            convertDateToReadableDate("$dayOfMonth/${month + 1}/$year")
                                    },
                                    2022,
                                    0, 1
                                )
                                dialog.show()
                            }
                        }
                    }
                }
            }
        }

        private fun convertDateToReadableDate(input: String): String {
            val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
            val date = inputFormat.parse(input)
            return outputFormat.format(date!!)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FieldsViewHolder {
        return FieldsViewHolder(
            RecyclerInputItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FieldsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

private class DiffCallback : DiffUtil.ItemCallback<List<Field>>() {
    override fun areItemsTheSame(oldList: List<Field>, newList: List<Field>): Boolean {
        return oldList == newList
    }

    override fun areContentsTheSame(oldList: List<Field>, newList: List<Field>): Boolean {
        return oldList == newList
    }
}