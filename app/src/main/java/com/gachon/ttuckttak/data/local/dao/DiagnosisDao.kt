package com.gachon.ttuckttak.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gachon.ttuckttak.data.local.entity.Diagnosis

@Dao
interface DiagnosisDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiagnosis(diagnosis: Diagnosis)

    @Query("SELECT * FROM user_diagnosis Where uid = :userIdx ORDER BY time DESC LIMIT 1")
    fun getLatestDiagnosis(userIdx: String): LiveData<Diagnosis?>

    @Query("SELECT * FROM user_diagnosis Where uid = :userIdx ORDER BY time DESC")
    fun getAllDiagnoses(userIdx: String): LiveData<List<Diagnosis>>

    @Query("DELETE FROM user_diagnosis Where uid = :userIdx")
    suspend fun deleteAllDiagnoses(userIdx: String)

}