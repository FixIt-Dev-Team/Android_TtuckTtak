package com.gachon.ttuckttak.ui.setting

import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.databinding.ActivitySettingsAlertBinding

class SettingsAlertActivity : BaseActivity<ActivitySettingsAlertBinding>(ActivitySettingsAlertBinding::inflate) {

    override fun initAfterBinding() = with(binding) {
        // 뒤로가기 버튼을 누르는 경우
        buttonBack.setOnClickListener {
            finish() // 설정 화면으로 이동
        }

        // event switch를 누르는 경우
        switchEventAndFunctionPush.setOnCheckedChangeListener { CompoundButton, onSwitch ->
            switchEventAndFunctionPush.setChecked(!onSwitch) // 현 상태 반대로

            // Todo: 서버에 전송
        }

        // night time switch를 누르는 경우
        switchNightTimePushAlert.setOnCheckedChangeListener { CompoundButton, onSwitch ->
            switchNightTimePushAlert.setChecked(!onSwitch) // 현 상태 반대

            // Todo: 서버에 전송
        }
    }
}