package com.example.proyectofinal_prm.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlin.math.max

@Composable
fun Listing(
    names: List<String> = List(10) { "Item #$it" },
) {
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        itemsIndexed(names) { index, name ->
            val firstVisibleItem = listState.firstVisibleItemIndex
            val scrollOffset = listState.firstVisibleItemScrollOffset

            val shouldAnimate = index == firstVisibleItem

            val scale by animateFloatAsState(
                targetValue = if (shouldAnimate) {
                    max(0.3f, 1f - scrollOffset / 200f)
                } else 1f,
                label = "scaleAnimation"
            )

            val alpha by animateFloatAsState(
                targetValue = if (shouldAnimate) {
                    max(0f, 1f - scrollOffset / 200f)
                } else 1f,
                label = "alphaAnimation"
            )

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = androidx.compose.ui.graphics.Color.White
                ),
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        this.alpha = alpha
                    },
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                ),
            ) {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    ) {
                        Message(
                            title = name,
                            subtitle = "this is a subtitle of $name"
                        )
                    }
                }
            }
        }
    }
}
