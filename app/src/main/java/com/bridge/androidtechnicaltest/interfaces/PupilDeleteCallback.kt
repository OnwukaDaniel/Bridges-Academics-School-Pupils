package com.bridge.androidtechnicaltest.interfaces

import com.bridge.androidtechnicaltest.db.Pupil

interface PupilDeleteCallback {
    fun onDeleteClicked(pupil: Pupil)
}