package com.example.homework13.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.homework13.databinding.RegisterRecyclerFormItemBinding

class RegistrationFormRecyclerAdapter(private val childAdapterCallback: ChildAdapterDataCallback) : ListAdapter<List<Field>, RegistrationFormRecyclerAdapter.FieldsViewHolder>(DiffCallback()), ChildAdapterDataCallback {
    fun setData(list: List<List<Field>>) {
        submitList(list)
    }

    inner class FieldsViewHolder(private val binding: RegisterRecyclerFormItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() = with(binding.rvRegisterForm) {
            val innerAdapter = OneItemRecyclerAdapter(childAdapterCallback)
            innerAdapter.setInnerData(currentList[adapterPosition])

            layoutManager = LinearLayoutManager(binding.root.context)
            adapter = innerAdapter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FieldsViewHolder {
        return FieldsViewHolder(
            RegisterRecyclerFormItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FieldsViewHolder, position: Int) {
        holder.bind()
    }

    override fun innerDataChangeListener(field: Field) {
        childAdapterCallback.innerDataChangeListener(field)
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