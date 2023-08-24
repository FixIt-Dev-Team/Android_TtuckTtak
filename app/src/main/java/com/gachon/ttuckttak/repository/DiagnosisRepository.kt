package com.gachon.ttuckttak.repository

import com.gachon.ttuckttak.data.local.entity.Diagnosis

interface DiagnosisRepository {

    suspend fun getDiagnosis(): Diagnosis?
}