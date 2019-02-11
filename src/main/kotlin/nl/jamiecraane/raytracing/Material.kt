package nl.jamiecraane.raytracing

import java.awt.Color

class Material(val albedo: Vect3 = Vect3(1F, 0F), val diffuseColor: Color, val specularComponent: Float = 0F) {
    fun applyLightIntensity(diffuseLightIntensity: Float, specularLightIntensity: Float): Color {
        val colors = diffuseColor.getColorComponents(FloatArray(4))
        val r =
            colors[0] * diffuseLightIntensity * albedo.x + specularLightIntensity * albedo.y
        val g =
            colors[1] * diffuseLightIntensity * albedo.x + specularLightIntensity * albedo.y
        val b =
            colors[2] * diffuseLightIntensity * albedo.x + specularLightIntensity * albedo.y
        return Color(Math.min(1F, r), Math.min(1F, g), Math.min(1F, b))
    }
}