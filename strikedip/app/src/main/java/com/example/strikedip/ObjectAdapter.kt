package com.example.strikedip

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.strikedip.databinding.ItemObjectBinding

class ObjectAdapter : ListAdapter<ObjectEntity, ObjectAdapter.ObjectViewHolder>(ObjectDiffCallback()){

    var onOpenListStrikeDipActivity: ((ObjectEntity) -> Unit)?= null
    var onDeleteObject: ((ObjectEntity) -> Unit)?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjectViewHolder {
        val view = ItemObjectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ObjectViewHolder(view)
    }

    override fun onBindViewHolder(holder: ObjectViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    inner class ObjectViewHolder(private val binding: ItemObjectBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ObjectEntity) {
            with(binding){
                tvObjectName.text = item.name
                tvObjectNote.text = item.note

                btnShowData.setOnClickListener{
                    onOpenListStrikeDipActivity?.invoke(item)
                }

                btnDeleteData.setOnClickListener {
                    onDeleteObject?.invoke(item)
                }
            }
        }
    }


}

class ObjectDiffCallback : DiffUtil.ItemCallback<ObjectEntity>(){
    override fun areItemsTheSame(oldItem: ObjectEntity, newItem: ObjectEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ObjectEntity, newItem: ObjectEntity): Boolean {
        return oldItem.id == newItem.id
    }
}

