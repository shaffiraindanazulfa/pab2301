package com.example.pab_2301

data class FormData(
    var nama: String = "",
    var email: String = "",
    var telepon: String = "",
    var jenisKelamin: String = "",
    var kotaAsal: String = "",
    var tanggalLahir: String = "",
    var bidangMinat: String = "",
    var tingkatMinat: Int = 5,
    var setujuSK: Boolean = false,
    var notifikasiAktif: Boolean = false
) {
    fun calculateProgress(): Int {
        var filled = 0
        val total = 9

        if (nama.isNotBlank()) filled++
        if (email.isNotBlank()) filled++
        if (telepon.isNotBlank()) filled++
        if (jenisKelamin.isNotBlank()) filled++
        if (kotaAsal.isNotBlank() && !kotaAsal.contains("Pilih")) filled++
        if (tanggalLahir.isNotBlank() && !tanggalLahir.contains("Pilih")) filled++
        if (bidangMinat.isNotBlank() && !bidangMinat.contains("Pilih")) filled++
        if (setujuSK) filled++
        if (tingkatMinat > 0) filled++

        return ((filled.toDouble() / total.toDouble()) * 100).toInt()
    }
}

object FormDataRepository {
    var currentData: FormData? = null

    fun reset() {
        currentData = null
    }
}
