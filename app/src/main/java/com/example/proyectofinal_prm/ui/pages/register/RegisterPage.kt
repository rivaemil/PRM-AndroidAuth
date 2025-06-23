package com.example.proyectofinal_prm.ui.pages.register

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectofinal_prm.ui.components.InputField
import com.example.proyectofinal_prm.ui.components.Message
import com.example.proyectofinal_prm.ui.components.PrimaryButton
import com.example.proyectofinal_prm.ui.components.linkText
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.example.proyectofinal_prm.data.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Composable
fun RegisterPage(
    navController: NavController,
){
    val context = LocalContext.current
    var isChecked by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Message(
            title = "Registro",
            subtitle = "Llena los campos para continuar"
        )

        Column (

        ) {
            Text(
                text = "Nombre",
                modifier = Modifier.padding(horizontal = 24.dp),
            )
            InputField(
                placeholder = "Ingresa tu nombre",
                value = name,
                onValueChange = { name = it },
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Email",
                modifier = Modifier.padding(horizontal = 24.dp),

            )
            InputField(
                placeholder = "Ingresa tu email",
                value = email,
                onValueChange = { email = it },
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Contraseña",
                modifier = Modifier.padding(horizontal = 24.dp),
            )
            InputField(
                placeholder = "Ingresa tu contraseña",
                value = password,
                onValueChange = { password = it }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        PrimaryButton(
            onClick = {
                val user = RegisterRequest(name, email, password)

                ApiClient.apiService.register(user).enqueue(object : Callback<AuthResponse> {
                    override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                        if (response.isSuccessful) {
                            Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                            navController.navigate("home")
                        } else {
                            Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                        Toast.makeText(context, "Fallo de red: ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
            },
            text = "Registrarse",
            modifier = Modifier
                .height(45.dp)
                .padding(horizontal = 24.dp)
            ,
            isNavigationArrowVisible = false,
            shadowColor = Color.Gray,
            cornerRadius = 20.dp,
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Línea antes del texto
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Or sign in with",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Text(
                text = "Already have account? ",
            )
            linkText(
                text = "Sing in",
                fontWeight = FontWeight.Bold,
                onClick = {navController.navigate("login")}
            )
        }


    }
}
