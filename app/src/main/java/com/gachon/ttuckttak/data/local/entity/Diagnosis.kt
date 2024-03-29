package com.gachon.ttuckttak.data.local.entity

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(
    tableName = "user_diagnosis",
    foreignKeys = [
        ForeignKey(
            entity = User::class, // 외래키 연결대상 Entity 클래스
            parentColumns = ["uid"], // 외래키 연결대상 Entity 필드명
            childColumns = ["uid"],
            onDelete = ForeignKey.CASCADE // 삭제될 경우 같이 삭제 설정
        )
    ]
)
data class Diagnosis(
    @ColumnInfo(name = "uid") val uid: String, // 사용자의 식별자 (Foreign Key)
    val item: String, // 진단 항목
    val time: Date = Date(), // 진단 시간. 현재 시간을 기본값으로 사용
    @ColumnInfo(name = "bypass_idx") val bypassIdx: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L

    object MyBind {

        @JvmStatic
        @BindingAdapter("formattedDate")
        fun setFormattedDate(view: TextView, date: Date?) {
            view.text = formatDate(date)
        }

        private fun formatDate(date: Date?): String {
            return if (date != null) {
                SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault()).format(date)
            } else {
                ""
            }
        }
    }
}