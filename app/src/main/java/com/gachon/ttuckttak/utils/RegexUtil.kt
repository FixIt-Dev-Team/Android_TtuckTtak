package com.gachon.ttuckttak.utils

import java.util.regex.Matcher
import java.util.regex.Pattern

object RegexUtil {
    fun isValidPwFormat(target: String?): Boolean {
        val regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*\\W).{9,500}\$"
        val pattern: Pattern = Pattern.compile(regex)
        val matcher: Matcher = pattern.matcher(target.toString())
        return matcher.matches()
    }

    fun isValidNicknameFormat(target: String?): Boolean {
        val regex = ".{4,11}" // 4글자 이상, 12글자 미만
        val pattern: Pattern = Pattern.compile(regex)
        val matcher: Matcher = pattern.matcher(target.toString())
        return matcher.matches()
    }
}