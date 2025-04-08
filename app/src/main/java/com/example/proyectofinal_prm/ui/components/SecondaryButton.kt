package com.example.proyectofinal_prm.ui.components


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SecondaryButton(
    onClick: () -> Unit,
    text: String,
    isNavigationArrowVisible: Boolean,
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    shadowColor: Color = Color.Transparent,
    navigationIcon: ImageVector? = null,
    cornerRadius: Dp = 4.dp
){
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(62.dp)
            .shadow(
                elevation = 4.dp,
                spotColor = shadowColor
            ),
        colors = colors,
        shape = RoundedCornerShape(cornerRadius)
    ){
        if(isNavigationArrowVisible && navigationIcon != null){
            Icon(
                imageVector = navigationIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondary
            )
        }
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview
@Composable
fun SecondaryButtonPreview(){
    PrimaryButton(
        onClick = {},
        text = "Next",
        isNavigationArrowVisible = false,
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
            containerColor = Color.DarkGray
        ),
        shadowColor = Color.Gray,
        cornerRadius = 8.dp
    )
}