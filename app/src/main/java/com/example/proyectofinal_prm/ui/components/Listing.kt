package com.example.proyectofinal_prm.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.proyectofinal_prm.data.ApiClient
import com.example.proyectofinal_prm.data.DisplayItem
import com.example.proyectofinal_prm.data.ImageItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.max

@Composable
fun Listing() {
    var items by remember { mutableStateOf<List<DisplayItem>>(emptyList()) }

    LaunchedEffect(Unit) {
        try {
            val response = withContext(Dispatchers.IO) {
                ApiClient.apiService.getImages()
            }

            items = response.mapIndexed { index, image ->
                DisplayItem(
                    image = image,
                    name = productName(index),
                    description = Description(),
                    price = Price()
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        itemsIndexed(items) { index, item ->
            val firstVisibleItem = listState.firstVisibleItemIndex
            val scrollOffset = listState.firstVisibleItemScrollOffset

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
                    Image(
                        painter = rememberAsyncImagePainter(item.image.url),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Message(
                        title = item.name,
                        subtitle = "${item.description}\nPrecio: \$${item.price}"
                    )
                }
            }
        }
    }
}

fun productName(index: Int): String {
    val letters = ('A'..'Z').toList()
    return "Producto ${letters.getOrNull(index % letters.size) ?: "X"}"
}

fun Description(): String {
    val descriptions = listOf(
        "Este es un excelente producto.",
        "Calidad garantizada.",
        "Nuevo en el mercado.",
        "Edición limitada.",
        "Ideal para ti.",
        "Con la mejor tecnología."
    )
    return descriptions.random()
}

fun Price(): Int {
    return (100..999).random()
}
