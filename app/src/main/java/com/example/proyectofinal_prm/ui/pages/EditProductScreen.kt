package com.example.proyectofinal_prm.ui.pages

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.proyectofinal_prm.data.ApiClient
import com.example.proyectofinal_prm.data.ProductItem
import com.example.proyectofinal_prm.data.ProductRequest
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

@Composable
fun EditProductScreen(productId: Int, navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var product by remember { mutableStateOf<ProductItem?>(null) }
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    val imageUris = remember { mutableStateListOf<Uri>() }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        imageUris.clear()
        imageUris.addAll(uris.take(4))
    }

    LaunchedEffect(productId) {
        try {
            val fetchedProduct = ApiClient.apiService.getProduct(productId)
            product = fetchedProduct
            name = fetchedProduct.name
            description = fetchedProduct.description
            price = fetchedProduct.price.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    if (product != null) {
        Column(Modifier.padding(16.dp)) {
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") })
            OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Descripción") })
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Precio") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )

            Spacer(Modifier.height(8.dp))

            Button(onClick = { launcher.launch("image/*") }) {
                Text("Cambiar Imágenes (${imageUris.size}/4)")
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (imageUris.isEmpty()) {
                    product?.images?.forEach {
                        AsyncImage(model = it.url, contentDescription = null, modifier = Modifier.size(80.dp))
                    }
                } else {
                    imageUris.forEach { uri ->
                        AsyncImage(model = uri, contentDescription = null, modifier = Modifier.size(80.dp))
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    scope.launch {
                        updateProduct(
                            productId, name, description, price, imageUris, context
                        )
                        navController.popBackStack()
                    }
                },
                enabled = name.isNotBlank() && price.isNotBlank()
            ) {
                Text("Actualizar Producto")
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}

suspend fun updateProduct(
    productId: Int,
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
    service.updateProduct(productId, namePart, descriptionPart, pricePart, images)
}