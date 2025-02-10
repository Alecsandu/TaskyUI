package com.tasky.taskyui.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tasky.taskyui.navigation.AppNavGraph
import com.tasky.taskyui.ui.theme.TaskyUITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskyUITheme {
                AppNavGraph()
            }
        }
    }
}
