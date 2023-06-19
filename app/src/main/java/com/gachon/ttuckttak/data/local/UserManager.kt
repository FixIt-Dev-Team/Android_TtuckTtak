package com.gachon.ttuckttak.data.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

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
}