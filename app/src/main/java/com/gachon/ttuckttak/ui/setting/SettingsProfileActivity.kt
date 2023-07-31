package com.gachon.ttuckttak.ui.setting

import android.view.View
import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.databinding.ActivitySettingsProfileBinding

class SettingsProfileActivity : BaseActivity<ActivitySettingsProfileBinding>(ActivitySettingsProfileBinding::inflate) {

    override fun initAfterBinding() = with(binding) {

        // 뒤로가기 버튼을 누르는 경우
        buttonBack.setOnClickListener {
            finish()
        }

        // 변경 사항이 일어난 경우

        // 변경 사항이 일어난 후, 저장을 하려는 경우
        layoutCheckPoint.setOnClickListener {
            imageviewCheckPointCircle.setImageResource(R.drawable.ic_check_blue)
        }

        // 프로필 사진 변경 버튼을 클릭하는 경우
        layoutProfilePlus.setOnClickListener {

        }

        // 닉네임 변경 버튼을 누르는 경우
        buttonSettingNikname.setOnClickListener {

        }

        // 닉네임을 입력하는 경우
        edittextInputNikname.setOnClickListener {
            // 입력한 nikname 가져오기
            val nikname = edittextInputNikname.text.toString()

            // nikname 입력 여부 및 중복 여부 확인
            // nikname이 공백이 아닐 경우
            if(!nikname.isBlank() ) {
// nikname 중복 검사
                layoutSettingNikname.setOnClickListener {
                    // 중복인 경우
                    if(true) { // <임시설정>
                        // 중복 에러 메시지
                        textviewOverlapNikname.setVisibility(View.VISIBLE)
                    }

                    // 중복이 아닌 경우
                    else {
                        // email 입력
                        edittextInputEmail.setOnClickListener {
                            // 입력한 email 가져오기
                            val email = edittextInputEmail.text.toString()

                            // email이 공백이 아닌 경우
                            if(!email.isBlank()) {
                                // 비밀번호 재설정 버튼을 누르는 경우
                                buttonPasswordReset.setOnClickListener {
                                   // <관련 화면으로 이동>
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}