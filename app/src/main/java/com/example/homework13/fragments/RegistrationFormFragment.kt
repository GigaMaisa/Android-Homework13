package com.example.homework13.fragments

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.homework13.databinding.FragmentRegistrationFormBinding
import com.example.homework13.recycler.ChildAdapterDataCallback
import com.example.homework13.recycler.Field
import com.example.homework13.recycler.RegistrationFormRecyclerAdapter
import com.example.homework13.viewmodel.ItemViewModel

class RegistrationFormFragment : BaseFragment<FragmentRegistrationFormBinding>(FragmentRegistrationFormBinding::inflate), ChildAdapterDataCallback {
    private val inputsAdapter = RegistrationFormRecyclerAdapter(this)
    private val viewModel: ItemViewModel by viewModels()

    override fun setUp() {
        initRecycler()
        onRegisterButtonClick()
    }

    private fun onRegisterButtonClick() {
        val fields = viewModel.getFieldsFlattenList()
        val requiredFields = fields.filter { it.required }

        binding.btnRegister.setOnClickListener {
            val requiredFieldsMap = requiredFields.associate { it.fieldId to listOf(it.hint, it.userInput) }
            if (validateMap(requiredFieldsMap)) {
                showToast("Successful registration")
            }
        }
    }

    private fun validateMap(fieldsMap: Map<Int, List<String>>): Boolean {
        fieldsMap.forEach {
            if (it.value[1].isNullOrEmpty() || fieldsMap.isEmpty()) {
                showToast("${it.value[0]} Is Required")
                return false
            }
        }

        return true
    }

    private fun initRecycler() {
        binding.rvInputs.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = inputsAdapter
            inputsAdapter.setData(viewModel.inputData.value.orEmpty())
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun innerDataChangeListener(field: Field) {
        viewModel.updateField(field)
    }
}