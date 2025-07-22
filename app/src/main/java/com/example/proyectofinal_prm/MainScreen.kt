package com.example.proyectofinal_prm

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.proyectofinal_prm.data.ApiClient
import com.example.proyectofinal_prm.data.ProductItem
import com.example.proyectofinal_prm.ui.pages.*
import kotlinx.coroutines.launch

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val navItemList = listOf(
        NavItem("Home", Icons.Default.Home, 0),
        NavItem("Alerts", Icons.Default.Notifications, 5),
        NavItem("Settings", Icons.Default.Settings, 0),
        NavItem("Profile", Icons.Default.Person, 0)
    )

    var selectedIndex by remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()
    var products by remember { mutableStateOf(listOf<ProductItem>()) }

    fun fetchProducts() {
        scope.launch {
            try {
                products = ApiClient.apiService.getProducts().data
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    LaunchedEffect(Unit) {
        fetchProducts()
    }

    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            Box(modifier = Modifier.fillMaxSize()) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        Box {
                            NavigationBar {
                                navItemList.take(2).forEachIndexed { index, navItem ->
                                    NavigationBarItem(
                                        selected = selectedIndex == index,
                                        onClick = { selectedIndex = index },
                                        icon = {
                                            BadgedBox(badge = {
                                                if (navItem.badgeCount > 0) Badge {
                                                    Text(text = navItem.badgeCount.toString())
                                                }
                                            }) {
                                                Icon(imageVector = navItem.icon, contentDescription = "Icon")
                                            }
                                        },
                                        label = { Text(text = navItem.label) }
                                    )
                                }

                                NavigationBarItem(
                                    selected = false,
                                    onClick = { },
                                    icon = { Box {} },
                                    label = { Text(text = "") }
                                )

                                navItemList.drop(2).forEachIndexed { index, navItem ->
                                    NavigationBarItem(
                                        selected = selectedIndex == index + 2,
                                        onClick = { selectedIndex = index + 2 },
                                        icon = {
                                            BadgedBox(badge = {
                                                if (navItem.badgeCount > 0) Badge {
                                                    Text(text = navItem.badgeCount.toString())
                                                }
                                            }) {
                                                Icon(imageVector = navItem.icon, contentDescription = "Icon")
                                            }
                                        },
                                        label = { Text(text = navItem.label) }
                                    )
                                }
                            }

                            FloatingActionButton(
                                onClick = { navController.navigate("create_product") },
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                                    .offset(y = (-30).dp)
                                    .size(66.dp)
                                    .clip(CircleShape)
                                    .zIndex(1f),
                                shape = CircleShape
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                            }
                        }
                    }
                ) { innerPadding ->
                    ContentScreen(
                        modifier = Modifier.padding(innerPadding),
                        selectedIndex = selectedIndex,
                        products = products,
                        refreshProducts = { fetchProducts() },
                        onEditProduct = { productId ->
                            navController.navigate("edit_product/$productId")
                        },
                        navController = navController
                    )
                }
            }
        }

        composable("create_product") {
            CreateProductScreen(
                onProductCreated = {
                    navController.popBackStack()
                    fetchProducts()
                }
            )
        }

        composable(
            "edit_product/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: return@composable
            EditProductScreen(productId = productId, navController = navController)
        }
    }
}

@Composable
fun ContentScreen(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    products: List<ProductItem>,
    refreshProducts: () -> Unit,
    onEditProduct: (Int) -> Unit,
    navController: NavHostController
) {
    when (selectedIndex) {
        0 -> HomePage(
            modifier = modifier,
            products = products,
            refreshProducts = refreshProducts,
            onEditProduct = onEditProduct,
            navController = navController
        )
        1 -> NotificationPage()
        2 -> SettingsPage()
        3 -> ProfilePage()
    }
}
