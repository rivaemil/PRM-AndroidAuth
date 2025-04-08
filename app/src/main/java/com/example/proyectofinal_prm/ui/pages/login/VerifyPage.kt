package com.example.proyectofinal_prm.ui.pages.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectofinal_prm.ui.components.CodeInputField
import com.example.proyectofinal_prm.ui.components.Message
import com.example.proyectofinal_prm.ui.components.PrimaryButton
import com.example.proyectofinal_prm.ui.components.linkText

@Composable
fun VerifyPage(
    navController: NavController,
    modifier: Modifier = Modifier
)
{
    Column (
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    )
    {
        Spacer(modifier = Modifier.height(30.dp))
        Message(
            title = "Verify Code",
            subtitle = "Please enter the code we just sent to email"
        )
        linkText(
            text = "example@email.com",
            onClick = {navController.navigate("")}
        )

        Spacer(modifier = Modifier.height(10.dp))

        CodeInputField(codeLength = 6) { code ->
            println("Código ingresado: $code")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Don't receive OTP?",
            color = Color.Gray,
        )
        linkText(
            text = "Resend Code",
            onClick = {navController.navigate("")}
        )
        Spacer(modifier = Modifier.height(20.dp))

        PrimaryButton(
            onClick = { navController.navigate("login") },
            text = "Verify",
            modifier = Modifier
                .height(45.dp)
                .padding(horizontal = 24.dp)
            ,
            isNavigationArrowVisible = false,
        )
    }
}