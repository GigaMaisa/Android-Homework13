package com.example.homework13.recycler

import com.google.gson.annotations.SerializedName

data class Field(
    @SerializedName("field_id")
    val fieldId: Int,
    val hint: String,
    @SerializedName("field_type")
    val fieldType: String,
    val keyboard: String,
    val required: Boolean,
    @SerializedName("is_active")
    val isActive: Boolean,
    val icon: String,
    var userInput: String = "",
)

enum class FieldType(val type: String) {
    INPUT(type = "input"),
    CHOOSER(type = "chooser")
}

enum class ChooserType(val type: String) {
    DATE(type = "Birthday"),
    GENDER(type = "Gender")
}

enum class KeyboardType(val type: String) {
    TEXT(type = "text"),
    NUMBER(type = "number")
}
