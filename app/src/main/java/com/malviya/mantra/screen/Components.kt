package com.malviya.mantra.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun MantraRender(name: String) {
    Text(
        text = name,
        style = MaterialTheme.typography.titleLarge.copy(fontSize = 48.sp, lineHeight = 80.sp),
        textAlign = TextAlign.Center,
        color = Color.Black,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(1f)
    )
}

@Composable
fun GrayCircleWithNumber(number: Int) {
    Box(
        modifier = Modifier
            .padding(10.dp)
            .size(140.dp) // Adjust size as needed
            .background(Color.Gray, shape = CircleShape), contentAlignment = Alignment.Center

    ) {
        Text(
            fontWeight = FontWeight.Bold,
            fontSize = 44.sp,
            text = number.toString(),
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge // Adjust text style as needed
        )
    }
}
