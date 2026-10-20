package com.example.appinteractiva

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.appinteractiva.tareas.presentation.navigation.TareasApp
import com.example.appinteractiva.ui.theme.TareasEscolaresTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TareasEscolaresTheme {
                TareasApp()
            }
        }
    }
}