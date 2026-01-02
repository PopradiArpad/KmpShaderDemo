package com.popradiarpad.kmpshaderdemo.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import kmpshaderdemo.composeapp.generated.resources.Res
import kmpshaderdemo.composeapp.generated.resources.background
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Background() {
    Image(
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop, // This makes it behave like a background
        painter = painterResource(Res.drawable.background),
        contentDescription = null,
    )
}



@Preview
@Composable
fun BackgroundPreview() {
    Background()
}