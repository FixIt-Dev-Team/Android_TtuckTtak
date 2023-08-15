package com.gachon.ttuckttak.data.local

import android.content.Context
import com.gachon.ttuckttak.R

class SolutionManager(context: Context) {
    val solutions: List<List<String>> = listOf(listOf(),
        listOf("0", "10"),
        listOf("1"),
        listOf("1", "01", "001"),
        listOf("1", "01", "00111", "00110", "0001", "00001")
    )

    val questions: List<Map<String, Int>> = listOf(mapOf(),
        mapOf("1" to R.string.power_prob_2, "11" to R.string.power_prob_3),
        mapOf(),
        mapOf("0" to R.string.acc_prob_2, "00" to R.string.acc_prob_3),
        mapOf("0" to R.string.usage_prob_2, "00" to R.string.usage_prob_3, "001" to R.string.usage_prob_4, "0011" to R.string.usage_prob_5, "000" to R.string.usage_prob_6, "0000" to R.string.usage_prob_7)
    )

    fun getCategory(surveyIdx: Int) : Int = when(surveyIdx) {
        POWER -> R.string.power
        DISPLAY -> R.string.display
        ACC -> R.string.acc
        USAGE -> R.string.usage
        else -> -1
    }

    fun isSolution(surveyIdx: Int, pattern: String): Boolean {
        if (pattern in solutions[surveyIdx]) {
            return true
        }
        return false
    }

    fun isQuestion(surveyIdx: Int, pattern: String): Boolean {
        if (pattern in questions[surveyIdx]) {
            return true
        }
        return false
    }

    fun getSubtitle(surveyIdx: Int, pattern: String): Int {
        return questions[surveyIdx][pattern]!!
    }

    fun convertPattern(surveyIdx: Int, pattern: String): String {
        var len = 0
        when (surveyIdx) {
            POWER -> len = 2
            DISPLAY -> len = 1
            ACC -> len = 3
            USAGE -> len = 5
        }

        return pattern + ("0".repeat(len - pattern.length))
    }

    companion object CATEGORY {
        const val POWER = 1
        const val DISPLAY = 2
        const val ACC = 3
        const val USAGE = 4
    }
}