package com.gachon.ttuckttak.data.local

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * 암호화가 필요하지 않은 사용자의 정보를 저장하는 SharedPreference.
 * 현재는 회원가입 시 사용될 이메일과 비밀번호 초기화할 때 사용될 이메일을 관리하고 있다.
 */
class UserManager @Inject constructor(@ApplicationContext context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "sharedPreferences",
        Context.MODE_PRIVATE
    )

    /**
     * 회원가입시 사용될 이메일을 저장하는 method
     *
     * @param email 회원가입시 사용될 이메일
     */
    fun saveRegistrationEmail(email: String) {
        val editor = sharedPreferences.edit()
        editor.putString("registrationEmail", email)
        editor.apply()
    }

    /**
     * 회원가입시 사용될 이메일을 가져오는 method
     *
     * @return 회원가입시 사용될 이메일
     */
    fun getRegistrationEmail(): String? {
        return sharedPreferences.getString("registrationEmail", null)
    }

    /**
     * 비밀번호 초기화에 사용될 이메일을 저장하는 method
     *
     * @param email 비밀번호 초기화에 사용될 이메일
     */
    fun savePasswordResetEmail(email: String) {
        val editor = sharedPreferences.edit()
        editor.putString("resetPasswordEmail", email)
        editor.apply()
    }

    /**
     * 비밀번호 초기화에 사용될 이메일을 가져오는 method
     *
     * @return 비밀번호 초기화에 사용될 이메일
     */
    fun getPasswordResetEmail(): String? {
        return sharedPreferences.getString("resetPasswordEmail", null)
    }
}
