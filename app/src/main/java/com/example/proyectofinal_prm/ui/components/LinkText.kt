package com.example.proyectofinal_prm.ui.components


import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun linkText(
    modifier:Modifier = Modifier,
    text:String = "Link",
    fontWeight: FontWeight = FontWeight.Normal,
    onClick: () -> Unit
)
{
    Text(
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    textDecoration = TextDecoration.Underline,
                    fontWeight = fontWeight
                )
            ) {
                append(text)
            }
        },
        modifier = modifier.clickable { onClick() }
    )
}