package com.example.proyectofinal_prm.data

data class ImageItem(
    val id: Int,
    val url: String,
    val imageable_type: String,
    val imageable_id: Int
)

data class DisplayItem(
    val image: ImageItem,
    val name: String,
    val description: String,
    val price: Int
)
