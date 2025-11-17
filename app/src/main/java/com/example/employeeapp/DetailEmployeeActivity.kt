package com.example.employeeapp

import android.content.Intent // TAMBAHKAN IMPORT
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.employeeapp.databinding.ActivityDetailEmployeeBinding
import com.example.employeeapp.model.DeleteResponse // TAMBAHKAN IMPORT
import com.example.employeeapp.model.Employee // TAMBAHKAN IMPORT
import com.example.employeeapp.model.EmployeeDetailResponse
import com.example.employeeapp.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailEmployeeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailEmployeeBinding
    private val client = ApiClient.getInstance()
    private var currentEmployee: Employee? = null // Kita simpan data di sini

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEmployeeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // with(binding) { } // Ini tidak terpakai, biarkan saja

        val employeeId = intent.getIntExtra("EXTRA_ID", -1)
        if (employeeId == -1) {
            // ... (kode Anda sudah benar)
            Toast.makeText(this, "ID tidak valid", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        getEmployeeDetail(employeeId)

        binding.btnDelete.setOnClickListener {
            deleteEmployeeById(employeeId)
        }

        // Listener untuk tombol Update
        binding.btnGoToUpdate.setOnClickListener {
            if (currentEmployee != null) {
                val intent = Intent(this@DetailEmployeeActivity, UpdateEmployeeActivity::class.java)
                // Kirim data saat ini ke UpdateActivity
                intent.putExtra("EXTRA_ID", currentEmployee!!.id)
                intent.putExtra("EXTRA_NAME", currentEmployee!!.employeeName)
                intent.putExtra("EXTRA_SALARY", currentEmployee!!.employeeSalary)
                intent.putExtra("EXTRA_AGE", currentEmployee!!.employeeAge)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Data belum ter-load", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getEmployeeDetail(id: Int) {
        val response = client.getAllEmployeeDetail(id)

        response.enqueue( object : Callback<EmployeeDetailResponse>{
            override fun onResponse(
                p0: Call<EmployeeDetailResponse?>,
                response: Response<EmployeeDetailResponse?>
            ) {
                if (!response.isSuccessful) {
                    Toast.makeText(
                        this@DetailEmployeeActivity,
                        "HTTP ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return // Tambahkan return di sini
                }

                // Modif di sini:
                currentEmployee = response.body()?.data // Simpan data ke variabel global

                // Set text dengan format yang lebih baik
                binding.txtName.text = "Name: ${currentEmployee?.employeeName}"
                binding.txtSalary.text = "Salary: ${currentEmployee?.employeeSalary}"
                binding.txtAge.text = "Age: ${currentEmployee?.employeeAge}"
            }

            override fun onFailure(
                p0: Call<EmployeeDetailResponse?>,
                p1: Throwable
            ) {
                // Sebaiknya jangan di-TODO
                Toast.makeText(this@DetailEmployeeActivity, "Error: ${p1.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // --- TAMBAHKAN FUNGSI BARU DI BAWAH INI ---
    private fun deleteEmployeeById(id: Int) {
        client.deleteEmployee(id).enqueue(object : Callback<DeleteResponse> {
            override fun onResponse(call: Call<DeleteResponse>, response: Response<DeleteResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@DetailEmployeeActivity, "Sukses menghapus data", Toast.LENGTH_SHORT).show()
                    finish() // Tutup activity
                } else {
                    Toast.makeText(this@DetailEmployeeActivity, "Gagal delete: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {
                Toast.makeText(this@DetailEmployeeActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}