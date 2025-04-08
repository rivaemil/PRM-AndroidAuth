package com.example.proyectofinal_prm.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun CodeInputField(
    codeLength: Int = 6, // Número de dígitos que quieres en el código
    onCodeChange: (String) -> Unit
) {
    var code by remember { mutableStateOf("") }
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (i in 0 until codeLength) {
            OutlinedTextField(
                value = code.getOrNull(i)?.toString() ?: "",
                onValueChange = { input ->
                    if (input.length <= 1 && input.all { it.isDigit() }) {
                        if (input.isNotEmpty() && code.length < codeLength) {
                            code = code + input
                        } else if (input.isEmpty() && i < code.length) {
                            code = code.removeRange(i, i + 1)
                        }
                        onCodeChange(code)
                    }
                },
                placeholder = {
                    Text("-")
                },
                textStyle = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Center
                ),
                singleLine = true,
                modifier = Modifier
                    .width(48.dp)
                    .height(56.dp)
            )
        }
    }
}