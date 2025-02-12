package com.bridge.androidtechnicaltest.db

import androidx.lifecycle.LiveData
import com.bridge.androidtechnicaltest.network.PupilApi
import io.reactivex.Single
import javax.inject.Inject

class PupilRepository @Inject constructor(
    private val pupilDao: PupilDao // Assuming you have a Room DAO
) {

    fun getOrFetchPupils(): LiveData<List<Pupil>> = pupilDao.pupils

    fun insertPupil(pupil: Pupil){
        return pupilDao.insertPupil(pupil)
    }

    fun insertPupils(pupils: List<Pupil>){
        return pupilDao.insertPupils(pupils)
    }

    fun deletePupil(id: Long){
        return pupilDao.deletePupilById(id)
    }

    fun updatePupil(pupil: Pupil) = pupilDao.updatePupil(pupil)
}