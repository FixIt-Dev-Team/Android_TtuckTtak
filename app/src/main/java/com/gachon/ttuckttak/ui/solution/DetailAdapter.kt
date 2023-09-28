package com.gachon.ttuckttak.ui.solution

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gachon.ttuckttak.databinding.ViewItemDetailContentBinding

class DetailAdapter(val contentList: List<String>) : RecyclerView.Adapter<DetailAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ViewItemDetailContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.content.text = contentList[position]
    }

    override fun getItemCount(): Int {
        return contentList.size
    }

    inner class Holder(val binding: ViewItemDetailContentBinding) : RecyclerView.ViewHolder(binding.root) {
        val content = binding.textLevelAContent
    }

}