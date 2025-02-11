package com.bridge.androidtechnicaltest.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bridge.androidtechnicaltest.db.AppDatabase
import com.bridge.androidtechnicaltest.db.Pupil
import com.bridge.androidtechnicaltest.db.PupilRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PupilViewModel(private val repo: PupilRepository) : ViewModel() {
    private var _allPupils = MutableLiveData<List<Pupil>>()
    val allPupils: LiveData<List<Pupil>> get() = _allPupils
    private val _error: MutableLiveData<String> = MutableLiveData()
    val error: MutableLiveData<String> get() = MutableLiveData()

    init {
        fetchPupils()
    }

    private fun fetchPupils() {
        viewModelScope.launch(Dispatchers.IO) {
            _allPupils.postValue(repo.getOrFetchPupils())
        }
    }

    fun addPupil(name: String, country: String, log: String, lat: String, uri: Uri?) {
        viewModelScope.launch(Dispatchers.IO) {
            val pupil = Pupil(
                pupilId = 0,
                name = name,
                country = country,
                longitude = log.toDouble(),
                latitude = lat.toDouble(),
                image = uri!!.path!!,
            )
            repo.insertPupil(pupil)
        }
        fetchPupils()
    }

    fun setError(msg: String) {
        _error.postValue(msg)
    }
}