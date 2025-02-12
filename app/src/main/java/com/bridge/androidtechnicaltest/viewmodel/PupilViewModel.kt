package com.bridge.androidtechnicaltest.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.*
import com.bridge.androidtechnicaltest.db.Pupil
import com.bridge.androidtechnicaltest.db.PupilRepository
import com.bridge.androidtechnicaltest.db.PupilUploadDto
import com.bridge.androidtechnicaltest.network.PupilApi
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PupilViewModel @Inject constructor(
    private val repo: PupilRepository,
    private val api: PupilApi
) : ViewModel() {
    private var _allPupils = repo.getOrFetchPupils()
    val allPupils: LiveData<List<Pupil>> get() = _allPupils
    private val _error: MutableLiveData<String> = MutableLiveData()
    val error: MutableLiveData<String> get() = _error
    private val _pupilData = MutableLiveData<Pupil>()
    val pupilData: LiveData<Pupil> get() = _pupilData

    fun addPupil(
        name: String,
        country: String,
        log: String,
        lat: String,
        imageUrl: String,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val pupil = Pupil(
                pupilId = 0,
                name = name,
                country = country,
                longitude = log.toDouble(),
                latitude = lat.toDouble(),
                image = imageUrl,
                uploaded = false
            )
            repo.insertPupil(pupil)
            uploadPupil(pupil)
        }
    }

    fun setPupil(pupil: Pupil) {
        _pupilData.value = pupil
    }

    fun deletePupilById(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deletePupil(id)
            deletePupilApi(id.toInt())
        }
    }

    @SuppressLint("CheckResult")
    fun fetchPupils() {
        viewModelScope.launch(Dispatchers.IO) {
            api.getPupils().subscribe({ response ->
                val newList = response.items.map { pupil ->
                    Pupil(
                        pupilId = pupil.pupilId,
                        name = pupil.name,
                        country = pupil.country,
                        longitude = pupil.longitude,
                        latitude = pupil.latitude,
                        image = pupil.image,
                        uploaded = true
                    )
                }
                viewModelScope.launch(Dispatchers.IO) {
                    repo.insertPupils(newList)
                }
            }, { error ->
                _allPupils = repo.getOrFetchPupils()
                _error.postValue("$error. Loading data from local DB")
            })
        }
    }

    @SuppressLint("CheckResult")
    fun uploadPupil(pupil: Pupil) {
        val pupilDto = PupilUploadDto.fromPupil(pupil)
        api.addPupil(pupilDto)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _error.postValue("Pupil added successfully");
            }, { error ->
                println("Error adding pupil: $error")
                _error.postValue("Error adding pupil: $error")
            })
    }

    @SuppressLint("CheckResult")
    fun deletePupilApi(pupilId: Int) {
        api.deletePupil(pupilId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _error.postValue("Pupil deleted successfully");
            }, { error ->
                _error.postValue("Error deleting pupil: $error")
            })
    }

    fun checkCacheAndUpload() {
        for (datum in allPupils.value?: arrayListOf()) {
            if(!datum.uploaded) uploadPupil(datum)
        }
    }
}
