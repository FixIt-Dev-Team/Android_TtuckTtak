package com.gachon.ttuckttak.ui.setting

import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.data.local.TokenManager
import com.gachon.ttuckttak.data.local.UserManager
import com.gachon.ttuckttak.data.remote.TtukttakServer
import com.gachon.ttuckttak.data.remote.dto.NoticeReq
import com.gachon.ttuckttak.databinding.ActivitySettingsAlertBinding
import com.gachon.ttuckttak.ui.login.LandingActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsAlertActivity : BaseActivity<ActivitySettingsAlertBinding>(ActivitySettingsAlertBinding::inflate) {

    private val userManager: UserManager by lazy { UserManager(this@SettingsAlertActivity) }
    private val tokenManager: TokenManager by lazy { TokenManager(this@SettingsAlertActivity) }

    override fun initAfterBinding() {
        setUi()
        setClickListener()
    }

    private fun setUi() = with(binding) {
        switchEventAndFunctionPush.isChecked = intent.getBooleanExtra("pushStatus", false)
        switchNightTimePushAlert.isChecked = intent.getBooleanExtra("nightPushStatus", false)
    }

    private fun setClickListener() = with(binding) {
        // 뒤로가기 버튼을 누르는 경우
        buttonBack.setOnClickListener {
            finish()
        }

        // event switch를 누르는 경우
        switchEventAndFunctionPush.setOnCheckedChangeListener { CompoundButton, onSwitch ->
            launcherEvent(onSwitch)
        }

        // night time switch를 누르는 경우
        switchNightTimePushAlert.setOnCheckedChangeListener { CompoundButton, onSwitch ->
            launcherNight(onSwitch)
        }
    }

    private fun launcherEvent(value: Boolean) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // 서버에 push 요청
                val response = TtukttakServer.push(tokenManager.getAccessToken()!!, NoticeReq(userManager.getUserIdx()!!, value))
                Log.i("response", response.toString())

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e(LandingActivity.TAG, "서버 통신 오류: ${e.message}")
                    showToast("push 요청 실패")
                }
            }
        }
    }

    private fun launcherNight(value: Boolean) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // 서버에 push 요청
                val response = TtukttakServer.pushNight(tokenManager.getAccessToken()!!, NoticeReq(userManager.getUserIdx()!!, value))
                Log.i("response", response.toString())

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e(LandingActivity.TAG, "서버 통신 오류: ${e.message}")
                    showToast("push 요청 실패")
                }
            }
        }
    }
}