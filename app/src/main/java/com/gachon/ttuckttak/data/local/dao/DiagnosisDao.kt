package com.gachon.ttuckttak.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.gachon.ttuckttak.data.local.entity.Diagnosis

@Dao
interface DiagnosisDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDiagnosis(diagnosis: Diagnosis)

    @Query("SELECT * FROM Diagnosis")
    fun getDiagnosis(): Diagnosis?

    @Update
    fun updateDiagnosis(diagnosis: Diagnosis)

    @Update
    fun updateTime(diagnosis: Diagnosis)

    @Delete
    fun deleteDiagnosis(diagnosis: Diagnosis)
}