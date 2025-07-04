package com.example.proyectofinal_prm.data

data class ProductCreateRequest(
    val name: String,
    val description: String,
    val price: String = "0.0"
)

