package com.bitbytestudio.experimental_composeviews.ui.experiment.liquidGlassEffect

const val LIQUID_SHADER = """
uniform shader input;
uniform float2 resolution;
uniform float2 touch;
uniform float time;
uniform float ripple;

half4 main(float2 fragCoord) {
    float2 uv = fragCoord / resolution;

    // Distance from touch
    float dist = distance(uv, touch);

    // Ripple wave
    float wave = sin(20.0 * dist - time * 6.0) * ripple;

    // Liquid distortion
    float2 offset = normalize(uv - touch) * wave * 0.03;

    float2 distortedUV = uv + offset;

    return input.eval(distortedUV * resolution);
}
"""