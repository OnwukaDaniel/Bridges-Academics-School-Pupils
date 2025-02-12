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
    private var _allPupils = repo.getOrFetchPupils()
    val allPupils: LiveData<List<Pupil>> get() = _allPupils
    private val _error: MutableLiveData<String> = MutableLiveData()
    private val _pupilData = MutableLiveData<Pupil>()
    val pupilData: LiveData<Pupil> get() = _pupilData

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
                uploaded = false,
            )
            repo.insertPupil(pupil)
        }
    }

    fun setPupil(pupil: Pupil) {
        _pupilData.value = pupil
    }

    fun deletePupilById(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deletePupil(id)
        }
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