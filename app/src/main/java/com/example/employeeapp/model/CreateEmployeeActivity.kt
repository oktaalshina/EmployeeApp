package com.example.employeeapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.employeeapp.databinding.ActivityCreateEmployeeBinding
import com.example.employeeapp.model.Employee
import com.example.employeeapp.model.EmployeeDetailResponse
import com.example.employeeapp.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateEmployeeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateEmployeeBinding
    private val client = ApiClient.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateEmployeeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString()
            val salary = binding.etSalary.text.toString().toIntOrNull()
            val age = binding.etAge.text.toString().toIntOrNull()

            if (name.isEmpty() || salary == null || age == null) {
                Toast.makeText(this, "Harap isi semua data", Toast.LENGTH_SHORT).show()
            } else {
                val newEmployee = Employee(id = 0, employeeName = name, employeeSalary = salary, employeeAge = age)
                createNewEmployee(newEmployee)
            }
        }
    }

    private fun createNewEmployee(newEmployee: Employee) {
        client.createEmployee(newEmployee).enqueue(object : Callback<EmployeeDetailResponse> {
            override fun onResponse(call: Call<EmployeeDetailResponse>, response: Response<EmployeeDetailResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CreateEmployeeActivity, "Sukses menambah data", Toast.LENGTH_SHORT).show()
                    finish() // Tutup activity
                } else {
                    Toast.makeText(this@CreateEmployeeActivity, "Gagal: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<EmployeeDetailResponse>, t: Throwable) {
                Toast.makeText(this@CreateEmployeeActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}