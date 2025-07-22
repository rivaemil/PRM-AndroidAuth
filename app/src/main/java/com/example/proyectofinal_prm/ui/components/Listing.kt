package com.example.proyectofinal_prm.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.proyectofinal_prm.data.ApiClient
import com.example.proyectofinal_prm.data.ProductItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.max

@Composable
fun Listing(navController: NavController) {
    var items by remember { mutableStateOf<List<ProductItem>>(emptyList()) }

    LaunchedEffect(Unit) {
        try {
            val response = withContext(Dispatchers.IO) {
                ApiClient.apiService.getProducts()
            }
            items = response.data
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        itemsIndexed(items) { index, product ->
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


            var offsetX by remember { mutableStateOf(0f) }
            var hasNavigated by remember { mutableStateOf(false) }

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        this.alpha = alpha
                        translationX = offsetX
                    }
                    .pointerInput(product.id) {
                        detectHorizontalDragGestures(
                            onDragEnd = {
                                if (offsetX > 150 && !hasNavigated) {
                                    hasNavigated = true
                                    navController.navigate("edit_product/${product.id}")
                                }
                                if (!hasNavigated) {
                                    offsetX = 0f
                                }
                            },
                            onDragCancel = {

                                if (!hasNavigated) {
                                    offsetX = 0f
                                }
                            },
                            onHorizontalDrag = { change, dragAmount ->
                                val newOffset = offsetX + dragAmount
                                // Solo permitir arrastrar a la derecha (offsetX >= 0)
                                if (newOffset >= 0f) {
                                    offsetX = newOffset
                                }
                            }
                        )
                    },
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    CarouselImage(images = product.images)
                    Spacer(modifier = Modifier.height(8.dp))
                    Message(
                        title = product.name,
                        subtitle = "${product.description}\nPrecio: \$${product.price}"
                    )
                }
            }
        }
    }
}


@Composable
fun CarouselImage(images: List<com.example.proyectofinal_prm.data.ImageItem>) {
    var currentIndex by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .pointerInput(images) {
                detectHorizontalDragGestures { _, dragAmount ->
                    if (dragAmount < -50) {
                        currentIndex = (currentIndex + 1).coerceAtMost(images.lastIndex)
                    } else if (dragAmount > 50) {
                        currentIndex = (currentIndex - 1).coerceAtLeast(0)
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        if (images.isNotEmpty()) {
            val rawUrl = images[currentIndex].url
            val imageUrl = if (rawUrl.startsWith("http")) {
                rawUrl
            } else {
                "http://10.0.2.2:8000${rawUrl}"
            }

            Image(
                painter = rememberAsyncImagePainter(model = imageUrl),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                images.forEachIndexed { i, _ ->
                    val color = if (i == currentIndex) Color.White else Color.LightGray
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(8.dp)
                            .background(color, shape = MaterialTheme.shapes.small)
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text("Sin imagen", color = Color.DarkGray)
            }
        }
    }
}
