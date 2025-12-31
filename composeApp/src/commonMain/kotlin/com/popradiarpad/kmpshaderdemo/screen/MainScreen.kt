package com.popradiarpad.kmpshaderdemo.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import com.popradiarpad.kmpshaderdemo.util.runPointerInputShader
import kmpshaderdemo.composeapp.generated.resources.Res
import kmpshaderdemo.composeapp.generated.resources.background
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

// language=GLSL
const val HUE_GLOW_SHADER1 = """
    uniform shader content;
    uniform float2 uSize;
    uniform float uTimeS;
    uniform float2 uTouchPos;
    layout(color) uniform half4 uColor1;
    
    float wave(float x, float stretchingFactor) {
        return sin(x * 6.2832) * 0.5 * stretchingFactor + 0.5;
    }
        
    half4 hueToRgba(float h) {
        // Bigger then 1 stretches the sinus out of [0..1]
        // which later will be clamped back, and the flat sections
        // cause a peaking in the color circle.
        // float stretchingFactor = max(1+sin(uTimeS * 0.2), 1);
        float stretchingFactor = 1;
    
        float r = wave(h, stretchingFactor);
        float b = wave(h - 0.333, stretchingFactor);
        float g = wave(h + 0.333, stretchingFactor);
        return half4(clamp(float3(r, g, b), 0.0, 1.0), 1.0);
    }
    
    half4 main(float2 fragCoord) {
        // 1. Get direction vector from touch to current pixel
        float2 delta = fragCoord - uTouchPos;
        float d = length(delta);
        
        // 2. Calculate the angle (0 to 2*PI) and map to 0.0-1.0 for the Hue
        // atan2 returns angle; we add uTimeS to make the colors spin!
        float angle = atan(delta.y, delta.x) / 6.28318 + 0.5;
        float hue = fract(angle + uTimeS * 0.2);
        
        half4 color = hueToRgba(hue);
        
        // 3. Create the Glow
        // We want a "ring" that glows. 
        // 'abs(d - radius)' makes the intensity highest at the radius.
        float radius = 300.0; 
        float glowThickness = 30.0;
        // float intensity = exp(-abs(d - radius) / glowThickness);
        // float mask = step(radius, d);
        float mask = smoothstep(radius - glowThickness, radius, d);
        float intensity = mask * exp(-abs(d - radius) * 0.03);
        
        // 4. Final Output
        // Add the glow to the background content
        return content.eval(fragCoord) + (color * intensity);
    }
"""

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

@Composable
private fun Background() {
    Image(
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop, // This makes it behave like a background
        painter = painterResource(Res.drawable.background),
        contentDescription = null,
    )
}

@Composable
private fun GlowingRing(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.runPointerInputShader(HUE_GLOW_SHADER1, color1 = Color.Green),
    ) {}
}



@Preview
@Composable
fun MainScreenPreview() {
    MainScreen()
}