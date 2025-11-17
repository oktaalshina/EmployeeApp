package com.example.employeeapp.network

import com.example.employeeapp.model.DeleteResponse
import com.example.employeeapp.model.Employee
import com.example.employeeapp.model.EmployeeDetailResponse
import com.example.employeeapp.model.EmployeeResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    //untuk handle endpoint dari base url/employee
    @GET("employees")
    fun getAllEmployees(): Call<EmployeeResponse>

    @GET("employee/{id}")
    fun getAllEmployeeDetail(
        @Path("id") id: Int
    ): Call<EmployeeDetailResponse>

    @POST("create")
    fun createEmployee(
        @Body employee: Employee
    ): Call<EmployeeDetailResponse>

    @PUT("update/{id}")
    fun updateEmployee(
        @Path("id") id: Int,
        @Body employee: Employee
    ): Call<EmployeeDetailResponse>

    @DELETE("delete/{id}")
    fun deleteEmployee(
        @Path("id") id: Int
    ): Call<DeleteResponse>
}