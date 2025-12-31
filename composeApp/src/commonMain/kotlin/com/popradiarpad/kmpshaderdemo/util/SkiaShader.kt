package com.popradiarpad.kmpshaderdemo.util

import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.measureTime

/**
 * A Modifier that runs a shader on the Composable surface.
 * The shader gets some parameters and must be prepared to handle them.
 *
 * The shader must have the following uniforms:
 * * uniform float2 uSize;     // The size of the surface the shader is running in (width, height) pixels
 * * uniform float uTimeS;     // The time in seconds since entering into composition.
 * * uniform float2 uTouchPos; // The position of the touch event as Offset(x, y) in pixels.
 *
 * When @param color1 is not null then the shader must have the following uniform:
 *
 * layout(color) uniform half4 uColor1; // A color parameter
 */
@Composable
expect fun Modifier.runPointerInputShader(shaderCode: String, color1: Color? = null): Modifier

/**
 * The time as State since entering into composition in 60 FPS tact in second.
 */
@Composable
fun rememberTimeFullFPS_S() = produceState(0f) {
    while (true) {
        withInfiniteAnimationFrameMillis { frameTimeMillis ->
            value = frameTimeMillis / 1000f
        }
    }
}

/**
 * The time as State since entering into composition in circa 30 FPS tact in second.
 */
@Composable
fun rememberTime30FPS_S() = produceState(0f) {
    var v = Duration.ZERO
    while (true) {
        v += measureTime {
            delay(1000 / 29)
        }
        value = v.inWholeMicroseconds / 1e6f
    }
}

