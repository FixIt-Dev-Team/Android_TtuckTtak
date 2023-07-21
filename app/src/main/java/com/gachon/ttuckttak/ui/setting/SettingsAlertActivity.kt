package com.gachon.ttuckttak.ui.setting

import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.databinding.ActivitySettingsAlertBinding

class SettingsAlertActivity : BaseActivity<ActivitySettingsAlertBinding>(ActivitySettingsAlertBinding::inflate) {

    override fun initAfterBinding() = with(binding) {
        // 뒤로가기 버튼을 누르는 경우
        buttonBack.setOnClickListener {
            // 설정 화면으로 이동
            startNextActivity(SettingsActivity::class.java)
        }

        // event switch를 누르는 경우

        switchEventAndFunctionPush.setOnCheckedChangeListener{_, isChecked ->
            if(isChecked) {
                // switch가 체크된 경우
                switchEventAndFunctionPush.toggle()
            }
            else {
                // switch가 체크되지 않은 경우
            }
        }

        // night time switch를 누르는 경우
        switchNightTimePushAlert.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                // switch가 체크된 경우
                switchNightTimePushAlert.toggle()
            }
            else {
                // switch가 체크되지 않은 경우
            }
        }
    }
}