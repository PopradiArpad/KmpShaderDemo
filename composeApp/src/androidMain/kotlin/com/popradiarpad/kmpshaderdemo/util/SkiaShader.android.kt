package com.popradiarpad.kmpshaderdemo.util

import android.graphics.RuntimeShader
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged

//// Maybe the most straightforward shader application:
//// Go as fast as possible to high-level: make a brush from the shader
//@Composable
//actual fun Modifier.skiaShader(shaderCode: String, time: Float?, color1: Color?): Modifier =
//    drawWithCache {
//        val shader = RuntimeShader(shaderCode)
//
//        shader.setFloatUniform("uSize", size.width, size.height)
//        if (color1 != null) shader.setColorUniform("uColor1", color1.toAndroidColor)
//        if (time != null) shader.setFloatUniform("uTime", time)
//
//        val brush = ShaderBrush(shader)
//
//        onDrawBehind {
//            drawRect(brush)
//        }
//    }

// Other shader applications.
// They need a triggering graphics layer in the Modifier chain
// defined after this modifier. Like a
//        .border(2.dp, Color.Red, RoundedCornerShape(24.dp))
//
//@Composable
//actual fun Modifier.skiaShader(shaderCode: String, time: Float?, color1: Color?): Modifier {
//    val shader = remember(shaderCode) { RuntimeShader(shaderCode) }
//    if (time != null) shader.setFloatUniform("uTime", time)
//
//    // Paint Bucket (Brush) version
//    // When the underlying content doesn't get mixed in into the result.
//    // This works without already existing content.
//    // return background(ShaderBrush(shader))
//
//    // Processing Engine version
//    // When the underlying content will be mixed in the shader.
//    return graphicsLayer {
//        shader.setFloatUniform("uSize", size.width, size.height)
//
//        renderEffect = RenderEffect
//            .createRuntimeShaderEffect(shader, "content")
//            .asComposeRenderEffect()
//    }
//}



private val Color.toAndroidColor: android.graphics.Color
    get() = android.graphics.Color.valueOf(toArgb())

@Composable
actual fun Modifier.runPointerInputTimeShader(
    shaderCode: String, color1: Color?
): Modifier {
    val shader = remember(shaderCode) { RuntimeShader(shaderCode) }
    val brush = remember(shader) { ShaderBrush(shader) }
    val timeS by rememberTimeMaxFPS_S()
    remember(shader, color1) {
        if (color1 != null) shader.setColorUniform("uColor1", color1.toAndroidColor)
        true
    }
    var touchPos by remember { mutableStateOf(Offset.Zero) }
    remember(shader, touchPos) {
        shader.setFloatUniform("uTouchPos", touchPos.x, touchPos.y)
        true
    }

    return pointerInput(Unit) {
        detectTapGestures {
            touchPos = it
//            l.d { "touchPos: $touchPos" }
        }
    }
        .onSizeChanged { size ->
//            l.d { "size: $size" }
            shader.setFloatUniform("uSize", size.width.toFloat(), size.height.toFloat())
        }
        .drawWithCache {
//            l.d { "timeS: $timeS" }
            shader.setFloatUniform("uTimeS", timeS)

            onDrawBehind {
                drawRect(brush)
            }
        }
}