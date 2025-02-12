package com.bridge.androidtechnicaltest.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bridge.androidtechnicaltest.databinding.FragmentPupildetailBinding
import com.bridge.androidtechnicaltest.viewmodel.PupilViewModel
import com.bumptech.glide.Glide
import java.io.File

class PupilDetailFragment : Fragment() {
    private lateinit var binding: FragmentPupildetailBinding
    private val sharedViewModel: PupilViewModel by activityViewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPupildetailBinding.inflate(inflater, container, false)
        sharedViewModel.pupilData.observe(viewLifecycleOwner) { pupil ->
            binding.pupilId.text = pupil.pupilId.toString()
            binding.pupilName.text = pupil.name
            binding.country.text = pupil.country
            binding.location.text =
                "Log: " + pupil.longitude.toString() + ", Lat:" + pupil.latitude.toString()
            context?.let {
                Glide.with(it)
                    .load(File(pupil.image))
                    .into(binding.pupilImage)
            };
        }
        return binding.root
    }
}