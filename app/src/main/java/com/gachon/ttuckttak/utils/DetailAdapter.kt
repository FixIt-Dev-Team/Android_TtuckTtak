package com.gachon.ttuckttak.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gachon.ttuckttak.databinding.ItemDetailContentBinding

class DetailAdapter(val contentList: List<String>) : RecyclerView.Adapter<DetailAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailAdapter.Holder {
        val binding = ItemDetailContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: DetailAdapter.Holder, position: Int) {
        holder.content.text = contentList[position]
    }

    override fun getItemCount(): Int {
        return contentList.size
    }

    inner class Holder(val binding: ItemDetailContentBinding) : RecyclerView.ViewHolder(binding.root) {
        val content = binding.textLevelAContent
    }

}