package com.gachon.ttuckttak.ui.setting

import android.content.Intent
import android.util.Log
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.data.local.TokenManager
import com.gachon.ttuckttak.data.local.UserManager
import com.gachon.ttuckttak.data.remote.TtukttakServer
import com.gachon.ttuckttak.data.remote.dto.auth.LogoutReq
import com.gachon.ttuckttak.data.remote.dto.view.UserInfoRes
import com.gachon.ttuckttak.databinding.ActivitySettingsBinding
import com.gachon.ttuckttak.ui.login.LandingActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception


class SettingsActivity : BaseActivity<ActivitySettingsBinding>(ActivitySettingsBinding::inflate) {

    private val userManager: UserManager by lazy { UserManager(this@SettingsActivity) }
    private val tokenManager: TokenManager by lazy { TokenManager(this@SettingsActivity) }

    private var pushStatus = false
    private var nightPushStatus = false

    override fun initAfterBinding() = with(binding) {
        setUi()
        setClickListener()
    }

    private fun setUi() = with(binding) {
        getProfile(userManager.getUserIdx()!!, tokenManager.getAccessToken()!!) // 서버에서 유저 정보 가져오기

        textviewUserName.text = userManager.getUserName()
        textviewUserEmail.text = userManager.getUserMail()

        if (userManager.getUserImageUrl().isNullOrEmpty()) {
            imageviewProfile.setImageDrawable(AppCompatResources.getDrawable(this@SettingsActivity, R.drawable.img_profile))
            Log.i(TAG, "프로필 이미지 로딩 실패")

        } else {
            Glide.with(this@SettingsActivity)
                .load(userManager.getUserImageUrl())
                .into(imageviewProfile)
        }
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
            val intent = Intent(this@SettingsActivity, SettingsAlertActivity::class.java).apply {
                putExtra("pushStatus", pushStatus)
                putExtra("nightPushStatus", nightPushStatus)
            }

            startActivity(intent)
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

    private fun getProfile(userId: String, token: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = TtukttakServer.getUserInfo(userId, token)

                withContext(Dispatchers.Main) {
                    if (response.isSuccess) {
                        val data = response.data!!
                        Log.i(TAG, response.toString())
                        saveProfile(data)

                        pushStatus = response.data.pushStatus
                        nightPushStatus = response.data.nightPushStatus

                    } else {
                        Log.e(TAG, "유저 정보 취득 실패")
                        Log.e(TAG, "${response.code} ${response.message}")
                        showToast("유저 정보 취득 실패")
                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e(TAG, "서버 통신 오류: ${e.message}")
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

    companion object {
        const val TAG = "PROFILE"
    }

}