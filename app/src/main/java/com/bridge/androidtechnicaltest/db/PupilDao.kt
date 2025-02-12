package com.bridge.androidtechnicaltest.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Single

@Dao
interface PupilDao {
    @get:Query("SELECT * FROM Pupils ORDER BY name ASC")
    val pupils: LiveData<List<Pupil>>

    @Insert
    fun insertPupil(pupil: Pupil)

    @Delete
    fun deletePupil(pupil: Pupil)

    @Query("DELETE FROM Pupils WHERE pupil_id = :pupilId")
    fun deletePupilById(pupilId: Long)
}