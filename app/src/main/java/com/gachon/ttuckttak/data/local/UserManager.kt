package com.gachon.ttuckttak.data.local

import android.content.Context
import android.content.SharedPreferences

class UserManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "UserPrefs",
        Context.MODE_PRIVATE
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