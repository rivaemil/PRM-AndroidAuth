package com.example.proyectofinal_prm.data

data class ProductItem(
    val id: Int,
    val name: String,
    val description: String,
    val price: String,
    val created_at: String,
    val images: List<ImageItem>
)

data class ImageItem(
    val id: Int,
    val url: String,
    val imageable_type: String,
    val imageable_id: Int,
    val created_at: String,
    val updated_at: String
)

