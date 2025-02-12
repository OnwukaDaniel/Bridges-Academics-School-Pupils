package com.bridge.androidtechnicaltest.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.bridge.androidtechnicaltest.R
import com.bridge.androidtechnicaltest.databinding.FragmentPupildetailBinding
import com.bridge.androidtechnicaltest.db.AppDatabase
import com.bridge.androidtechnicaltest.db.Pupil
import com.bridge.androidtechnicaltest.db.PupilRepository
import com.bridge.androidtechnicaltest.db.PupilViewModelFactory
import com.bridge.androidtechnicaltest.network.PupilApi
import com.bridge.androidtechnicaltest.viewmodel.PupilViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class PupilDetailFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentPupildetailBinding
    private lateinit var viewModel: PupilViewModel
    private val sharedViewModel: PupilViewModel by activityViewModels()
    private lateinit var pupil: Pupil

    @Inject
    lateinit var api: PupilApi

    @Inject
    lateinit var db: AppDatabase

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPupildetailBinding.inflate(inflater, container, false)
        sharedViewModel.pupilData.observe(viewLifecycleOwner) { pupil ->
            this.pupil = pupil
            binding.pupilId.text = pupil.pupilId.toString()
            binding.pupilName.text = pupil.name
            binding.country.text = pupil.country
            binding.location.text =
                "Log: " + pupil.longitude.toString() + ", Lat:" + pupil.latitude.toString()
            context?.let {
                Glide.with(it)
                    .load(File(pupil.image))
                    .centerCrop()
                    .error(R.drawable.error_image)
                    .into(binding.pupilImage)
            };
        }
        binding.pupilDetailsBack.setOnClickListener(this)
        binding.detailDelete.setOnClickListener(this)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val database = AppDatabase.getInstance(requireContext())
        val repo = PupilRepository(database.pupilDao())
        val factory = PupilViewModelFactory(repo, api)
        viewModel = ViewModelProvider(this, factory)[PupilViewModel::class.java]
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().supportFragmentManager.popBackStack()
            }
        })
    }

    override fun onClick(v: View?) {
        if (v != null) {
            if(v.id == R.id.pupil_details_back) {
                requireActivity().supportFragmentManager.popBackStack()
            } else if(v.id == R.id.detail_delete) deleteDialog()
        }
    }

    private fun deleteDialog() {
        val inflater = LayoutInflater.from(context)
        val dialogView: View = inflater.inflate(R.layout.dialog_delete_confirmation, null)
        dialogView.setPadding(46, 46, 46, 46)
        val dialog: AlertDialog =
            AlertDialog.Builder(context)
                .setView(dialogView)
                .setNegativeButton("Cancel") { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
                .setPositiveButton("Delete") { dialogInterface, _ ->
                    dialogInterface.dismiss()
                    viewModel.deletePupilById(pupil.pupilId)
                    requireActivity().supportFragmentManager.popBackStack()
                }.create()
        dialog.show()
    }
}