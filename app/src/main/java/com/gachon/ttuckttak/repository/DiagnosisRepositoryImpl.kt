package com.gachon.ttuckttak.repository

import com.gachon.ttuckttak.data.local.dao.DiagnosisDao
import com.gachon.ttuckttak.data.local.entity.Diagnosis
import javax.inject.Inject

class DiagnosisRepositoryImpl @Inject constructor(private val diagnosisDao: DiagnosisDao) : DiagnosisRepository {

    override suspend fun getDiagnosis(): Diagnosis? {
        return diagnosisDao.getDiagnosis()
    }
}