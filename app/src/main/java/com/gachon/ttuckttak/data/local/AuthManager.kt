package com.gachon.ttuckttak.data.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.gachon.ttuckttak.data.remote.dto.auth.LoginRes
import com.gachon.ttuckttak.data.remote.dto.auth.RefreshRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * 암호화가 필요한 사용자의 정보를 저장하는 SharedPreference.
 * 사용자의 식별자, access token, refresh token을 관리하고 있다.
 *
 * Todo: 좀 더 높은 보안을 위해 Android KeyStore로 전환
 */
class AuthManager @Inject constructor(@ApplicationContext context: Context) {
    private val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
    private val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
    private val sharedPreferences = EncryptedSharedPreferences.create(
        "encryptedShared",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    /**
     * 사용자의 식별자, access token, refresh token을 저장하는 메소드
     *
     * @param data 로그인 결과 (사용자의 식별자, token type, access token, refresh token)
     */
    fun saveUserInfo(data: LoginRes) {
        val editor = sharedPreferences.edit()
        editor.putString("userIdx", data.userIdx)
        editor.putString("accessToken", data.tokenInfo.accessToken)
        editor.putString("refreshToken", data.tokenInfo.refreshToken)
        editor.apply()
    }

    /**
     * 사용자의 식별자를 반환하는 메소드
     *
     * @return 사용자의 식별자
     */
    fun getUserIdx(): String? {
        return sharedPreferences.getString("userIdx", null)
    }

    /**
     * 사용자의 access token을 반환하는 method
     *
     * @return 사용자의 access token
     */
    fun getAccessToken(): String? {
        return sharedPreferences.getString("accessToken", null)
    }

    /**
     * 사용자의 refresh token을 반환하는 method
     *
     * @return 사용자의 refresh token
     */
    fun getRefreshToken(): String? {
        return sharedPreferences.getString("refreshToken", null)
    }

    /**
     * 사용자의 토큰을 업데이트 하는 method
     *
     * @param data 사용자의 토큰 정보 (token type, access token, refresh token)
     */
    fun updateTokenInfo(data: RefreshRes) {
        val editor = sharedPreferences.edit()
        editor.putString("accessToken", data.accessToken)
        editor.putString("refreshToken", data.refreshToken)
        editor.apply()
    }

    /**
     * 사용자의 정보를 지우는 method
     */
    fun clearUserInfo() {
        val editor = sharedPreferences.edit()
        editor.remove("userIdx")
        editor.remove("accessToken")
        editor.remove("refreshToken")
        editor.apply()
    }
}