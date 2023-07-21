package com.gachon.ttuckttak.ui.setting

import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.databinding.ActivitySettingsBinding
import com.gachon.ttuckttak.ui.login.LoginActivity


class SettingsActivity : BaseActivity<ActivitySettingsBinding>(ActivitySettingsBinding::inflate) {

    override fun initAfterBinding() = with(binding) {
        // 뒤로가기 버튼을 누르는 경우 - 이전 화면으로 이동 <임시 설정>
        buttonBack.setOnClickListener {
            //startNextActivity()
        }

        // 프로필 박스의 화살표를 누르는 경우 - 프로필 설정 액티비티로 이동
        layoutProfile.setOnClickListener {
            startNextActivity(SettingsProfileActivity::class.java)
        }

        // 알림 설정 버튼을 누르는 경우 - 알림 설정 액티비티로 이동
        buttonAlertSetting.setOnClickListener {
            startNextActivity(SettingsAlertActivity::class.java)
        }

        // 뚝딱센터 버튼을 누르는 경우 - 고객센터 액티비티로 이동 <임시 설정>
        buttonConsumerCenter.setOnClickListener {
            //startNextActivity()
        }

        // 리뷰 및 평가하기 버튼을 누르는 경우 - 해당 액티비티로 이동 <임시 설정>
        layoutReview.setOnClickListener {
            //startNextActivity()
        }

        // 도움 및 설명 버튼을 누르는 경우 - 해당 액티비티로 이동 <임시 설정>
        buttonHelp.setOnClickListener {
            //startNextActivity()
        }

        // 이용 수칙 버튼을 누르는 경우 - 해당 액티비티로 이동 <임시 설정>
        buttonUse.setOnClickListener {
            //startNextActivity()
        }

        // 로그아웃 버튼을 누르는 경우 - LoginActivity로 이동
        buttonLogout.setOnClickListener {
            startActivityWithClear(LoginActivity::class.java)
        }
    }
}