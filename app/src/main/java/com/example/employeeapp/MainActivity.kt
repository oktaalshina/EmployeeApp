package com.example.employeeapp

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.employeeapp.databinding.ActivityMainBinding
import com.example.employeeapp.model.EmployeeResponse
import com.example.employeeapp.network.ApiClient
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    //api client
    private val client = ApiClient.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // HANYA TAMBAHKAN INI (listener untuk tombol FAB)
        binding.fabAdd.setOnClickListener {
            val intent = Intent(this@MainActivity, CreateEmployeeActivity::class.java)
            startActivity(intent)
        }

        // Kode Anda di bawah ini tidak berubah
        // with(binding) { }
        // loadEmployee() // Pindahkan ke onResume
    }

    override fun onResume() {
        super.onResume()
        // Kita panggil loadEmployee di sini agar list otomatis refresh
        // setelah kita create, update, atau delete data
        loadEmployee()
    }

    fun loadEmployee(){
        val response = client.getAllEmployees()

        //lakukan request dengan async (tidak ditunggu)
        // ketika sudah dapat datanya baru di proses
        response.enqueue(object : retrofit2.Callback<EmployeeResponse> {
            override fun onResponse(
                call: Call<EmployeeResponse>,
                response: Response<EmployeeResponse?>) {
                if (!response.isSuccessful) {
                    Toast.makeText(
                        this@MainActivity,
                        "HTTP ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                val body = response.body()
                val employees = body?.data.orEmpty()

                if (employees.isEmpty()) {
                    Toast.makeText(
                        this@MainActivity,
                        "Data Kosong",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                val names = employees.map { it.employeeName }

                val listAdapter = ArrayAdapter(
                    this@MainActivity,
                    android.R.layout.simple_list_item_1,
                    names
                )

                binding.lvEmployee.adapter = listAdapter
                binding.lvEmployee.onItemClickListener = AdapterView.OnItemClickListener {_, _, position, _ ->
                    val id = employees[position].id
                    val intent = Intent(this@MainActivity, DetailEmployeeActivity::class.java)

                    intent.putExtra("EXTRA_ID", id)
                    startActivity(intent)
                }
            }

            override fun onFailure(
                p0: Call<EmployeeResponse?>,
                p1: Throwable
            ) {
                Toast.makeText(this@MainActivity, "Error: ${p1.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}