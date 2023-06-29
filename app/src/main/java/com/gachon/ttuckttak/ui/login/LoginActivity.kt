package com.gachon.ttuckttak.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.gachon.ttuckttak.BuildConfig
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.data.local.TokenManager
import com.gachon.ttuckttak.data.local.UserManager
import com.gachon.ttuckttak.databinding.ActivityLoginBinding
import com.gachon.ttuckttak.data.remote.dto.LoginRes
import com.kakao.sdk.common.KakaoSdk

// FixMe: Deprecated된 함수들 최신화

class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {

    private val userManager: UserManager by lazy { UserManager(this) }
    private val tokenManager: TokenManager by lazy { TokenManager(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY) // Local.properties에서 값을 가져와 초기화하기
    }

    override fun initAfterBinding() {
        binding.buttonKakaoLogin.setOnClickListener {
            val intent = Intent(this, KakaoLoginWebViewActivity::class.java)
            startActivityForResult(intent, REQ_CODE_KAKAO_AUTH_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQ_CODE_KAKAO_AUTH_CODE && resultCode == Activity.RESULT_OK) {
            val authCode = data?.getStringExtra("authCode")
            if (authCode != null) {
                Log.i(TAG, "Received kakao auth code: $authCode")
            }
        }
    }

    /**
     * 뚝딱 서비스의 사용자 식별자와 토큰 정보를 앱 내에 저장
     */
    private fun saveInfo(data: LoginRes) {
        userManager.saveUserIdx(data.userIdx)
        tokenManager.saveToken(data.tokenInfo)
    }

    companion object {
        const val REQ_CODE_KAKAO_AUTH_CODE = 100
        const val TAG = "LOGIN"
    }
}