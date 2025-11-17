package com.example.employeeapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.employeeapp.databinding.ActivityDetailEmployeeBinding
import com.example.employeeapp.model.EmployeeDetailResponse
import com.example.employeeapp.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailEmployeeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailEmployeeBinding
    private val client = ApiClient.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailEmployeeBinding.inflate(layoutInflater)

        setContentView(binding.root)

        with(binding) {
            //
        }

        val employeeId = intent.getIntExtra("EXTRA_ID", -1)
        if (employeeId == -1) {
            Toast.makeText(
                this,
                "ID tidak valid",
                Toast.LENGTH_SHORT
            ).show()
            finish()
            return
        }
        getEmployeeDetail(employeeId)
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
                }

                val body = response.body()
                val employee = body?.data

                binding.txtName.setText(employee?.employeeName.toString())
                binding.txtSalary.setText(employee?.employeeSalary.toString())
                binding.txtAge.setText(employee?.employeeAge.toString())
            }

            override fun onFailure(
                p0: Call<EmployeeDetailResponse?>,
                p1: Throwable
            ) {
                TODO("Not yet implemented")
            }

        })
    }
}