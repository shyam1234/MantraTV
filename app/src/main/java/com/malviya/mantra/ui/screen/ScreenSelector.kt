package com.malviya.mantra.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.malviya.mantra.R
import com.malviya.mantra.ui.components.DynamicBackground
import com.malviya.mantra.ui.theme.colorButtonGreen
import com.malviya.mantra.ui.theme.textColorButton

enum class ScreenType { 
    Counter, 
    Mala 
}

@Composable
fun ScreenSelector(
    onSelect: (ScreenType) -> Unit
) {
    DynamicBackground {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.choose_screen),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = textColorButton
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = { onSelect(ScreenType.Counter) },
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = colorButtonGreen
                )
            ) {
                Text(
                    text = stringResource(R.string.counter_screen),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = { onSelect(ScreenType.Mala) },
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = colorButtonGreen
                )
            ) {
                Text(
                    text = stringResource(R.string.mala_screen),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
} 