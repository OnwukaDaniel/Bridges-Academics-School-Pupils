package com.bridge.androidtechnicaltest.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Single

@Dao
interface PupilDao {
    @get:Query("SELECT * FROM Pupils ORDER BY name ASC")
    val pupils: List<Pupil>

    @Insert
    fun insertPupil(pupil: Pupil)
}