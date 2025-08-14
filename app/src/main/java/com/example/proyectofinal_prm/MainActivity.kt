package com.example.proyectofinal_prm

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyectofinal_prm.data.Session
import com.example.proyectofinal_prm.data.TokenStore
import com.example.proyectofinal_prm.ui.pages.login.LoginPage
import com.example.proyectofinal_prm.ui.pages.login.VerifyPage
import com.example.proyectofinal_prm.ui.pages.register.RegisterPage
import com.example.proyectofinal_prm.ui.theme.ProyectoFinal_PRMTheme

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val stored = TokenStore(this).load()
        if (!stored.isNullOrBlank()) {
            Session.setToken(stored)
        }
        setContent {
            ProyectoFinal_PRMTheme() {
                val navController = rememberNavController()
                AppNavigator()
            }
        }
    }
}

@Composable
fun AppNavigator() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "login") {
        composable("login") { LoginPage(navController) }
        composable("register") { RegisterPage(navController) }
        composable("verify") { VerifyPage(navController) }
        composable("home") { MainScreen() }
    }
}

