package com.example.pab_2301

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import java.util.Calendar

class MainActivity : Activity() {

    private lateinit var edtNama: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtTelepon: EditText
    private lateinit var rgJk: RadioGroup
    private lateinit var rbLaki: RadioButton
    private lateinit var rbPerempuan: RadioButton
    private lateinit var spKota: Spinner
    private lateinit var tvTanggalLahir: TextView
    private lateinit var spBidangMinat: Spinner
    private lateinit var tvMinatValue: TextView
    private lateinit var sbMinat: SeekBar
    private lateinit var swNotifikasi: Switch
    private lateinit var cbSyarat: CheckBox
    private lateinit var tvProgressPercentage: TextView
    private lateinit var pbFormProgress: ProgressBar
    private lateinit var btnSimpan: Button
    private lateinit var btnReset: Button
    private lateinit var navRingkasan: LinearLayout

    private val daftarKota = arrayOf(
        "Pilih Kota Asal", "Jakarta", "Bandung", "Semarang",
        "Yogyakarta", "Surabaya", "Bogor", "Medan", "Padang", "Makassar"
    )

    private val daftarBidang = arrayOf(
        "Pilih Bidang Minat", "Teknologi Informasi", "Desain UI/UX",
        "Data Analysis", "Manajemen Proyek", "Cyber Security"
    )

    private var selectedDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupSpinners()
        setupDatePicker()
        setupListeners()
        loadExistingData()
        updateProgress()
    }

    private fun initViews() {
        edtNama = findViewById(R.id.edtNama)
        edtEmail = findViewById(R.id.edtEmail)
        edtTelepon = findViewById(R.id.edtTelepon)
        rgJk = findViewById(R.id.rgJk)
        rbLaki = findViewById(R.id.rbLaki)
        rbPerempuan = findViewById(R.id.rbPerempuan)
        spKota = findViewById(R.id.spKota)
        tvTanggalLahir = findViewById(R.id.tvTanggalLahir)
        spBidangMinat = findViewById(R.id.spBidangMinat)
        tvMinatValue = findViewById(R.id.tvMinatValue)
        sbMinat = findViewById(R.id.sbMinat)
        swNotifikasi = findViewById(R.id.swNotifikasi)
        cbSyarat = findViewById(R.id.cbSyarat)
        tvProgressPercentage = findViewById(R.id.tvProgressPercentage)
        pbFormProgress = findViewById(R.id.pbFormProgress)
        btnSimpan = findViewById(R.id.btnSimpan)
        btnReset = findViewById(R.id.btnReset)
        navRingkasan = findViewById(R.id.navRingkasan)
    }

    private fun setupSpinners() {
        val adapterKota = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, daftarKota)
        spKota.adapter = adapterKota

        val adapterBidang = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, daftarBidang)
        spBidangMinat.adapter = adapterBidang
    }

    private fun setupDatePicker() {
        tvTanggalLahir.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    tvTanggalLahir.text = selectedDate
                    updateProgress()
                },
                year, month, day
            )
            datePickerDialog.show()
        }
    }

    private fun setupListeners() {
        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateProgress()
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        edtNama.addTextChangedListener(watcher)
        edtEmail.addTextChangedListener(watcher)
        edtTelepon.addTextChangedListener(watcher)

        rgJk.setOnCheckedChangeListener { _, _ -> updateProgress() }
        cbSyarat.setOnCheckedChangeListener { _, _ -> updateProgress() }

        spKota.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateProgress()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spBidangMinat.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateProgress()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        sbMinat.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val value = if (progress < 1) 1 else progress
                tvMinatValue.text = "Skala: $value / 10"
                updateProgress()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        btnSimpan.setOnClickListener {
            val valid = validateForm()
            if (valid) {
                val data = createFormDataFromInputs()
                FormDataRepository.currentData = data

                Toast.makeText(this, "Pendaftaran berhasil disimpan!", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, DuaActivity::class.java)
                startActivity(intent)
            }
        }

        btnReset.setOnClickListener {
            showResetConfirmationDialog()
        }

        navRingkasan.setOnClickListener {
            val intent = Intent(this, DuaActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createFormDataFromInputs(): FormData {
        val jenisKelamin = when (rgJk.checkedRadioButtonId) {
            R.id.rbLaki -> "Laki-laki"
            R.id.rbPerempuan -> "Perempuan"
            else -> ""
        }

        val kota = if (spKota.selectedItemPosition > 0) spKota.selectedItem.toString() else ""
        val bidang = if (spBidangMinat.selectedItemPosition > 0) spBidangMinat.selectedItem.toString() else ""

        return FormData(
            nama = edtNama.text.toString().trim(),
            email = edtEmail.text.toString().trim(),
            telepon = edtTelepon.text.toString().trim(),
            jenisKelamin = jenisKelamin,
            kotaAsal = kota,
            tanggalLahir = selectedDate,
            bidangMinat = bidang,
            tingkatMinat = if (sbMinat.progress < 1) 1 else sbMinat.progress,
            setujuSK = cbSyarat.isChecked,
            notifikasiAktif = swNotifikasi.isChecked
        )
    }

    private fun updateProgress() {
        val current = createFormDataFromInputs()
        val percentage = current.calculateProgress()

        pbFormProgress.progress = percentage
        tvProgressPercentage.text = "$percentage%"
    }

    private fun validateForm(): Boolean {
        val nama = edtNama.text.toString().trim()
        val email = edtEmail.text.toString().trim()
        val telepon = edtTelepon.text.toString().trim()

        if (nama.isEmpty()) {
            edtNama.error = "Nama lengkap wajib diisi!"
            edtNama.requestFocus()
            return false
        }

        if (email.isEmpty()) {
            edtEmail.error = "Alamat email wajib diisi!"
            edtEmail.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.error = "Format email tidak valid (contoh: user@gmail.com)"
            edtEmail.requestFocus()
            return false
        }

        if (telepon.isEmpty()) {
            edtTelepon.error = "Nomor telepon wajib diisi!"
            edtTelepon.requestFocus()
            return false
        }

        if (rgJk.checkedRadioButtonId == -1) {
            Toast.makeText(this, "Silakan pilih Jenis Kelamin!", Toast.LENGTH_SHORT).show()
            return false
        }

        if (spKota.selectedItemPosition <= 0) {
            Toast.makeText(this, "Silakan pilih Kota Asal!", Toast.LENGTH_SHORT).show()
            return false
        }

        if (selectedDate.isEmpty()) {
            Toast.makeText(this, "Silakan pilih Tanggal Lahir!", Toast.LENGTH_SHORT).show()
            return false
        }

        if (spBidangMinat.selectedItemPosition <= 0) {
            Toast.makeText(this, "Silakan pilih Program / Bidang Minat!", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!cbSyarat.isChecked) {
            Toast.makeText(this, "Anda harus menyetujui Syarat & Ketentuan!", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun loadExistingData() {
        val data = FormDataRepository.currentData ?: return

        edtNama.setText(data.nama)
        edtEmail.setText(data.email)
        edtTelepon.setText(data.telepon)

        when (data.jenisKelamin) {
            "Laki-laki" -> rbLaki.isChecked = true
            "Perempuan" -> rbPerempuan.isChecked = true
        }

        val posKota = daftarKota.indexOf(data.kotaAsal)
        if (posKota >= 0) spKota.setSelection(posKota)

        if (data.tanggalLahir.isNotBlank()) {
            selectedDate = data.tanggalLahir
            tvTanggalLahir.text = selectedDate
        }

        val posBidang = daftarBidang.indexOf(data.bidangMinat)
        if (posBidang >= 0) spBidangMinat.setSelection(posBidang)

        sbMinat.progress = data.tingkatMinat
        tvMinatValue.text = "Skala: ${data.tingkatMinat} / 10"

        swNotifikasi.isChecked = data.notifikasiAktif
        cbSyarat.isChecked = data.setujuSK
    }

    private fun showResetConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Reset Formulir")
            .setMessage("Apakah Anda yakin ingin menghapus seluruh data pada formulir?")
            .setPositiveButton("Ya, Reset") { _, _ ->
                resetForm()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun resetForm() {
        edtNama.text.clear()
        edtEmail.text.clear()
        edtTelepon.text.clear()
        rgJk.clearCheck()
        spKota.setSelection(0)
        selectedDate = ""
        tvTanggalLahir.text = "Pilih Tanggal Lahir"
        spBidangMinat.setSelection(0)
        sbMinat.progress = 5
        tvMinatValue.text = "Skala: 5 / 10"
        swNotifikasi.isChecked = false
        cbSyarat.isChecked = false

        FormDataRepository.reset()
        updateProgress()

        Toast.makeText(this, "Formulir berhasil direset", Toast.LENGTH_SHORT).show()
    }
}
