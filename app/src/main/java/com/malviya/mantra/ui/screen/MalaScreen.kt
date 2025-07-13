package com.malviya.mantra.ui.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.malviya.mantra.ui.components.MantraScreenLayout
import com.malviya.mantra.ui.viewmodel.ChantViewModel
import com.malviya.mantra.ui.viewmodel.ONE_MALA_ROUND_COUNT
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun MalaScreen(
    name: String,
    viewModel: ChantViewModel
) {
    val remoteConfigService by viewModel.remoteConfigService.collectAsState()
    val count by viewModel.count.collectAsState()
    val color by viewModel.color.collectAsState()

    // Animation for mala rotation
    val rotation by animateFloatAsState(
        targetValue = (count * 360f / ONE_MALA_ROUND_COUNT),
        animationSpec = tween(durationMillis = 500),
        label = "mala_rotation"
    )

    MantraScreenLayout(
        name = name,
        viewModel = viewModel,
        flashMessage = remoteConfigService.getFlashMessage(),
        centerContent = {
            MalaVisualization(
                currentBead = count,
                totalBeads = ONE_MALA_ROUND_COUNT,
                rotation = rotation,
                color = color,
                onBeadClick = { viewModel.incrementCount(true) }
            )
        },
        onIncrement = { viewModel.incrementCount(true) },
        onDecrement = { viewModel.decrementCount(true) }
    )
}

@Composable
fun MalaVisualization(
    currentBead: Int,
    totalBeads: Int,
    rotation: Float,
    color: Color,
    onBeadClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(300.dp)
            .clickable { onBeadClick() },
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .size(280.dp)
                .rotate(rotation)
        ) {
            val center = Offset(size.width / 2, size.height / 2)
            val radius = size.minDimension / 2.5f
            val beadRadius = 8.dp.toPx()
            
            // Draw all beads
            for (i in 0 until totalBeads) {
                val angle = (2 * Math.PI * i / totalBeads).toFloat()
                val x = center.x + radius * cos(angle)
                val y = center.y + radius * sin(angle)
                
                val beadColor = when {
                    i == currentBead -> color // Current bead gets the feedback color
                    i < currentBead -> Color.Gray // Completed beads
                    else -> Color.White // Future beads
                }
                
                drawCircle(
                    color = beadColor,
                    radius = beadRadius,
                    center = Offset(x, y)
                )
                
                // Draw bead outline
                drawCircle(
                    color = Color.Black,
                    radius = beadRadius,
                    center = Offset(x, y),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.dp.toPx())
                )
            }
            
            // Draw center point
            drawCircle(
                color = Color.Yellow,
                radius = 4.dp.toPx(),
                center = center
            )
        }
        
        // Display current count in center
        Text(
            text = "$currentBead",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

 