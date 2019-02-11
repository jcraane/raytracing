package nl.jamiecraane.raytracing.lights

import nl.jamiecraane.raytracing.Material
import java.awt.Color

fun Color.applyLightIntensity(diffuseLightIntensity: Float, specularLightIntensity: Float, material: Material): Color {
    val colors = material.diffuseColor.getColorComponents(FloatArray(4))
    val r =
        colors[0] * diffuseLightIntensity * material.albedo.x + specularLightIntensity * material.albedo.y
    val g =
        colors[1] * diffuseLightIntensity * material.albedo.x + specularLightIntensity * material.albedo.y
    val b =
        colors[2] * diffuseLightIntensity * material.albedo.x + specularLightIntensity * material.albedo.y
    return Color(Math.min(1F, r), Math.min(1F, g), Math.min(1F, b))
}
