package com.popradiarpad.kmpshaderdemo

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.popradiarpad.kmpshaderdemo.screen.MainScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        MainScreen()
    }
}