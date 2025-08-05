package com.example.proyectofinal_prm.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal_prm.data.ApiService
import com.example.proyectofinal_prm.data.ProductItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

class ProductViewModel(private val apiService: ApiService) : ViewModel() {

    private val _productState = MutableStateFlow<ProductItem?>(null)
    val productState: StateFlow<ProductItem?> = _productState

    fun setProduct(product: ProductItem) {
        _productState.value = product
    }

    fun updateProduct(
        context: Context,
        id: Int,
        name: String,
        description: String,
        price: Double,
        deletedImageUrls: List<String>,
        newImageUris: List<Uri>,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val nameBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
                val descBody = description.toRequestBody("text/plain".toMediaTypeOrNull())
                val priceBody = price.toString().toRequestBody("text/plain".toMediaTypeOrNull())

                val imageParts = newImageUris.mapNotNull { uri ->
                    uriToMultipart(context, uri, "images[]")
                }

                val deletedJson = deletedImageUrls.joinToString(
                    prefix = "[\"",
                    separator = "\",\"",
                    postfix = "\"]"
                )
                val deletedImagesBody = deletedJson.toRequestBody("application/json".toMediaTypeOrNull())

                val response = apiService.updateProduct(
                    productId = id,
                    name = nameBody,
                    description = descBody,
                    price = priceBody,
                    deletedImageIds = deletedImagesBody,
                    images = imageParts
                )

                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError("Error del servidor: ${response.code()} ${response.message()}")
                }

            } catch (e: Exception) {
                e.printStackTrace()
                onError("Error: ${e.localizedMessage}")
            }
        }
    }

    private fun uriToMultipart(context: Context, uri: Uri, partName: String): MultipartBody.Part? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val tempFile = File.createTempFile("upload", ".jpg", context.cacheDir)
            FileOutputStream(tempFile).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
            val requestFile = tempFile.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData(partName, tempFile.name, requestFile)
        } catch (e: Exception) {
            Log.e("ViewModel", "Error al crear MultipartBody: ${e.message}")
            null
        }
    }
}
