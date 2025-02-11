package com.bridge.androidtechnicaltest.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bridge.androidtechnicaltest.R
import com.bridge.androidtechnicaltest.adapters.PupilsRecyclerViewAdapter
import com.bridge.androidtechnicaltest.databinding.ActivityMainBinding
import com.bridge.androidtechnicaltest.db.AppDatabase
import com.bridge.androidtechnicaltest.db.PupilRepository
import com.bridge.androidtechnicaltest.db.PupilViewModelFactory
import com.bridge.androidtechnicaltest.viewmodel.PupilViewModel
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var dialogImageView: ImageView
    private var adapter = PupilsRecyclerViewAdapter()
    private lateinit var viewModel: PupilViewModel
    private var uri: Uri? = null


    private var pickImageLauncher: ActivityResultLauncher<String> = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            this.uri = uri
            Glide.with(binding.root).load(this.uri!!).centerCrop().into(dialogImageView)
            message("Image selected.")
        } else {
            message("No image selected.")
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val database = AppDatabase.getInstance(this)
        val repo = PupilRepository(database.pupilDao())
        val factory = PupilViewModelFactory(repo)
        viewModel = ViewModelProvider(this, factory)[PupilViewModel::class.java]

        binding.btnAddPupil.setOnClickListener(this)
        binding.rvPupils.adapter = adapter
        binding.rvPupils.layoutManager = GridLayoutManager(applicationContext, 2)
        viewModel.allPupils.observe(this) { pupils ->
            adapter.dataset = pupils
            adapter.notifyDataSetChanged()
        }
        //viewModel = PupilViewModel(this)
    }

    @SuppressLint("NotifyDataSetChanged", "MissingInflatedId")
    override fun onClick(v: View?) {
        if (v?.id == R.id.btn_add_pupil) {
            val inflater = LayoutInflater.from(this)
            val dialogView: View = inflater.inflate(R.layout.dialog_add_pupil, null)

            val nameView = dialogView.findViewById<EditText>(R.id.dialog_pupil_name)
            val btnAdd = dialogView.findViewById<Button>(R.id.dialog_btn_add_pupil)
            dialogImageView = dialogView.findViewById(R.id.dialog_add_image)
            val countryView = dialogView.findViewById<EditText>(R.id.dialog_pupil_country)
            val logView = dialogView.findViewById<EditText>(R.id.dialog_pupil_log)
            val latView = dialogView.findViewById<EditText>(R.id.dialog_pupil_lat)
            dialogImageView.setOnClickListener { openImagePicker() }
            dialogView.setPadding(46, 46, 46, 46)
            val dialog: AlertDialog =
                AlertDialog.Builder(this).setTitle("Add new pupil").setView(dialogView)
                    .setNegativeButton("Cancel") { dialogInterface, _ ->
                        dialogInterface.dismiss()
                    }.create()
            dialog.show()
            btnAdd.setOnClickListener {
                val name: String = nameView.getText().toString().trim()
                val country: String = countryView.getText().toString().trim()
                val log: String = logView.getText().toString().trim()
                val lat: String = latView.getText().toString().trim()
                val valid = validateInput(name, country, log, lat)
                if (valid) {
                    viewModel.addPupil(name, country, log, lat, uri)
                    dialog.dismiss()
                }
            }
        }
    }

    fun validateInput(name: String, country: String, log: String, lat: String): Boolean {
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
        if (uri == null) {
            message("Image can't be empty")
            return false
        }
        return true
    }

    private fun message(msg: String) {
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG).show()
    }

    private fun openImagePicker() {
        pickImageLauncher.launch("image/*")
    }
}