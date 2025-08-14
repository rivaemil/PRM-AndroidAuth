package com.example.proyectofinal_prm.ui.pages

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.proyectofinal_prm.data.ApiClient
import com.example.proyectofinal_prm.data.ProductItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@Composable
fun EditProductScreen(productId: Int, navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var product by remember { mutableStateOf<ProductItem?>(null) }
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    // imágenes existentes (urls) y borradas
    var existingImageUrls by remember { mutableStateOf<List<String>>(emptyList()) }
    var deletedImageUrls by remember { mutableStateOf<List<String>>(emptyList()) }

    // imágenes nuevas (uris)
    val newImageUris = remember { mutableStateListOf<Uri>() }

    // picker de múltiples imágenes
    val picker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        val total = existingImageUrls.size - deletedImageUrls.size + newImageUris.size + uris.size
        if (total <= 4) {
            newImageUris.addAll(uris)
        } else {
            // recorta lo que sobrepase el límite
            val espacio = 4 - (existingImageUrls.size - deletedImageUrls.size + newImageUris.size)
            if (espacio > 0) newImageUris.addAll(uris.take(espacio))
        }
    }

    // Carga del producto
    LaunchedEffect(productId) {
        try {
            val p = ApiClient.apiService.getProduct(productId)
            product = p
            name = p.name
            description = p.description
            price = p.price.toString()
            existingImageUrls = p.images.map { it.url } // asume ProductItem.images: List<{url:String}>
            deletedImageUrls = emptyList()
        } catch (_: Exception) { }
    }

    if (product == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Column(Modifier.padding(16.dp)) {
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Precio") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        // fila de imágenes existentes (con botón para marcar borrar)
        Text("Imágenes existentes")
        Spacer(Modifier.height(6.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(existingImageUrls) { url ->
                val marcada = remember(url, deletedImageUrls) { deletedImageUrls.contains(url) }
                Box {
                    AsyncImage(model = url, contentDescription = null, modifier = Modifier.size(96.dp))
                    TextButton(
                        onClick = {
                            deletedImageUrls = if (marcada) deletedImageUrls - url else deletedImageUrls + url
                        },
                        modifier = Modifier.align(Alignment.BottomCenter)
                    ) { Text(if (marcada) "Recuperar" else "Eliminar") }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // nuevas imágenes elegidas
        Text("Imágenes nuevas")
        Spacer(Modifier.height(6.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(newImageUris) { uri ->
                Box {
                    AsyncImage(model = uri, contentDescription = null, modifier = Modifier.size(96.dp))
                    TextButton(
                        onClick = { newImageUris.remove(uri) },
                        modifier = Modifier.align(Alignment.BottomCenter)
                    ) { Text("Quitar") }
                }
            }
        }

        Spacer(Modifier.height(8.dp))
        val restantes = 4 - (existingImageUrls.size - deletedImageUrls.size + newImageUris.size)
        Text("Total: ${(existingImageUrls.size - deletedImageUrls.size) + newImageUris.size} / 4 (puedes agregar $restantes más)")
        Spacer(Modifier.height(8.dp))
        Button(onClick = { picker.launch("image/*") }, enabled = restantes > 0) {
            Text("Agregar imágenes")
        }

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                scope.launch {
                    try {
                        updateProductMultipart(
                            context = context,
                            productId = productId,
                            name = name,
                            description = description,
                            price = price,
                            newUris = newImageUris.toList(),
                            deletedUrls = deletedImageUrls
                        )
                        navController.popBackStack()
                    } catch (_: Exception) {}
                }
            },
            enabled = name.isNotBlank() && price.isNotBlank()
        ) {
            Text("Guardar cambios")
        }
    }
}

// --- Envío multipart con _method=PUT + deletedImageUrls[] ---
suspend fun updateProductMultipart(
    context: Context,
    productId: Int,
    name: String,
    description: String,
    price: String,
    newUris: List<Uri>,
    deletedUrls: List<String>
) {
    val cr = context.contentResolver

    val namePart = name.toRequestBody("text/plain".toMediaTypeOrNull())
    val descriptionPart = description.toRequestBody("text/plain".toMediaTypeOrNull())
    val pricePart = price.toRequestBody("text/plain".toMediaTypeOrNull())

    // nuevas imágenes
    val imageParts = newUris.mapIndexed { idx, uri ->
        val input = cr.openInputStream(uri)!!
        val file = File.createTempFile("img_$idx", ".jpg", context.cacheDir)
        file.outputStream().use { out -> input.copyTo(out) }
        val req = file.asRequestBody("image/*".toMediaTypeOrNull())
        MultipartBody.Part.createFormData("images[$idx]", file.name, req)
    }

    // urls a eliminar
    val deletedParts = deletedUrls.map {
        MultipartBody.Part.createFormData("deletedImageUrls[]", it)
    }

    // usamos retrofit ya configurado en ApiClient, apuntando a /api/products/{id}?_method=PUT
    ApiClient.apiService.updateProductMultipart(
        id = productId,
        name = namePart,
        description = descriptionPart,
        price = pricePart,
        parts = imageParts + deletedParts // orden no importa
    )
}
