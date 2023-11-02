package com.gachon.ttuckttak.ui.solution

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gachon.ttuckttak.data.remote.dto.solution.SolutionBypassDto
import com.gachon.ttuckttak.databinding.ViewItemBypassBinding

class BypassAdapter(val bypassList: List<SolutionBypassDto>) : RecyclerView.Adapter<BypassAdapter.Holder>() {

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ViewItemBypassBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.title.text = bypassList[position].targetEntryName

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    override fun getItemCount(): Int {
        return bypassList.size
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    private lateinit var itemClickListener : OnItemClickListener

    inner class Holder(val binding: ViewItemBypassBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.bypassTitle
    }
}