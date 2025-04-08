package com.example.proyectofinal_prm.ui.pages.login

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectofinal_prm.ui.components.InputField
import com.example.proyectofinal_prm.ui.components.Message
import com.example.proyectofinal_prm.ui.components.PrimaryButton
import com.example.proyectofinal_prm.ui.components.linkText
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.example.proyectofinal_prm.api.ApiClient
import com.example.proyectofinal_prm.extensions.findActivity
import com.example.proyectofinal_prm.ui.components.SecondaryButton
import com.example.proyectofinal_prm.ui.components.SecondaryButtonPreview
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import java.util.concurrent.Executors

@Composable
fun LoginPage(
    navController: NavController,
    modifier: Modifier = Modifier
)
{
    val context = LocalContext.current
    val executor = ContextCompat.getMainExecutor(context)
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var error by remember { mutableStateOf("") }
//    val scope = rememberCoroutineScope()
//    var isChecked by remember { mutableStateOf(false) }
    var showBiometricError by remember { mutableStateOf(false) }
//  NO TOCAR!  val activity = (context as? Activity) as FragmentActivity

//    fun checkLaravelAuthAndNavigate() {
//        val sharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
//        val token = sharedPreferences.getString("token", null)
//
//        if (!token.isNullOrEmpty()) {
//            CoroutineScope(Dispatchers.IO).launch {
//                try {
//                    val response = RetrofitClient.retrofit.getUser("Bearer $token")
//                    withContext(Dispatchers.Main) {
//                        if (response.isSuccessful) {
//                            navController.navigate("home")
//                        } else {
//                            showBiometricError = true
//                        }
//                    }
//                } catch (e: Exception) {
//                    withContext(Dispatchers.Main) {
//                        showBiometricError = true
//                    }
//                }
//            }
//        } else {
//            showBiometricError = true
//        }
//    }

    Column (
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    )
    {
        Spacer(modifier = Modifier.height(30.dp))
        Message(
            title = "Inicio de Sesión",
            subtitle = "Bienvenido de vuelta, ingresa tus credenciales"
        )
        Spacer(modifier = Modifier.height(24.dp))

        InputField(
            placeholder = "Ingresa tu email",
        )

        Spacer(modifier = Modifier.height(24.dp))

        InputField(
            placeholder = "Ingresa tu contraseña",
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(1.dp))
            }

            linkText(
                text = "Olvidaste tu contraseña?",
                onClick = {navController.navigate("verify")}
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        PrimaryButton(
            onClick = {
                navController.navigate("home")
            },
            text = "Login",
            modifier = Modifier
                .height(45.dp)
                .padding(horizontal = 24.dp)
            ,
            isNavigationArrowVisible = false,
            shadowColor = Color.Gray,
            cornerRadius = 20.dp,
        )
        Spacer(modifier = Modifier.height(20.dp))

        SecondaryButton(
            onClick = {
//                val biometricManager = BiometricManager.from(context)
//                if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS) {
//                    val promptInfo = BiometricPrompt.PromptInfo.Builder()
//                        .setTitle("Biometric Login")
//                        .setSubtitle("Use fingerprint or face to login")
//                        .setNegativeButtonText("Cancel")
//                        .build()
//
//                    val biometricPrompt = BiometricPrompt(activity, executor,
//                        object : BiometricPrompt.AuthenticationCallback() {
//                            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
//                                super.onAuthenticationSucceeded(result)
//                                navController.navigate("home")
//                            }
//
//                            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
//                                super.onAuthenticationError(errorCode, errString)
//                                showBiometricError = true
//                            }
//                        })
//                    biometricPrompt.authenticate(promptInfo)
//                } else {
//                    showBiometricError = true
//                }
            },
            text = "Ingresa con huella digital",
            modifier = Modifier
                .height(45.dp)
                .padding(horizontal = 24.dp),
            isNavigationArrowVisible = false,
            shadowColor = Color.Transparent,
            cornerRadius = 20.dp,
        )

//        if (showBiometricError) {
//            Text(
//                text = "Biometric authentication not available or failed",
//                color = Color.Red,
//                modifier = Modifier.padding(top = 8.dp)
//            )
//        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Text(
                text = "Aun no tienes una cuenta?"
            )
            linkText(
                text = "Registrate",
                fontWeight = FontWeight.Bold,
                onClick = {navController.navigate("register")}
            )
        }
    }
}
