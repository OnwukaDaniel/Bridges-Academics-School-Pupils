package com.bridge.androidtechnicaltest.db

import com.bridge.androidtechnicaltest.network.PupilApi
import io.reactivex.Single

class PupilRepository(private val pupilDao: PupilDao) {

    fun getOrFetchPupils(): List<Pupil> {
        return pupilDao.pupils
    }

    fun insertPupil(pupil: Pupil){
        return pupilDao.insertPupil(pupil)
    }
}