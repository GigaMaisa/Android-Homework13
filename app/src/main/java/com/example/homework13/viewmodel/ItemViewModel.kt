package com.example.homework13.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.homework13.recycler.Field
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ItemViewModel : ViewModel() {
    private val _inputData = MutableLiveData<List<List<Field>>>()
    val inputData: LiveData<List<List<Field>>> get() = _inputData

    private val json: String = """
        [
          [
            {
              "field_id": 1,
              "hint": "UserName",
              "field_type": "input",
              "keyboard": "text",
              "required": false,
              "is_active": true,
              "icon": "https://jemala.png"
            },
            {
              "field_id": 2,
              "hint": "Email",
              "field_type": "input",
              "required": true,
              "keyboard": "text",
              "is_active": true,
              "icon": "https://jemala.png"
            },
            {
              "field_id": 3,
              "hint": "phone",
              "field_type": "input",
              "required": true,
              "keyboard": "number",
              "is_active": true,
              "icon": "https://jemala.png"
            }
          ],
          [
            {
              "field_id": 4,
              "hint": "FullName",
              "field_type": "input",
              "keyboard": "text",
              "required": true,
              "is_active": true,
              "icon": "https://jemala.png"
            },
            {
              "field_id": 14,
              "hint": "Jemali",
              "field_type": "input",
              "keyboard": "text",
              "required": false,
              "is_active": true,
              "icon": "https://jemala.png"
            },
            {
              "field_id": 89,
              "hint": "Birthday",
              "field_type": "chooser",
              "required": false,
              "is_active": true,
              "icon": "https://jemala.png"
            },
            {
              "field_id": 898,
              "hint": "Gender",
              "field_type": "chooser",
              "required": false,
              "is_active": true,
              "icon": "https://jemala.png"
            }
          ]
        ]
    """.trimIndent()

    init {
        val type = object : TypeToken<List<List<Field>>>() {}.type
        _inputData.value = Gson().fromJson(json, type)
    }

    fun updateField(updatedField: Field) {
        val currData = _inputData.value.orEmpty().toMutableList()

        for (row in currData.indices) {
            val column = currData[row].indexOfFirst {
                it.fieldId == updatedField.fieldId
            }

            if (column != -1) {
                val updatedRow = currData[row].toMutableList()
                updatedRow[column] = updatedField
                currData[row] = updatedRow
                _inputData.value = currData
                break
            }
        }
    }

    fun getFieldsFlattenList(): List<Field> {
        return inputData.value.orEmpty().flatten()
    }
}