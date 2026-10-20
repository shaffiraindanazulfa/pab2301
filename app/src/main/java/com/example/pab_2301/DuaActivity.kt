package com.example.pab_2301

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class DuaActivity : Activity() {

    private lateinit var layoutEmptyState: LinearLayout
    private lateinit var layoutDataSummary: LinearLayout
    private lateinit var btnKeForm: Button

    private lateinit var tvSummaryNama: TextView
    private lateinit var tvSummaryEmail: TextView
    private lateinit var tvSummaryTelepon: TextView
    private lateinit var tvSummaryJk: TextView
    private lateinit var tvSummaryKota: TextView
    private lateinit var tvSummaryTanggalLahir: TextView
    private lateinit var tvSummaryBidangMinat: TextView
    private lateinit var tvSummaryTingkatMinat: TextView
    private lateinit var tvSummarySK: TextView
    private lateinit var tvSummaryNotifikasi: TextView

    private lateinit var btnEditData: Button
    private lateinit var btnSelesai: Button
    private lateinit var navPendaftaranSummary: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dua)

        initViews()
        displaySummaryData()
        setupListeners()
    }

    private fun initViews() {
        layoutEmptyState = findViewById(R.id.layoutEmptyState)
        layoutDataSummary = findViewById(R.id.layoutDataSummary)
        btnKeForm = findViewById(R.id.btnKeForm)

        tvSummaryNama = findViewById(R.id.tvSummaryNama)
        tvSummaryEmail = findViewById(R.id.tvSummaryEmail)
        tvSummaryTelepon = findViewById(R.id.tvSummaryTelepon)
        tvSummaryJk = findViewById(R.id.tvSummaryJk)
        tvSummaryKota = findViewById(R.id.tvSummaryKota)
        tvSummaryTanggalLahir = findViewById(R.id.tvSummaryTanggalLahir)
        tvSummaryBidangMinat = findViewById(R.id.tvSummaryBidangMinat)
        tvSummaryTingkatMinat = findViewById(R.id.tvSummaryTingkatMinat)
        tvSummarySK = findViewById(R.id.tvSummarySK)
        tvSummaryNotifikasi = findViewById(R.id.tvSummaryNotifikasi)

        btnEditData = findViewById(R.id.btnEditData)
        btnSelesai = findViewById(R.id.btnSelesai)
        navPendaftaranSummary = findViewById(R.id.navPendaftaranSummary)
    }

    private fun displaySummaryData() {
        val data = FormDataRepository.currentData

        if (data == null || data.nama.isBlank()) {
            layoutEmptyState.visibility = View.VISIBLE
            layoutDataSummary.visibility = View.GONE
        } else {
            layoutEmptyState.visibility = View.GONE
            layoutDataSummary.visibility = View.VISIBLE

            tvSummaryNama.text = data.nama
            tvSummaryEmail.text = data.email
            tvSummaryTelepon.text = if (data.telepon.isNotBlank()) data.telepon else "-"
            tvSummaryJk.text = if (data.jenisKelamin.isNotBlank()) data.jenisKelamin else "-"
            tvSummaryKota.text = if (data.kotaAsal.isNotBlank()) data.kotaAsal else "-"
            tvSummaryTanggalLahir.text = if (data.tanggalLahir.isNotBlank()) data.tanggalLahir else "-"
            tvSummaryBidangMinat.text = if (data.bidangMinat.isNotBlank()) data.bidangMinat else "-"
            tvSummaryTingkatMinat.text = "Skala ${data.tingkatMinat} dari 10"
            tvSummarySK.text = if (data.setujuSK) "Disetujui" else "Belum Disetujui"
            tvSummaryNotifikasi.text = if (data.notifikasiAktif) "Aktif" else "Tidak Aktif"
        }
    }

    private fun setupListeners() {
        btnKeForm.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }

        btnEditData.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }

        btnSelesai.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Pendaftaran Selesai")
                .setMessage("Terima kasih, data pendaftaran Anda telah tersimpan secara resmi. Ingin memulai pendaftaran baru?")
                .setPositiveButton("Ya, Pendaftaran Baru") { _, _ ->
                    FormDataRepository.reset()
                    Toast.makeText(this, "Pendaftaran baru dimulai!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("Tutup", null)
                .show()
        }

        navPendaftaranSummary.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }
    }
}
