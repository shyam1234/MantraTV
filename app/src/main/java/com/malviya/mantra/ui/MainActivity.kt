package com.malviya.mantra.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.ui.res.stringResource
import com.malviya.mantra.R
import com.malviya.mantra.screen.GreetingScreen
import com.malviya.mantra.ui.theme.MantraTheme

class MainActivity : ComponentActivity() {
    private val chantViewModel: ChantViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MantraTheme {
                /*Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = stringResource(R.string.greeting),
                        modifier = Modifier.padding(innerPadding)
                    )
                }*/
                MantraTheme {
                    GreetingScreen( name = stringResource(R.string.greeting),chantViewModel)
                }
            }
        }
    }
}

