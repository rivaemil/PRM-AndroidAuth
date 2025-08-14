package com.example.proyectofinal_prm.ui.pages.login

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import com.example.proyectofinal_prm.ui.components.*
import java.util.concurrent.Executors
import com.example.proyectofinal_prm.data.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun LoginPage(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activity = LocalActivity.current as FragmentActivity
    val tokenStore = remember { TokenStore(context) }

    var showBiometricError by remember { mutableStateOf(false) }
    var biometricErrorText by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    fun verifyBiometricAuth(
        activityContext: Context,
        authSuccess: () -> Unit,
        authFailed: (errorText: String) -> Unit
    ) {
        val biometricExecutor = Executors.newSingleThreadExecutor()
        val bioManager = BiometricManager.from(activityContext)

        when (bioManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                val authPrompt = BiometricPrompt(
                    activityContext as FragmentActivity,
                    biometricExecutor,
                    object : BiometricPrompt.AuthenticationCallback() {
                        override fun onAuthenticationError(code: Int, errMsg: CharSequence) {
                            authFailed("Error de autenticación: $errMsg")
                        }
                        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                            authSuccess()
                        }
                        override fun onAuthenticationFailed() {
                            authFailed("La huella no coincide con nuestros registros")
                        }
                    }
                )

                val promptConfig = BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Verificación Biométrica")
                    .setDescription("Coloca tu dedo en el sensor para identificarte")
                    .setConfirmationRequired(false)
                    .setNegativeButtonText("Cancelar")
                    .build()

                authPrompt.authenticate(promptConfig)
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                authFailed("No hay huellas registradas en el dispositivo")
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                authFailed("El sensor biométrico no está disponible")
            }
            else -> {
                authFailed("Dispositivo no compatible con autenticación biométrica")
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Message(
            title = "Inicio de Sesión",
            subtitle = "Bienvenido de vuelta, ingresa tus credenciales"
        )
        Spacer(modifier = Modifier.height(24.dp))

        InputField(
            placeholder = "Ingresa tu email",
            value = email,
            onValueChange = { email = it },
        )

        Spacer(modifier = Modifier.height(24.dp))

        InputField(
            placeholder = "Ingresa tu contraseña",
            value = password,
            onValueChange = { password = it },
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(1.dp))
            }
            linkText(
                text = "Olvidaste tu contraseña?",
                onClick = { navController.navigate("verify") }
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        PrimaryButton(
            onClick = {
                // 1) Llamada de login: guarda el token en Session + TokenStore
                val user = LoginRequest(email.trim(), password)
                ApiClient.apiService.login(user).enqueue(object : Callback<AuthResponse> {
                    override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                        if (response.isSuccessful) {
                            val token = response.body()?.access_token
                            if (!token.isNullOrBlank()) {
                                // Memoria
                                Session.setToken(token)
                                // Persistencia
                                tokenStore.save(token)
                                Toast.makeText(context, "Login exitoso", Toast.LENGTH_SHORT).show()
                                navController.navigate("home")
                            } else {
                                Toast.makeText(context, "Respuesta inválida: no hay token", Toast.LENGTH_LONG).show()
                            }
                        } else {
                            Toast.makeText(context, "Credenciales inválidas", Toast.LENGTH_LONG).show()
                        }
                    }
                    override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                        Toast.makeText(context, "Fallo de red: ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
            },
            text = "Login",
            modifier = Modifier
                .height(45.dp)
                .padding(horizontal = 24.dp),
            isNavigationArrowVisible = false,
            shadowColor = Color.Gray,
            cornerRadius = 20.dp,
        )

        Spacer(modifier = Modifier.height(20.dp))

        SecondaryButton(
            onClick = {
                verifyBiometricAuth(
                    activityContext = activity,
                    authSuccess = {
                        // 2) Biometría: usa token guardado previamente
                        val stored = tokenStore.load()
                        if (!stored.isNullOrBlank()) {
                            Session.setToken(stored)
                            Handler(Looper.getMainLooper()).post {
                                navController.navigate("home")
                            }
                        } else {
                            Handler(Looper.getMainLooper()).post {
                                Toast.makeText(
                                    context,
                                    "No hay sesión guardada. Inicia con email una vez.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    },
                    authFailed = {
                        showBiometricError = true
                        biometricErrorText = it
                    }
                )
            },
            text = "Ingresa con huella digital",
            modifier = Modifier
                .height(45.dp)
                .padding(horizontal = 24.dp),
            isNavigationArrowVisible = false,
            shadowColor = Color.Transparent,
            cornerRadius = 20.dp,
        )

        if (showBiometricError) {
            Text(
                text = biometricErrorText,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Aun no tienes una cuenta?")
            linkText(
                text = "Registrate",
                fontWeight = FontWeight.Bold,
                onClick = { navController.navigate("register") }
            )
        }
    }
}
