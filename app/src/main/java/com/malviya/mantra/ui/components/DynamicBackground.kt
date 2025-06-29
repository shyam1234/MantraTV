package com.malviya.mantra.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.malviya.mantra.firebase.RemoteConfigService
import com.malviya.mantra.ui.theme.colorBackground
import timber.log.Timber

@Composable
fun DynamicBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val remoteConfigService = remember { RemoteConfigService() }
    var backgroundImageUrl by remember { mutableStateOf("") }
    var isBackgroundEnabled by remember { mutableStateOf(true) }
    var backgroundOpacity by remember { mutableFloatStateOf(0.4f) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        remoteConfigService.fetchAndActivate { success ->
            if (success) {
                backgroundImageUrl = remoteConfigService.getBackgroundImageUrl()
                isBackgroundEnabled = remoteConfigService.isBackgroundImageEnabled()
                backgroundOpacity = remoteConfigService.getBackgroundOpacity()
                Timber.d("Background config loaded: URL=$backgroundImageUrl, Enabled=$isBackgroundEnabled, Opacity=$backgroundOpacity")
            }
            isLoading = false
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Background Image
        if (isBackgroundEnabled && backgroundImageUrl.isNotEmpty() && !isLoading) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(backgroundImageUrl)
                    .allowRgb565(true)
                    .allowHardware(true)
                    .crossfade(true)
                    .build(),
                contentDescription = "Background Image",
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorBackground)
                    .alpha(backgroundOpacity),
                contentScale = ContentScale.Crop,
                onSuccess = {
                    Timber.d("Background image loaded successfully")
                },
                onError = {
                    Timber.e("Error loading background image: ${it.result}")
                }
            )
        } else {
            // Fallback background color
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorBackground)
            )
        }

        // Content
        content()
    }
} 