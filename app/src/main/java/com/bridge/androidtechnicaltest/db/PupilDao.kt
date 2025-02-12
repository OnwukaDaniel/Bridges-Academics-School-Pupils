package com.bridge.androidtechnicaltest.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Single

@Dao
interface PupilDao {
    @Query("SELECT * FROM pupils WHERE pupil_id = :pupilId LIMIT 1")
    fun getPupilById(pupilId: Long): Pupil?

    @get:Query("SELECT * FROM Pupils ORDER BY name ASC")
    val pupils: LiveData<List<Pupil>>

    @Update
    fun updatePupil(pupil: Pupil)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPupils(pupils: List<Pupil>)

    @Insert
    fun insertPupil(pupil: Pupil)

    @Delete
    fun deletePupil(pupil: Pupil)

    @Query("DELETE FROM Pupils WHERE pupil_id = :pupilId")
    fun deletePupilById(pupilId: Long)
}