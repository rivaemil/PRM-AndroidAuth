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

@Composable
fun RegisterPage(
    navController: NavController,
){
    var isChecked by remember { mutableStateOf(false) }
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
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Email",
                modifier = Modifier.padding(horizontal = 24.dp),
            )
            InputField(
                placeholder = "Ingresa tu email",
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Contraseña",
                modifier = Modifier.padding(horizontal = 24.dp),
            )
            InputField(
                placeholder = "Ingresa tu contraseña",
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        PrimaryButton(
            onClick = { navController.navigate("home") },
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
