package com.gachon.ttuckttak.ui.login

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.gachon.ttuckttak.BuildConfig
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.databinding.ActivityLoginBinding
import com.kakao.sdk.common.KakaoSdk

class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {

    private val TAG = "LOGIN"
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY) // Local.properties에서 값을 가져와 초기화하기
    }

    override fun initAfterBinding() = with(binding) {
        buttonKakaoLogin.setOnClickListener {
            viewModel.requestKakaoAuthorizationCode(
                onSuccess = { authorizationCode ->
                    viewModel.loginWithKakao(
                        authorizationCode,
                        onSuccess = {
                            Log.i(TAG, "카카오톡 인가 코드를 사용하여 뚝딱 서비스에 로그인 하였습니다")
                            Toast.makeText(this@LoginActivity, "로그인 성공", Toast.LENGTH_SHORT).show()
                            // Todo: 다음 페이지로 넘어가기
                        },
                        onFailure = { message ->
                            Log.e(TAG, "카카오톡 인가 코드를 사용하여 뚝딱 서비스에 로그인 하는 데 실패하였습니다")
                            Log.e(TAG, message)
                            Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                onFailure = { message ->
                    Log.e(TAG, "카카오톡 인가 코드를 받는 데 실패하였습니다")
                    Log.e(TAG, message)
                    Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}