package com.bridge.androidtechnicaltest.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bridge.androidtechnicaltest.R
import com.bridge.androidtechnicaltest.databinding.FragmentPupillistBinding
import com.bridge.androidtechnicaltest.viewmodel.PupilViewModel

class PupilListFragment : Fragment() {
    private lateinit var binding: FragmentPupillistBinding
    private val sharedViewModel: PupilViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPupillistBinding.inflate(inflater, container, false)
        sharedViewModel.pupilData.observe(viewLifecycleOwner) { pupil ->

        }
        return binding.root
    }
}