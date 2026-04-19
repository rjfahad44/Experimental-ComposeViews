package com.bitbytestudio.experimental_composeviews.ui.experiment.liquidGlassEffect

import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import android.graphics.Shader
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@Composable
fun GlassContainer(
    modifier: Modifier = Modifier,
    rippleState: GlassRippleState,
    content: @Composable BoxScope.() -> Unit
) {
    val isAgslSupported = remember {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    }

    val runtimeShader = remember {
        if (isAgslSupported) {
            RuntimeShader(LIQUID_SHADER)
        } else null
    }

    val shape = remember { RoundedCornerShape(24.dp) }

    val effect = remember(runtimeShader) {
        runtimeShader?.let {
            RenderEffect
                .createRuntimeShaderEffect(it, "input")
                .asComposeRenderEffect()
        }
    }

    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    rippleState.trigger(offset)
                }
            }
            .then(
                if (isAgslSupported && effect != null) {
                    Modifier.graphicsLayer {
                        // SAFE: only called when shader exists
                        runtimeShader?.let { shader ->

                            shader.setFloatUniform(
                                "resolution",
                                size.width,
                                size.height
                            )

                            shader.setFloatUniform(
                                "touch",
                                rippleState.touch.x / size.width,
                                rippleState.touch.y / size.height
                            )

                            shader.setFloatUniform("time", rippleState.time)
                            shader.setFloatUniform("ripple", rippleState.ripple)
                        }

                        renderEffect = effect
                    }
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    Modifier.graphicsLayer {
                        renderEffect = RenderEffect
                            .createBlurEffect(25f, 25f, Shader.TileMode.CLAMP)
                            .asComposeRenderEffect()
                    }
                } else {
                    Modifier.blur(16.dp) // basic fallback
                }
            )
            .clip(shape)
            .background(
                Color.White.copy(
                    alpha = if (isAgslSupported) 0.06f else 0.12f
                )
            )
            .border(
                1.dp,
                Color.White.copy(alpha = 0.2f),
                shape
            )
            .padding(16.dp)
    ) {
        content()
    }
}