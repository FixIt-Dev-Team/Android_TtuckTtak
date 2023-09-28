package com.gachon.ttuckttak.ui.solution

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gachon.ttuckttak.data.remote.dto.solution.SolutionDto
import com.gachon.ttuckttak.databinding.ViewItemSolutionBeforeBinding

class SolutionBeforeAdapter(val solutionList: List<SolutionDto>) : RecyclerView.Adapter<SolutionBeforeAdapter.Holder>() {
    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    var itemClick: ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ViewItemSolutionBeforeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.itemView.setOnClickListener {
            itemClick?.onClick(it, position)
        }
        holder.title.text = solutionList[position].descHeader
    }

    override fun getItemCount(): Int {
        return solutionList.size
    }

    inner class Holder(val binding: ViewItemSolutionBeforeBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.title
    }
}