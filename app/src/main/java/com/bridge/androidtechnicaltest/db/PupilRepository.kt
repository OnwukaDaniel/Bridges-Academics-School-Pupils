package com.bridge.androidtechnicaltest.db

import androidx.lifecycle.LiveData
import com.bridge.androidtechnicaltest.network.PupilApi
import io.reactivex.Single

class PupilRepository(private val pupilDao: PupilDao) {

    fun getOrFetchPupils(): LiveData<List<Pupil>> = pupilDao.pupils

    fun insertPupil(pupil: Pupil){
        return pupilDao.insertPupil(pupil)
    }

    fun deletePupil(id: Long){
        return pupilDao.deletePupilById(id)
    }
}