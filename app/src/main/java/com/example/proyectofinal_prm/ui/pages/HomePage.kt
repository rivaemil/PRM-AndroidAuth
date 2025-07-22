package com.example.proyectofinal_prm.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.example.proyectofinal_prm.data.ProductItem
import com.example.proyectofinal_prm.ui.components.Listing
import androidx.navigation.NavHostController

@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    products: List<ProductItem>,
    refreshProducts: () -> Unit,
    onEditProduct: (Int) -> Unit,
    navController: NavController
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.DarkGray)
    ) {
        Text(
            "Productos",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )

        Listing(navController = navController) // ahora sí está definido
    }
}
