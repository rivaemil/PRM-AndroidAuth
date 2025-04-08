package com.example.proyectofinal_prm.ui.pages.login

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.activity.compose.LocalActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import com.example.proyectofinal_prm.ui.components.InputField
import com.example.proyectofinal_prm.ui.components.Message
import com.example.proyectofinal_prm.ui.components.PrimaryButton
import com.example.proyectofinal_prm.ui.components.SecondaryButton
import com.example.proyectofinal_prm.ui.components.linkText
import java.util.concurrent.Executors

@Composable
fun LoginPage(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activity = LocalActivity.current as FragmentActivity // Necesario para BiometricPrompt
    var showBiometricError by remember { mutableStateOf(false) }
    var biometricErrorText by remember { mutableStateOf("") }

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
                onClick = { navController.navigate("verify") }
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
                        Handler(Looper.getMainLooper()).post {
                            navController.navigate("home")
                        }
                    },
                    authFailed = {
                        showBiometricError = true
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

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Aun no tienes una cuenta?")
            linkText(
                text = "Registrate",
                fontWeight = FontWeight.Bold,
                onClick = { navController.navigate("register") }
            )
        }
    }
}
