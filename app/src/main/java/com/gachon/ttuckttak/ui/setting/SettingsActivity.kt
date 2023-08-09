package com.gachon.ttuckttak.ui.setting

import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.data.local.TokenManager
import com.gachon.ttuckttak.data.local.UserManager
import com.gachon.ttuckttak.data.remote.TtukttakServer
import com.gachon.ttuckttak.data.remote.dto.UserInfoRes
import com.gachon.ttuckttak.databinding.ActivitySettingsBinding
import com.gachon.ttuckttak.ui.login.LoginActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception


class SettingsActivity : BaseActivity<ActivitySettingsBinding>(ActivitySettingsBinding::inflate) {

    private val userManager: UserManager by lazy { UserManager(this@SettingsActivity) }
    private val tokenManager: TokenManager by lazy { TokenManager(this@SettingsActivity) }

    override fun initAfterBinding() = with(binding) {
        // 서버에서 유저 정보 가져오기
        getProfile(userManager.getUserIdx()!!, tokenManager.getAccessToken()!!)
        textviewUserName.text = userManager.getUserName()
        textviewUserEmail.text = userManager.getUserMail()

        setClickListener()
    }

    private fun setClickListener() = with(binding) {
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
            userManager.clearUserIdx()
            tokenManager.clearToken()
            startActivityWithClear(LoginActivity::class.java)
        }
    }

    private fun getProfile(userId: String, token: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = TtukttakServer.getUserInfo(userId, token)

                withContext(Dispatchers.Main) {
                    if (response.isSuccess) {
                        val data = response.data!!
                        Log.i(SettingsProfileActivity.TAG, "userName: ${data.userName}")
                        Log.i(SettingsProfileActivity.TAG, "userMail: ${data.email}")
                        Log.i(SettingsProfileActivity.TAG, "userImgUrl: ${data.profileImgUrl}")
                        Log.i(SettingsProfileActivity.TAG, "accountType: ${data.accountType}")
                        saveProfile(data)
                    } else {
                        Log.e(SettingsProfileActivity.TAG, "유저 정보 취득 실패")
                        Log.e(SettingsProfileActivity.TAG, "${response.code} ${response.message}")
                        showToast("유저 정보 취득 실패")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e(SettingsProfileActivity.TAG, "서버 통신 오류: ${e.message}")
                    showToast("유저 정보 취득 실패")
                }
            }
        }
    }

    private fun saveProfile(data: UserInfoRes) {
        userManager.saveUserName(data.userName)
        userManager.saveUserMail(data.email)
        userManager.saveUserImageUrl(data.profileImgUrl)
    }

}