package com.bridge.androidtechnicaltest.db

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bridge.androidtechnicaltest.viewmodel.PupilViewModel

class PupilViewModelFactory(private val repository: PupilRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PupilViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PupilViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
