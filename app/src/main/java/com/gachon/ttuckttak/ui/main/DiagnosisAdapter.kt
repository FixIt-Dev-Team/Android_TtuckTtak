package com.gachon.ttuckttak.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.databinding.ViewDiagnosisBinding
import com.gachon.ttuckttak.data.local.entity.Diagnosis

// RecentDiagnosisAdapter 클래스 선언. ListAdapter를 상속받아서 구현.
class DiagnosisAdapter : ListAdapter<Diagnosis, DiagnosisAdapter.DiagnosisViewHolder>(DiffCallback) {

    // ViewHolder를 생성하는 메소드. 여기서는 DataBinding을 사용하여 ViewHolder를 생성합니다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiagnosisViewHolder {
        val binding: ViewDiagnosisBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.view_diagnosis, parent, false)
        return DiagnosisViewHolder(binding)
    }

    // ViewHolder에 데이터를 바인딩하는 메소드. position 파라미터로 어댑터의 위치 정보를 받아옵니다.
    override fun onBindViewHolder(holder: DiagnosisViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    // DiagnosisViewHolder 클래스 선언. 이 클래스는 RecyclerView의 각 아이템 뷰에 대한 참조를 가집니다.
    inner class DiagnosisViewHolder(private val binding: ViewDiagnosisBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // bind 함수는 실제로 데이터와 뷰 홀더(ViewHolder)를 연결(bind)하는 역할을 합니다.
        fun bind(data: Diagnosis) {
            binding.apply {
                diagnosis = data
                executePendingBindings()
            }
        }
    }

    // DiffCallback 객체 선언. 이 객체는 리사이클러뷰가 새로운 리스트와 이전 리스트의 차이점을 알 수 있게 돕습니다.
    companion object DiffCallback : DiffUtil.ItemCallback<Diagnosis>() {

        // areItemsTheSame 메소드는 두 개체가 같은 항목인지 확인합니다 (즉, 동일한 항목인지).
        override fun areItemsTheSame(oldItem: Diagnosis, newItem: Diagnosis): Boolean {
            return oldItem === newItem
        }

        // areContentsTheSame 메소드는 두 항목의 데이터가 같은지 확인합니다 (즉, 동일한 데이터인지).
        override fun areContentsTheSame(oldItem: Diagnosis, newItem: Diagnosis): Boolean {
            return oldItem.id == newItem.id
        }
    }
}
