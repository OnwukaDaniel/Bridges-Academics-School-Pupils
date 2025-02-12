package com.bridge.androidtechnicaltest.viewmodel

import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bridge.androidtechnicaltest.db.Pupil
import com.bridge.androidtechnicaltest.db.PupilRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PupilViewModel(private val repo: PupilRepository) : ViewModel() {
    private var _allPupils = MutableLiveData<List<Pupil>>()
    val allPupils: LiveData<List<Pupil>> get() = _allPupils
    private val _error: MutableLiveData<String> = MutableLiveData()
    val error: MutableLiveData<String> get() = MutableLiveData()
    private val _pupilData = MutableLiveData<Pupil>()
    val pupilData: LiveData<Pupil> get() = _pupilData

    init {
        fetchPupils()
    }

    private fun fetchPupils() {
        viewModelScope.launch(Dispatchers.IO) {
            _allPupils.postValue(repo.getOrFetchPupils())
        }
    }

    fun addPupil(
        name: String,
        country: String,
        log: String,
        lat: String,
        uri: Uri?,
        contentResolver: ContentResolver
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val pupil = Pupil(
                pupilId = 0,
                name = name,
                country = country,
                longitude = log.toDouble(),
                latitude = lat.toDouble(),
                image = (getRealPathFromUri(contentResolver, uri!!)?: "").toString(),
            )
            repo.insertPupil(pupil)
        }
        fetchPupils()
    }

    fun setError(msg: String) {
        _error.postValue(msg)
    }

    fun setPupil(pupil: Pupil) {
        _pupilData.value = pupil
    }

    private fun getRealPathFromURI(contentResolver: ContentResolver, contentUri: Uri): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(contentUri, projection, null, null, null)

        cursor?.use {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            if (it.moveToFirst()) {
                return it.getString(columnIndex)
            }
        }
        return contentUri.toString()
    }

    private fun getRealPathFromUri(contentResolver: ContentResolver, uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null) ?: return null
        val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val filePath = cursor.getString(columnIndex)
        cursor.close()
        return filePath
    }


}