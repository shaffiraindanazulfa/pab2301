package com.example.tugaspab

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tugaspab.databinding.ActivityDuaBinding

class DuaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDuaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDuaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        displayConfirmationData()
        setupButtons()
    }

    private fun displayConfirmationData() {
        val nama = intent.getStringExtra(MainActivity.EXTRA_NAMA)
        val email = intent.getStringExtra(MainActivity.EXTRA_EMAIL)
        val whatsapp = intent.getStringExtra(MainActivity.EXTRA_WHATSAPP)
        val kategori = intent.getStringExtra(MainActivity.EXTRA_KATEGORI)
        val workshop = intent.getStringExtra(MainActivity.EXTRA_WORKSHOP)
        val kota = intent.getStringExtra(MainActivity.EXTRA_KOTA)
        val tanggal = intent.getStringExtra(MainActivity.EXTRA_TANGGAL)
        val pengalaman = intent.getIntExtra(MainActivity.EXTRA_PENGALAMAN, 1)
        val pengingat = intent.getBooleanExtra(MainActivity.EXTRA_PENGINGAT, false)

        if (nama.isNullOrBlank() && email.isNullOrBlank()) {
            Toast.makeText(this, getString(R.string.no_data_passed), Toast.LENGTH_SHORT).show()
            binding.tvValueNama.text = "-"
            binding.tvValueEmail.text = "-"
            binding.tvValueWhatsapp.text = "-"
            binding.tvValueKategori.text = "-"
            binding.tvValueWorkshop.text = "-"
            binding.tvValueKota.text = "-"
            binding.tvValueTanggal.text = "-"
            binding.tvValuePengalaman.text = "-"
            binding.tvValuePengingat.text = "-"
            return
        }

        binding.tvValueNama.text = nama ?: "-"
        binding.tvValueEmail.text = email ?: "-"
        binding.tvValueWhatsapp.text = whatsapp ?: "-"
        binding.tvValueKategori.text = kategori ?: "-"
        binding.tvValueWorkshop.text = workshop ?: "-"
        binding.tvValueKota.text = kota ?: "-"
        binding.tvValueTanggal.text = tanggal ?: "-"

        val levelExp = if (pengalaman < 1) 1 else pengalaman
        val expCategory = when (levelExp) {
            in 1..3 -> getString(R.string.exp_level_pemula)
            in 4..7 -> getString(R.string.exp_level_menengah)
            else -> getString(R.string.exp_level_mahir)
        }
        binding.tvValuePengalaman.text = getString(R.string.format_pengalaman, levelExp, expCategory)

        if (pengingat) {
            binding.tvValuePengingat.text = getString(R.string.reminder_active)
            binding.tvValuePengingat.setTextColor(getColor(R.color.color_success))
        } else {
            binding.tvValuePengingat.text = getString(R.string.reminder_inactive)
            binding.tvValuePengingat.setTextColor(getColor(R.color.text_muted))
        }
    }

    private fun setupButtons() {
        binding.btnKembali.setOnClickListener {
            finish()
        }

        binding.btnSelesai.setOnClickListener {
            Toast.makeText(this, getString(R.string.toast_registration_completed), Toast.LENGTH_LONG).show()
            finishAffinity()
        }
    }
}