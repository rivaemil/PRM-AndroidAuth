package com.example.proyectofinal_prm.ui.pages

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.proyectofinal_prm.viewmodel.ProductViewModel
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

@Composable
fun EditProductScreen(
    productId: Int,
    navController: NavController,
    viewModel: ProductViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val context = LocalContext.current

    val product = viewModel.productState.collectAsState().value

    var name by remember { mutableStateOf(TextFieldValue(product?.name ?: "")) }
    var description by remember { mutableStateOf(TextFieldValue(product?.description ?: "")) }
    var price by remember { mutableStateOf(TextFieldValue(product?.price?.toString() ?: "")) }

    val newImageUris = remember { mutableStateListOf<Uri>() }
    val existingImageUrls = remember {
        mutableStateListOf<String>().apply {
            product?.images?.mapNotNull { it.url }?.let { addAll(it) }
        }
    }
    val deletedImageUrls = remember { mutableStateListOf<String>() }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null && (existingImageUrls.size + newImageUris.size) < 4) {
            newImageUris.add(uri)
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                val nameStr = name.text.trim()
                val descStr = description.text.trim()
                val priceVal = price.text.toDoubleOrNull() ?: 0.0

                if (nameStr.isBlank() || descStr.isBlank() || priceVal <= 0.0) {
                    Toast.makeText(context, "Campos inválidos", Toast.LENGTH_SHORT).show()
                    return@FloatingActionButton
                }

                val productId = product?.id ?: return@FloatingActionButton

                viewModel.updateProduct(
                    context = context,
                    id = productId,
                    name = nameStr,
                    description = descStr,
                    price = priceVal,
                    deletedImageUrls = deletedImageUrls,
                    newImageUris = newImageUris,
                    onSuccess = {
                        Toast.makeText(context, "Producto actualizado", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    },
                    onError = { msg ->
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                    }
                )
            }) {
                Icon(imageVector = Icons.Default.Create, contentDescription = "Actualizar")
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Precio") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("Imágenes existentes:", style = MaterialTheme.typography.titleSmall)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                existingImageUrls.forEach { url ->
                    Box {
                        Image(
                            painter = rememberAsyncImagePainter(url),
                            contentDescription = null,
                            modifier = Modifier
                                .size(80.dp)
                                .background(Color.LightGray)
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onLongPress = {
                                            existingImageUrls.remove(url)
                                            deletedImageUrls.add(url)
                                        }
                                    )
                                },
                            contentScale = ContentScale.Crop
                        )
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Eliminar",
                            tint = Color.Red,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .clickable {
                                    existingImageUrls.remove(url)
                                    deletedImageUrls.add(url)
                                }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("Imágenes nuevas:", style = MaterialTheme.typography.titleSmall)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                newImageUris.forEach { uri ->
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .background(Color.Gray),
                        contentScale = ContentScale.Crop
                    )
                }
                if ((existingImageUrls.size + newImageUris.size) < 4) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(Color.Gray, shape = CircleShape)
                            .clickable { imagePickerLauncher.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("+", color = Color.White, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}
