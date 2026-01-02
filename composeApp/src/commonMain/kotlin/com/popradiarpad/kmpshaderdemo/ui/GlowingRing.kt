package com.popradiarpad.kmpshaderdemo.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.popradiarpad.kmpshaderdemo.util.runPointerInputTimeBackgroundShader

@Composable
fun GlowingRing(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.runPointerInputTimeBackgroundShader(ROTATING_HUE_GLOWING_RING_SHADER),
    ) {}
}

/**
 * A shader for [Modifier.runPointerInputTimeBackgroundShader] shader runner.
 *
 * The shader shall use SkSL to be multiplatform:
 * SkSL will be translated into the platform specific shader languages.
 */
// language=GLSL
private const val ROTATING_HUE_GLOWING_RING_SHADER = """
    /*
    Put a glowing ring above the background using parameters from the shader runner (tap position and time for the rotation effect).
    
    We have 3 aspects:
    * Position (where to draw)
    * Hue (which color)
    * Intensity (of the color)
    
    The form is rotational symmetric so the geometry get simplified to a radial half line from the circle center.
    
    For the hue we need a mathematical formula to map hue from angle.
    
    The intensity will be a mix of linear amplification and exponential decay on the radial line.
    
    In the calculations we often use normalized ranges to keep the overview.
    */
    
    uniform shader content;    // The background content, the variable name is indifferent, SkSL uses the first uniform shader type as the background.
                               // Not deliviered by the shader runner.
    uniform float2 uSize;      // The surface size, required by the shader runner
    uniform float uTimeS;      // The time in seconds since composition, required by the shader runner
    uniform float2 uTouchPos;  // The touch position, required by the shader runner.

    /*
    A periodic function on [0..1] mapping into [0..1] (when stretchFactor is 1)
    This function in three instances is used to get the R, G, B values for the hue.
    Having the same function with equally shifted phases creates beautiful periodic hue.
    */
    float wave(float x, float stretchFactor) {
        return sin(x * 6.2832) * 0.5 * stretchFactor + 0.5;
    }
        
    // Map color to float.    
    half4 hueToRgba(float h) {
        // Bigger then 1 stretches the sinus out of [0..1]
        // which later will be clamped back, and the flat sections
        // cause a peaking effect in the color circle.
        float stretchFactor = 1;
    
        float r = wave(h, stretchFactor);
        float b = wave(h - 0.333, stretchFactor);
        float g = wave(h + 0.333, stretchFactor);
        return half4(clamp(float3(r, g, b), 0.0, 1.0), 1.0);
    }
    
    // Map color to pixel(position).
    half4 main(float2 pixel) {
        // Get direction vector from touch to current pixel.
        float2 direction = pixel - uTouchPos;
        float d = length(direction);

        // Determine hue.
        // Calculate the angle (0 to 2*PI) and normalize it to [0..1] for the Hue.
        // atan2 returns angle; we add uTimeS to make the colors spin.
        float angle = atan(direction.y, direction.x) / 6.28318 + 0.5;
        float hue = fract(angle + uTimeS * 0.2);
        half4 color = hueToRgba(hue);
        
        // Determine intensity: the glow around the ring.
        // Think in the half line from circle center over the pixel positon.
        // The center is the origo.
        // We want a smooth glowing:
        // From center to (radius - glowThickness) nothing,
        // then linearly amplified up to radius,
        // then exponential decay. 
        float radius = 300.0; 
        float glowThickness = 30.0;
        float mask = smoothstep(radius - glowThickness, radius, d);
        float intensity = mask * exp(-abs(d - radius) * 0.03);
        
        // Mix the background content with the glow.
        return content.eval(pixel) + (color * intensity);
    }
"""

