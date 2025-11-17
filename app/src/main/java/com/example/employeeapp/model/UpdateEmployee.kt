package com.example.employeeapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.employeeapp.databinding.ActivityUpdateEmployeeBinding
import com.example.employeeapp.model.Employee
import com.example.employeeapp.model.EmployeeDetailResponse
import com.example.employeeapp.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateEmployeeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateEmployeeBinding
    private val client = ApiClient.getInstance()
    private var employeeId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateEmployeeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data dari Intent
        employeeId = intent.getIntExtra("EXTRA_ID", -1)
        val name = intent.getStringExtra("EXTRA_NAME")
        val salary = intent.getIntExtra("EXTRA_SALARY", 0)
        val age = intent.getIntExtra("EXTRA_AGE", 0)

        // Set data lama ke form
        binding.etName.setText(name)
        binding.etSalary.setText(salary.toString())
        binding.etAge.setText(age.toString())

        binding.btnSaveUpdate.setOnClickListener {
            val newName = binding.etName.text.toString()
            val newSalary = binding.etSalary.text.toString().toIntOrNull()
            val newAge = binding.etAge.text.toString().toIntOrNull()

            if (newName.isEmpty() || newSalary == null || newAge == null) {
                Toast.makeText(this, "Harap isi semua data", Toast.LENGTH_SHORT).show()
            } else {
                val updatedEmployee = Employee(id = employeeId, employeeName = newName, employeeSalary = newSalary, employeeAge = newAge)
                updateEmployeeData(employeeId, updatedEmployee)
            }
        }
    }

    private fun updateEmployeeData(id: Int, updatedEmployee: Employee) {
        client.updateEmployee(id, updatedEmployee).enqueue(object : Callback<EmployeeDetailResponse> {
            override fun onResponse(call: Call<EmployeeDetailResponse>, response: Response<EmployeeDetailResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@UpdateEmployeeActivity, "Sukses update data", Toast.LENGTH_SHORT).show()
                    finish() // Tutup activity
                } else {
                    Toast.makeText(this@UpdateEmployeeActivity, "Gagal update: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<EmployeeDetailResponse>, t: Throwable) {
                Toast.makeText(this@UpdateEmployeeActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}