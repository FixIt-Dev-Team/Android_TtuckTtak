package com.gachon.ttuckttak.ui.setting

import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.data.local.TokenManager
import com.gachon.ttuckttak.data.local.UserManager
import com.gachon.ttuckttak.data.local.database.AppDatabase
import com.gachon.ttuckttak.data.remote.TtukttakServer
import com.gachon.ttuckttak.data.remote.dto.NoticeReq
import com.gachon.ttuckttak.databinding.ActivitySettingsAlertBinding
import com.gachon.ttuckttak.ui.login.LandingActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

class SettingsAlertActivity : BaseActivity<ActivitySettingsAlertBinding>(ActivitySettingsAlertBinding::inflate) {

    private val userManager: UserManager by lazy { UserManager(this@SettingsAlertActivity) }
    private val tokenManager: TokenManager by lazy { TokenManager(this@SettingsAlertActivity) }

    val userDao by lazy {AppDatabase.getDatabase(this@SettingsAlertActivity).userDao()}
    override fun initAfterBinding() = with(binding) {

        setUi()

        // 뒤로가기 버튼을 누르는 경우
        buttonBack.setOnClickListener {
            // 설정 화면으로 이동
            startNextActivity(SettingsActivity::class.java)
        }

        // event switch를 누르는 경우
        switchEventAndFunctionPush.setOnCheckedChangeListener{CompoundButton, onSwitch ->
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

    private fun setUi() = with(binding) {
        val user = userDao.getUser()

        switchEventAndFunctionPush.isChecked = user.alertEvent
        switchNightTimePushAlert.isChecked = user.alertNight
    }
}