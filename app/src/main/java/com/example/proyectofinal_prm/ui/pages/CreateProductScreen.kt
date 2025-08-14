package com.example.proyectofinal_prm.ui.pages

import android.content.Context
import android.net.Uri
import android.util.Log
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
import com.example.proyectofinal_prm.data.ApiClient
import com.example.proyectofinal_prm.data.Session
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProductScreen(
    onProductCreated: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    val imageUris = remember { mutableStateListOf<Uri>() }
    val picker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        imageUris.clear()
        imageUris.addAll(uris.take(4))
    }

    var loading by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { pad ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(pad)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Precio") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            Button(onClick = { picker.launch("image/*") }) {
                Text("Seleccionar Imágenes (${imageUris.size}/4)")
            }
            Spacer(Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                imageUris.forEach { uri ->
                    AsyncImage(model = uri, contentDescription = null, modifier = Modifier.size(80.dp))
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    scope.launch {
                        if (Session.bearerToken.isNullOrBlank()) {
                            snackbarHostState.showMessage("No hay token. Inicia sesión primero.")
                            return@launch
                        }
                        if (imageUris.size != 4) {
                            snackbarHostState.showMessage("Debes seleccionar exactamente 4 imágenes.")
                            return@launch
                        }
                        loading = true
                        val (ok, msg) = createProductCall(
                            context = context,
                            name = name,
                            description = description,
                            price = price,
                            imageUris = imageUris.toList()
                        )
                        loading = false
                        if (ok) {
                            snackbarHostState.showMessage("Producto creado.")
                            onProductCreated()
                        } else {
                            snackbarHostState.showMessage(msg ?: "Error al crear producto")
                        }
                    }
                },
                enabled = !loading && name.isNotBlank() && price.isNotBlank() && imageUris.size == 4,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (loading) "Creando..." else "Crear Producto")
            }
        }
    }
}

private suspend fun SnackbarHostState.showMessage(text: String) {
    withContext(Dispatchers.Main) { showSnackbar(text) }
}

/**
 * Llama al backend. Devuelve Pair<success, errorMessage?>
 */
suspend fun createProductCall(
    context: Context,
    name: String,
    description: String,
    price: String,
    imageUris: List<Uri>,
): Pair<Boolean, String?> {
    return withContext(Dispatchers.IO) {
        try {
            val cr = context.contentResolver

            val namePart = name.toRequestBody("text/plain".toMediaTypeOrNull())
            val descriptionPart = description.toRequestBody("text/plain".toMediaTypeOrNull())
            val pricePart = price.toRequestBody("text/plain".toMediaTypeOrNull())

            val images = imageUris.mapIndexed { index, uri ->
                val input = cr.openInputStream(uri)!!
                val tmp = File.createTempFile("create_$index", ".jpg", context.cacheDir)
                tmp.outputStream().use { out -> input.copyTo(out) }
                val body = tmp.asRequestBody("image/*".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("images[$index]", tmp.name, body)
            }

            val resp = ApiClient.multipartService.uploadProduct(
                name = namePart,
                description = descriptionPart,
                price = pricePart,
                images = images
            )

            Log.d("CreateProduct", "resp code=${resp.code()} success=${resp.isSuccessful}")
            if (resp.isSuccessful) {
                true to null
            } else {
                val err = resp.errorBody()?.string()
                Log.e("CreateProduct", "error=$err")
                false to ("${resp.code()} ${err ?: ""}".trim())
            }
        } catch (e: Exception) {
            Log.e("CreateProduct", "exception=${e.message}", e)
            false to e.message
        }
    }
}
