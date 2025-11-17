package com.example.employeeapp.model

import com.google.gson.annotations.SerializedName

data class DeleteResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("message")
    val message: String
)
