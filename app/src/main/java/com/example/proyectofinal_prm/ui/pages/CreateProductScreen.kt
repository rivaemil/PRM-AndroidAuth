package com.example.proyectofinal_prm.ui.pages

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.File

@Composable
fun CreateProductScreen(
    onProductCreated: () -> Unit
) {
    val context = LocalContext.current
    val imageUris = remember { mutableStateListOf<Uri>() }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        imageUris.clear()
        imageUris.addAll(uris.take(4))
    }

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") })
        OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Descripción") })
        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Precio") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        Button(
            onClick = { launcher.launch("image/*") },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text("Seleccionar Imágenes (${imageUris.size}/4)")
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            imageUris.forEach { uri ->
                AsyncImage(model = uri, contentDescription = null, modifier = Modifier.size(80.dp))
            }
        }

        Button(
            onClick = {
                scope.launch {
                    uploadProduct(name, description, price, imageUris, context)
                    onProductCreated()
                }
            },
            enabled = name.isNotBlank() && price.isNotBlank() && imageUris.size == 4,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Crear Producto")
        }
    }
}

suspend fun uploadProduct(
    name: String,
    description: String,
    price: String,
    imageUris: List<Uri>,
    context: Context
) {
    val contentResolver = context.contentResolver

    val namePart = name.toRequestBody("text/plain".toMediaTypeOrNull())
    val descriptionPart = description.toRequestBody("text/plain".toMediaTypeOrNull())
    val pricePart = price.toRequestBody("text/plain".toMediaTypeOrNull())

    val images = imageUris.mapIndexed { index, uri ->
        val inputStream = contentResolver.openInputStream(uri)!!
        val file = File.createTempFile("upload", ".jpg")
        file.outputStream().use { inputStream.copyTo(it) }
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        MultipartBody.Part.createFormData("images[$index]", file.name, requestBody)
    }

    val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8000/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(ApiMultipartService::class.java)

    service.uploadProduct(namePart, descriptionPart, pricePart, images)
}

interface ApiMultipartService {
    @Multipart
    @POST("products")
    suspend fun uploadProduct(
        @Part("name") name: okhttp3.RequestBody,
        @Part("description") description: okhttp3.RequestBody,
        @Part("price") price: okhttp3.RequestBody,
        @Part images: List<MultipartBody.Part>
    ): retrofit2.Response<Unit>
}