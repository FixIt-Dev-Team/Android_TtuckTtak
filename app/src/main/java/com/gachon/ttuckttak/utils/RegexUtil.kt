package com.gachon.ttuckttak.utils

import java.util.regex.Matcher
import java.util.regex.Pattern

object RegexUtil {
    fun isValidEmail(target: String?): Boolean {
        val regex = "^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9+-_.]+\\.[a-zA-Z0-9+-_.]+$"
        val pattern: Pattern = Pattern.compile(regex)
        val matcher: Matcher = pattern.matcher(target.toString())
        return matcher.matches()
    }

    fun isValidPw(target: String?): Boolean {
        val regex = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{9,}"
        val pattern: Pattern = Pattern.compile(regex)
        val matcher: Matcher = pattern.matcher(target.toString())
        return matcher.matches()
    }
}