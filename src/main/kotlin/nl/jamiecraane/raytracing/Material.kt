package nl.jamiecraane.raytracing

import java.awt.Color

/**
 * @param albedo the proportion of the incident light or radiation that is reflected by a surface, typically that of a planet or moon.
 * @param diffuseColor
 * @param specularComponent
 */
class Material(val albedo: Vect3 = Vect3(1F, 0F), val diffuseColor: Color, val specularComponent: Float = 0F) {
    fun applyLightIntensity(
        diffuseLightIntensity: Float,
        specularLightIntensity: Float,
        reflectColor: Color
    ): Color {
        val colors = diffuseColor.getColorComponents(FloatArray(4))
        val reflectColors = reflectColor.getColorComponents(FloatArray(4))
        val r =
            colors[0] * diffuseLightIntensity * albedo.x + specularLightIntensity * albedo.y + (reflectColors[0] * albedo.z)
        val g =
            colors[1] * diffuseLightIntensity * albedo.x + specularLightIntensity * albedo.y + (reflectColors[1] * albedo.z)
        val b =
            colors[2] * diffuseLightIntensity * albedo.x + specularLightIntensity * albedo.y + (reflectColors[2] * albedo.z)
        return Color(Math.min(1F, r), Math.min(1F, g), Math.min(1F, b))
    }
}