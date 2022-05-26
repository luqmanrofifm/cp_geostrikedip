package com.example.strikedip

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.strikedip.databinding.ItemDataStrikeDipBinding

class StrikeDipAdapter : ListAdapter<StrikeDipEntity, StrikeDipAdapter.StrikeDipViewHolder>(StrikeDipDiffCallback()){

    var onOpenDetailStrikeDip: ((StrikeDipEntity) -> Unit)?= null

    var onDeleteStrikeDip: ((StrikeDipEntity) -> Unit)?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StrikeDipViewHolder {
        val view = ItemDataStrikeDipBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StrikeDipViewHolder(view)
    }

    override fun onBindViewHolder(holder: StrikeDipViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    inner class StrikeDipViewHolder(private val binding: ItemDataStrikeDipBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: StrikeDipEntity) {
            with(binding){
                tvKeteranganPengukuran.text = "Pengukuran id ke-"+item.id.toString()
                btnShowData.setOnClickListener{
                    onOpenDetailStrikeDip?.invoke(item)
                }
                btnDeleteData.setOnClickListener {
                    onDeleteStrikeDip?.invoke(item)
                }
            }
        }
    }


}

class StrikeDipDiffCallback : DiffUtil.ItemCallback<StrikeDipEntity>(){
    override fun areItemsTheSame(oldItem: StrikeDipEntity, newItem: StrikeDipEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: StrikeDipEntity, newItem: StrikeDipEntity): Boolean {
        return oldItem.id == newItem.id
    }
}

