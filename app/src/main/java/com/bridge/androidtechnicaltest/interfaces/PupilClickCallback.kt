package com.bridge.androidtechnicaltest.interfaces

import com.bridge.androidtechnicaltest.db.Pupil

interface PupilClickCallback {
    fun onPupilClick(pupil: Pupil)
}