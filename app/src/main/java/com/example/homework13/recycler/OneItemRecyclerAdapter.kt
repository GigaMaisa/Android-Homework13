package com.example.homework13.recycler

import android.app.DatePickerDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.homework13.R
import com.example.homework13.databinding.BirthdayLayoutRecyclerItemBinding
import com.example.homework13.databinding.GenderLayoutRecyclerItemBinding
import com.example.homework13.databinding.InputLayoutRecyclerItemBinding
import java.text.SimpleDateFormat
import java.util.Locale

class OneItemRecyclerAdapter(private val innerCallback: ChildAdapterDataCallback) : ListAdapter<Field, RecyclerView.ViewHolder>(DiffUtilCallback()){
    private companion object {
        const val STANDARD_INPUT = 0
        const val GENDER_CHOOSER = 1
        const val BIRTHDAY_CHOOSER = 2
    }

    fun notifyDataChanged(newField: Field) {
        innerCallback.innerDataChangeListener(field = newField)
    }

    fun setInnerData(list: List<Field>) {
        submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            GENDER_CHOOSER -> GenderChooserViewHolder(GenderLayoutRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            BIRTHDAY_CHOOSER -> BirthdayChooserViewHolder(BirthdayLayoutRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> StandardInputViewHolder(InputLayoutRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is StandardInputViewHolder -> holder.bind()
            is BirthdayChooserViewHolder -> holder.bind()
            is GenderChooserViewHolder -> {
                holder.bind()
                holder.onGenderChoose()
            }
        }
    }

    inner class StandardInputViewHolder(private val binding: InputLayoutRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() = with(binding.etInputField) {
            val item: Field = currentList[adapterPosition]
            setText(currentList[adapterPosition].userInput)
            hint = item.hint

            when (item.keyboard) {
                KeyboardType.TEXT.type -> inputType = 0x00000001
                KeyboardType.NUMBER.type -> inputType = 0x00000002
            }

            doAfterTextChanged {
                currentList[adapterPosition].userInput = it.toString()
                notifyDataChanged(currentList[adapterPosition])
            }
        }
    }

    inner class GenderChooserViewHolder(private val binding: GenderLayoutRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() = with(binding.genderSpinner) {
            ArrayAdapter.createFromResource(binding.root.context, R.array.gender_options, android.R.layout.simple_spinner_item).also { spinnerAdapter ->
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                adapter = spinnerAdapter
            }
        }

        fun onGenderChoose() {
            binding.genderSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        currentList[adapterPosition].userInput = parent?.getItemAtPosition(position).toString()
                        notifyDataChanged(currentList[adapterPosition])
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
        }
    }

    inner class BirthdayChooserViewHolder(private val binding: BirthdayLayoutRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() = with(binding.tvChooseBirthday) {
            if (currentList[adapterPosition].userInput != null) text = currentList[adapterPosition].userInput

            setOnClickListener {
                val dialog = DatePickerDialog(binding.root.context, { _, year, month, dayOfMonth ->
                    val readableData = convertDateToReadableDate("$dayOfMonth/${month + 1}/$year")
                        text = readableData
                        currentList[adapterPosition].userInput = readableData
                        notifyDataChanged(currentList[adapterPosition])
                    }, 2022, 0, 1)

                dialog.show()
            }
        }

        private fun convertDateToReadableDate(input: String): String {
            val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
            val date = inputFormat.parse(input)
            return outputFormat.format(date!!)
        }
    }

    override fun getItemViewType(position: Int): Int {
        with(currentList[position]) {
            if (fieldType == FieldType.INPUT.type) {
                return STANDARD_INPUT
            } else if (fieldType == FieldType.CHOOSER.type && hint == ChooserType.GENDER.type) {
                return GENDER_CHOOSER
            } else if (fieldType == FieldType.CHOOSER.type && hint == ChooserType.DATE.type) {
                return BIRTHDAY_CHOOSER
            }
        }
        return super.getItemViewType(position)
    }
}

private class DiffUtilCallback : DiffUtil.ItemCallback<Field>() {
    override fun areItemsTheSame(field: Field, newField: Field): Boolean {
        return field.fieldId == newField.fieldId
    }

    override fun areContentsTheSame(oldField: Field, newField: Field): Boolean {
        return oldField == newField
    }
}