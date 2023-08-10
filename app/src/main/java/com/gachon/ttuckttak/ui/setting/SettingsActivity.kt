package com.gachon.ttuckttak.ui.setting

import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.data.local.TokenManager
import com.gachon.ttuckttak.data.local.UserManager
import com.gachon.ttuckttak.data.remote.TtukttakServer
import com.gachon.ttuckttak.data.remote.dto.LogoutReq
import com.gachon.ttuckttak.databinding.ActivitySettingsBinding
import com.gachon.ttuckttak.ui.login.LandingActivity
import com.gachon.ttuckttak.ui.login.LoginActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SettingsActivity : BaseActivity<ActivitySettingsBinding>(ActivitySettingsBinding::inflate) {

    private val userManager: UserManager by lazy { UserManager(this@SettingsActivity) }
    private val tokenManager: TokenManager by lazy { TokenManager(this@SettingsActivity) }

    private val email: String by lazy { intent.getStringExtra("email")!! }

    override fun initAfterBinding() = with(binding) {
        // 뒤로가기 버튼을 누르는 경우
        buttonBack.setOnClickListener {
            finish()
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
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    // 서버에 로그아웃 인증코드 전송 요청
                    val response = TtukttakServer.logout(LogoutReq(userManager.getUserIdx()!!))
                    Log.i("response", response.toString())

                    if (response.isSuccess) {
                        userManager.clearUserIdx()
                        tokenManager.clearToken()
                        startActivityWithClear(LandingActivity::class.java)

                    } else {
                        withContext(Dispatchers.Main) {
                            when (response.code) {
                                400 -> {
                                    showToast("아이디나 비밀번호를 확인해주세요.")
                                }

                                500 -> {
                                    showToast(getString(R.string.unexpected_error_occurred))
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.e(LandingActivity.TAG, "서버 통신 오류: ${e.message}")
                        showToast("로그아웃 요청 실패")
                    }
                }
            }
        }
    }

}