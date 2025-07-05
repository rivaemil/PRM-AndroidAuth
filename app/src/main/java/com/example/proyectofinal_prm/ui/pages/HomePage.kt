package com.example.proyectofinal_prm.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectofinal_prm.data.ProductItem
import com.example.proyectofinal_prm.data.ProductUpdateRequest
import com.example.proyectofinal_prm.data.ApiClient
import com.example.proyectofinal_prm.ui.components.Listing
import kotlinx.coroutines.launch

@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    products: List<ProductItem>,
    refreshProducts: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var selectedProduct by remember { mutableStateOf<ProductItem?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.DarkGray),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Productos",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )

        Listing()
        }
}
