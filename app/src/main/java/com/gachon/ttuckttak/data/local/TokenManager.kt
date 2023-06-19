package com.gachon.ttuckttak.data.local

import android.content.Context
import android.content.SharedPreferences
import com.gachon.ttuckttak.data.remote.dto.TokensDto

class TokenManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "TokenPrefs",
        Context.MODE_PRIVATE
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