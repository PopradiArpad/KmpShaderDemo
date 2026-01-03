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
    
    uniform shader background; // The background, the variable name is indifferent, SkSL uses the first uniform shader type as the background.
                               // Not deliviered by the shader runner.
    uniform float2 uSize;      // The surface size, required by the shader runner
    uniform float uTimeS;      // The time in seconds since composition, required by the shader runner
    uniform float2 uTouchPos;  // The touch position, required by the shader runner.

    /*
    A periodic function on [0..1] mapping into [0..1] (when stretchFactor is 1)
    This function in three instances is used to get the R, G, B values for the hue.
    Having the same function with equally shifted phases creates beautiful periodic hue.
    */
    half wave(half x, half stretchFactor) {
        // 6.28318 is 2 * PI
        return sin(x * 6.28318) * 0.5 * stretchFactor + 0.5;
    }
        
    // Map color to float.    
    half3 hueToRgb(float hue) {
        // h is float to maintain precision over time, 
        // but the resulting colors can be half.
        half h_half = half(hue);
        half stretchFactor = 1.0;
    
        half r = wave(h_half, stretchFactor);
        half b = wave(h_half - 0.333, stretchFactor);
        half g = wave(h_half + 0.333, stretchFactor);
        
        return clamp(half3(r, g, b), 0.0, 1.0);
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
        float hue = fract(angle + uTimeS * 0.5);
        half4 color = half4(hueToRgb(hue), 1.0);
        
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
        return background.eval(pixel) + (color * intensity);
    }
"""

