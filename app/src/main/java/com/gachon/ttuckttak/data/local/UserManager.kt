package com.gachon.ttuckttak.data.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import okhttp3.HttpUrl.Companion.toHttpUrl
import retrofit2.http.Url

class UserManager(context: Context) {
    private val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
    private val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
    private val sharedPreferences = EncryptedSharedPreferences.create(
        "encryptedShared",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveUserIdx(data: String) {
        val editor = sharedPreferences.edit()
        editor.putString("userIdx", data)
        editor.apply()
    }

    fun getUserIdx(): String? {
        return sharedPreferences.getString("userIdx", null)
    }

    fun clearUserIdx() {
        val editor = sharedPreferences.edit()
        editor.remove("userIdx")
        editor.apply()
    }

    fun saveUserName(data: String) {
        val editor = sharedPreferences.edit()
        editor.putString("userName", data)
        editor.apply()
    }

    fun getUserName(): String? {
        return sharedPreferences.getString("userName", null)
    }

    fun saveUserMail(data: String) {
        val editor = sharedPreferences.edit()
        editor.putString("userMail", data)
        editor.apply()
    }

    fun getUserMail(): String? {
        return sharedPreferences.getString("userMail", null)
    }

    fun saveUserImageUrl(data: String) {
        val editor = sharedPreferences.edit()
        editor.putString("userImageUrl", data)
        editor.apply()
    }

    fun saveUserImagePath(data: String) {
        val editor = sharedPreferences.edit()
        editor.putString("userImagePath", data)
        editor.apply()
    }

    fun getUserImageUrl(): String? {
        return sharedPreferences.getString("userImageUrl", null)
    }

    fun getUserImagePath(): String? {
        return sharedPreferences.getString("userImagePath", null)
    }
}