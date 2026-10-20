package com.example.tugaspab

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tugaspab.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        const val EXTRA_NAMA = "extra_nama"
        const val EXTRA_EMAIL = "extra_email"
        const val EXTRA_WHATSAPP = "extra_whatsapp"
        const val EXTRA_KATEGORI = "extra_kategori"
        const val EXTRA_WORKSHOP = "extra_workshop"
        const val EXTRA_KOTA = "extra_kota"
        const val EXTRA_TANGGAL = "extra_tanggal"
        const val EXTRA_PENGALAMAN = "extra_pengalaman"
        const val EXTRA_PENGINGAT = "extra_pengingat"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupWatchers()
        setupButtons()
        updateProgress()
    }

    private fun setupWatchers() {
        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { updateProgress() }
            override fun afterTextChanged(s: Editable?) {}
        }

        binding.etNamaLengkap.addTextChangedListener(watcher)
        binding.etEmail.addTextChangedListener(watcher)
        binding.etWhatsapp.addTextChangedListener(watcher)

        binding.rgKategori.setOnCheckedChangeListener { _, _ -> updateProgress() }

        val spinnerListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) { updateProgress() }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        binding.spWorkshop.onItemSelectedListener = spinnerListener
        binding.spKota.onItemSelectedListener = spinnerListener
    }

    private fun updateProgress() {
        var completed = 0
        val totalFields = 6

        if (!binding.etNamaLengkap.text.isNullOrBlank()) completed++
        if (!binding.etEmail.text.isNullOrBlank() && Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString().trim()).matches()) completed++
        if (!binding.etWhatsapp.text.isNullOrBlank()) completed++
        if (binding.rgKategori.checkedRadioButtonId != -1) completed++
        if (binding.spWorkshop.selectedItemPosition > 0) completed++
        if (binding.spKota.selectedItemPosition > 0) completed++

        val percentage = ((completed.toDouble() / totalFields) * 100).toInt()
        binding.progressBarForm.progress = percentage
        binding.tvProgressLabel.text = getString(R.string.form_progress, percentage)
    }

    private fun setupButtons() {
        binding.btnDaftar.setOnClickListener {
            if (validateForm()) submitForm()
        }

        binding.btnReset.setOnClickListener {
            resetForm()
        }
    }

    private fun validateForm(): Boolean {
        var isValid = true

        binding.tilNama.error = null
        binding.tilEmail.error = null
        binding.tilWhatsapp.error = null

        val nama = binding.etNamaLengkap.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val whatsapp = binding.etWhatsapp.text.toString().trim()

        if (nama.isEmpty()) {
            binding.tilNama.error = getString(R.string.err_nama_required)
            isValid = false
        }

        if (email.isEmpty()) {
            binding.tilEmail.error = getString(R.string.err_email_required)
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = getString(R.string.err_email_invalid)
            isValid = false
        }

        if (whatsapp.isEmpty()) {
            binding.tilWhatsapp.error = getString(R.string.err_wa_required)
            isValid = false
        }

        if (binding.rgKategori.checkedRadioButtonId == -1) {
            Toast.makeText(this, getString(R.string.err_kategori_required), Toast.LENGTH_SHORT).show()
            isValid = false
        } else if (binding.spWorkshop.selectedItemPosition <= 0) {
            Toast.makeText(this, getString(R.string.err_workshop_required), Toast.LENGTH_SHORT).show()
            isValid = false
        } else if (binding.spKota.selectedItemPosition <= 0) {
            Toast.makeText(this, getString(R.string.err_kota_required), Toast.LENGTH_SHORT).show()
            isValid = false
        }

        return isValid
    }

    private fun submitForm() {
        val selectedRadio = findViewById<RadioButton>(binding.rgKategori.checkedRadioButtonId)
        val intent = Intent(this, DuaActivity::class.java).apply {
            putExtra(EXTRA_NAMA, binding.etNamaLengkap.text.toString().trim())
            putExtra(EXTRA_EMAIL, binding.etEmail.text.toString().trim())
            putExtra(EXTRA_WHATSAPP, binding.etWhatsapp.text.toString().trim())
            putExtra(EXTRA_KATEGORI, selectedRadio?.text?.toString() ?: "")
            putExtra(EXTRA_WORKSHOP, binding.spWorkshop.selectedItem.toString())
            putExtra(EXTRA_KOTA, binding.spKota.selectedItem.toString())
        }
        startActivity(intent)
    }

    private fun resetForm() {
        binding.etNamaLengkap.text?.clear()
        binding.etEmail.text?.clear()
        binding.etWhatsapp.text?.clear()

        binding.tilNama.error = null
        binding.tilEmail.error = null
        binding.tilWhatsapp.error = null

        binding.rgKategori.clearCheck()
        binding.spWorkshop.setSelection(0)
        binding.spKota.setSelection(0)

        updateProgress()
        Toast.makeText(this, getString(R.string.toast_form_reset), Toast.LENGTH_SHORT).show()
    }
}