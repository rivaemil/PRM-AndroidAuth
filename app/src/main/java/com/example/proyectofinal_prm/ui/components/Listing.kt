package com.example.proyectofinal_prm.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.proyectofinal_prm.data.ApiClient
import com.example.proyectofinal_prm.data.ProductItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.max
@Composable
fun Listing() {
    var products by remember { mutableStateOf<List<ProductItem>>(emptyList()) }

    LaunchedEffect(Unit) {
        try {
            println("Iniciando petición a /articles")
            val response = withContext(Dispatchers.IO) {
                ApiClient.apiService.getProducts()
            }
            println("Response recibida: ${response.data.size} productos")
            response.data.forEach { println("  • ${it.name} → imágenes: ${it.images.size}") }
            products = response.data
        } catch (e: Exception) {
            println("Error al obtener productos: ${e.message}")
            e.printStackTrace()
        }
    }

    val listState = rememberLazyListState()

    if (products.isEmpty()) {
        Text("No hay productos", color = Color.White)
    } else {
        LazyColumn(
            state = listState,
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            itemsIndexed(products) { index, product ->

                val scrollOffset = listState.firstVisibleItemScrollOffset
                val firstVisibleItem = listState.firstVisibleItemIndex
                val shouldAnimate = index == firstVisibleItem

                val scale by animateFloatAsState(
                    targetValue = if (shouldAnimate) max(0.3f, 1f - scrollOffset / 200f) else 1f,
                    label = "scaleAnimation"
                )

                val alpha by animateFloatAsState(
                    targetValue = if (shouldAnimate) max(0f, 1f - scrollOffset / 200f) else 1f,
                    label = "alphaAnimation"
                )

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            this.alpha = alpha
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        val imageUrl = product.images.firstOrNull()?.url

                        if (imageUrl != null) {
                            Image(
                                painter = rememberAsyncImagePainter(imageUrl),
                                contentDescription = "Producto ${product.name}",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        Message(
                            title = product.name,
                            subtitle = "${product.description}\nPrecio: \$${product.price}"
                        )
                    }
                }
            }
        }
    }
}
