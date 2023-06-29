package com.gachon.ttuckttak.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.gachon.ttuckttak.BuildConfig
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.base.BaseResponse
import com.gachon.ttuckttak.data.local.TokenManager
import com.gachon.ttuckttak.data.local.UserManager
import com.gachon.ttuckttak.data.remote.TtukttakServer
import com.gachon.ttuckttak.databinding.ActivityLoginBinding
import com.gachon.ttuckttak.data.remote.dto.LoginRes
import com.kakao.sdk.common.KakaoSdk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
                loginWithOauth(param = authCode, method = LoginMethod.KAKAO)
            }
        }
    }

    private fun loginWithOauth(param: String, method: LoginMethod) {
        CoroutineScope(Dispatchers.IO).launch {
            val result: BaseResponse<LoginRes> = when (method) {
                LoginMethod.KAKAO -> TtukttakServer.loginWithKakao(authCode = param)
            }

            withContext(Dispatchers.Main) {
                if (result.isSuccess) {
                    Log.d(TAG, result.data!!.toString())
                    saveInfo(data = result.data)
                } else {
                    Log.e(TAG, "로그인 실패!")
                    Toast.makeText(this@LoginActivity, "로그인 하는데 실패하였습니다.", Toast.LENGTH_SHORT).show()
                }
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

    enum class LoginMethod {
        KAKAO
    }
}