package com.popradiarpad.kmpshaderdemo.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.popradiarpad.kmpshaderdemo.ui.Background
import com.popradiarpad.kmpshaderdemo.ui.GlowingRing
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MainScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Background()
        GlowingRing(Modifier.fillMaxSize())
    }
}



@Preview
@Composable
fun MainScreenPreview() {
    MainScreen()
}