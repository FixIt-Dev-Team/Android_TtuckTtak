package com.gachon.ttuckttak.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gachon.ttuckttak.data.remote.dto.solution.SolutionDto
import com.gachon.ttuckttak.databinding.ItemSolutionBinding

class SolutionAdapter(val solutionList: List<SolutionDto>) : RecyclerView.Adapter<SolutionAdapter.Holder>() {

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SolutionAdapter.Holder {
        val binding = ItemSolutionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: SolutionAdapter.Holder, position: Int) {
        holder.title.text = solutionList[position].descHeader

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    override fun getItemCount(): Int {
        return solutionList.size
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    private lateinit var itemClickListener : OnItemClickListener

    inner class Holder(val binding: ItemSolutionBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.title
    }
}