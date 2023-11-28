package com.example.homework13.fragments

import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.homework13.recycler.Field
import com.example.homework13.utility.ReadJsonFile
import com.example.homework13.recycler.RegistrationFormRecyclerAdapter
import com.example.homework13.databinding.FragmentRegistrationFormBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RegistrationFormFragment : BaseFragment<FragmentRegistrationFormBinding>(FragmentRegistrationFormBinding::inflate) {
    private val inputsAdapter = RegistrationFormRecyclerAdapter()

    override fun setUp() {
        initRecycler()
        onRegisterButtonClick()
    }

    private fun onRegisterButtonClick() {
        binding.btnRegister.setOnClickListener {
            if (validate(inputsAdapter.getMapOfElements())) {
                showToast("Congrats")
            }
        }
    }

    private fun validate(mapOfElements: Map<Int, TextView>): Boolean {
        var notValid = false
        mapOfElements.values.forEach {
            if (it.text?.isEmpty()!!) {
                showToast("Field is not filled: ${it.hint}")
                notValid = true
            }
        }

        return !notValid
    }

    private fun initRecycler() {
        binding.rvInputs.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = inputsAdapter
            inputsAdapter.setData(readJson())
        }
    }

    private fun readJson(): List<List<Field>> {
        val gson = Gson()
        val jsonString = ReadJsonFile().readFile(requireContext(), "response.json")

        val type = object : TypeToken<List<List<Field>>>() {}.type
        return gson.fromJson(jsonString, type)
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}