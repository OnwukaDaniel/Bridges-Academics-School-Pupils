package com.bridge.androidtechnicaltest.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bridge.androidtechnicaltest.R
import com.bridge.androidtechnicaltest.adapters.PupilsRecyclerViewAdapter
import com.bridge.androidtechnicaltest.databinding.ActivityMainBinding
import com.bridge.androidtechnicaltest.db.AppDatabase
import com.bridge.androidtechnicaltest.db.Pupil
import com.bridge.androidtechnicaltest.db.PupilRepository
import com.bridge.androidtechnicaltest.db.PupilViewModelFactory
import com.bridge.androidtechnicaltest.interfaces.PupilClickCallback
import com.bridge.androidtechnicaltest.interfaces.PupilDeleteCallback
import com.bridge.androidtechnicaltest.live_data.NetworkLiveData
import com.bridge.androidtechnicaltest.network.PupilApi
import com.bridge.androidtechnicaltest.viewmodel.PupilViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), View.OnClickListener, PupilClickCallback,
    PupilDeleteCallback {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val sharedViewModel: PupilViewModel by viewModels()
    private var adapter = PupilsRecyclerViewAdapter()
    private lateinit var viewModel: PupilViewModel

    @Inject
    lateinit var api: PupilApi

    @Inject
    lateinit var db: AppDatabase

    @SuppressLint("NotifyDataSetChanged")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val repo = PupilRepository(db.pupilDao())
        val factory = PupilViewModelFactory(repo, api)
        viewModel = ViewModelProvider(this, factory)[PupilViewModel::class.java]
        viewModel.fetchPupils()
        binding.btnAddPupil.setOnClickListener(this)
        adapter.pupilClickCallback = this
        adapter.pupilDeleteCallback = this
        binding.rvPupils.adapter = adapter
        val layoutManager = GridLayoutManager(applicationContext, 2)
        binding.rvPupils.layoutManager = layoutManager
        val networkLiveData = NetworkLiveData(applicationContext)
        networkLiveData.observe(this) { isConnected ->
            if (isConnected) {
                viewModel.checkCacheAndUpload()
            } else {
                message("No internet connection")
            }
        }
        getData()
    }

    private fun getData() {
        viewModel.allPupils.observe(this@MainActivity) { pupils ->
            updateUI(pupils)
            viewModel.checkCacheAndUpload()
        }
        viewModel.error.observe(this@MainActivity) { msg ->
            message(msg)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateUI(items: List<Pupil>) {
        lifecycleScope.launch(Dispatchers.Main) {
            adapter.dataset = items
            adapter.notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged", "MissingInflatedId", "SetTextI18n")
    override fun onClick(v: View?) {
        if (v?.id == R.id.btn_add_pupil) {
            var imageUrl: String
            val inflater = LayoutInflater.from(this)
            val dialogView: View = inflater.inflate(R.layout.dialog_add_pupil, null)

            val nameView = dialogView.findViewById<EditText>(R.id.dialog_pupil_name)
            val btnAdd = dialogView.findViewById<Button>(R.id.dialog_btn_add_pupil)
            val pickImage = dialogView.findViewById<CardView>(R.id.pick_dummy_image)
            val countryView = dialogView.findViewById<EditText>(R.id.dialog_pupil_country)
            val logView = dialogView.findViewById<EditText>(R.id.dialog_pupil_log)
            val latView = dialogView.findViewById<EditText>(R.id.dialog_pupil_lat)
            val imageUrlInput = dialogView.findViewById<EditText>(R.id.dialog_pupil_url)
            dialogView.setPadding(46, 46, 46, 46)
            val dialog: AlertDialog =
                AlertDialog.Builder(this).setTitle("Add new pupil").setView(dialogView)
                    .setNegativeButton("Cancel") { dialogInterface, _ ->
                        dialogInterface.dismiss()
                    }.create()
            dialog.show()
            pickImage.setOnClickListener {
                imageUrlInput.setText("https://thumbs.dreamstime.com/z/d-character-schoolboy-backpack-ready-to-school-back-concept-pupil-isolated-realistic-boy-going-high-quality-photo-ai-349857457.jpg")
            }
            btnAdd.setOnClickListener {
                val name: String = nameView.getText().toString().trim()
                val country: String = countryView.getText().toString().trim()
                val log: String = logView.getText().toString().trim()
                val lat: String = latView.getText().toString().trim()
                imageUrl = imageUrlInput.getText().toString().trim()
                val valid = validateInput(name, country, log, lat, imageUrl)
                if (valid) {
                    viewModel.addPupil(name, country, log, lat, imageUrl)
                    dialog.dismiss()
                }
            }
        }
    }

    private fun validateInput(
        name: String, country: String, log: String, lat: String, imageUrl: String
    ): Boolean {
        if (name.isEmpty()) {
            message("Name can't be empty")
            return false
        }
        if (country.isEmpty()) {
            message("Country can't be empty")
            return false
        }
        if (log.isEmpty()) {
            message("Longitude can't be empty")
            return false
        }
        if (lat.isEmpty()) {
            message("Latitude can't be empty")
            return false
        }
        if (imageUrl.isEmpty()) {
            message("Image can't be empty")
            return false
        }
        return true
    }

    private fun message(msg: String) {
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG).show()
    }

    override fun onPupilClick(pupil: Pupil) {
        val frag = PupilDetailFragment()
        sharedViewModel.setPupil(pupil)
        supportFragmentManager.beginTransaction().addToBackStack("images")
            .replace(R.id.main_layout, frag).commit()
    }

    override fun onDeleteClicked(pupil: Pupil) {
        viewModel.deletePupilById(pupil.pupilId)
    }
}