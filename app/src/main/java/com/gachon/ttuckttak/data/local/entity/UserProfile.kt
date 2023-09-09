package com.gachon.ttuckttak.data.local.entity

import androidx.databinding.BindingAdapter
import androidx.room.ColumnInfo
import com.bumptech.glide.Glide
import com.gachon.ttuckttak.R
import de.hdodenhof.circleimageview.CircleImageView

data class UserProfile(
    @ColumnInfo(name = "user_name") val userName: String?,
    val email: String?,
    @ColumnInfo(name = "profile_img_url") val profileImgUrl: String?
) {
    object MyBind {

        @JvmStatic
        @BindingAdapter("loadImage")
        fun loadImage(view: CircleImageView, profileImgUrl: String?) {
            Glide.with(view.context)
                .load(profileImgUrl) // 사용자의 프로필 이미지를 Load
                .placeholder(R.drawable.img_profile) // 사용자의 프로필 로딩을 시작하기 전에 보여줄 이미지 설정
                .error(R.drawable.img_profile) // 리소스를 불러오다가 에러가 발생했을 때 보여줄 이미지를 설정
                .fallback(R.drawable.img_profile) // Load할 url이 null인 경우 등 비어있을 때 보여줄 이미지를 설정
                .into(view)
        }
    }
}