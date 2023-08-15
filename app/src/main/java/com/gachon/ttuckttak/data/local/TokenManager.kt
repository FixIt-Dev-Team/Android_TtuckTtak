package com.gachon.ttuckttak.data.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.gachon.ttuckttak.data.remote.dto.auth.TokensDto

class TokenManager(context: Context) {
    private val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
    private val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
    private val sharedPreferences = EncryptedSharedPreferences.create(
        "encryptedShared",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveToken(data: TokensDto) {
        val editor = sharedPreferences.edit()
        editor.putString("grantType", data.grantType)
        editor.putString("accessToken", data.accessToken)
        editor.putString("refreshToken", data.refreshToken)
        editor.apply()
    }

    fun getAccessToken(): String? {
        return sharedPreferences.getString("accessToken", null)
    }

    fun getRefreshToken(): String? {
        return sharedPreferences.getString("refreshToken", null)
    }

    fun resetAccessToken(newToken: String?) {
        val editor = sharedPreferences.edit()
        editor.putString("accessToken", newToken)
        editor.apply()
    }

    fun resetRefreshToken(newToken: String?) {
        val editor = sharedPreferences.edit()
        editor.putString("refreshToken", newToken)
        editor.apply()
    }

    fun clearToken() {
        val editor = sharedPreferences.edit()
        editor.remove("grantType")
        editor.remove("accessToken")
        editor.remove("refreshToken")
        editor.apply()
    }
}