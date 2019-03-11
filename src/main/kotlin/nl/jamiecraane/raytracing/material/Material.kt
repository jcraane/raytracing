package nl.jamiecraane.raytracing.material

import java.awt.Color

/**
 * @param albedo the proportion of the incident light or radiation that is reflected by a surface, typically that of a planet or moon.
 * @param diffuseColor
 * @param specularComponent
 */
class Material(private val albedo: Albedo = Albedo(1F), val diffuseColor: Color, val specularComponent: Float = 0F, val refractiveIndex: Float = 1F) {
    fun applyLightIntensity(
        diffuseLightIntensity: Float,
        specularLightIntensity: Float,
        reflectColor: Color,
        refractColor: Color
    ): Color {
        val colors = diffuseColor.getColorComponents(FloatArray(4))
        val reflectColors = reflectColor.getColorComponents(FloatArray(4))
        val refractColors = refractColor.getColorComponents(FloatArray(4))
        val r =
            colors[0] * diffuseLightIntensity * albedo.a + specularLightIntensity * albedo.b + (reflectColors[0] * albedo.c) + (refractColors[0] * albedo.d)
        val g =
            colors[1] * diffuseLightIntensity * albedo.a + specularLightIntensity * albedo.b + (reflectColors[1] * albedo.c) + (refractColors[1] * albedo.d)
        val b =
            colors[2] * diffuseLightIntensity * albedo.a + specularLightIntensity * albedo.b + (reflectColors[2] * albedo.c) + (refractColors[2] * albedo.d)
        return Color(Math.min(1F, r), Math.min(1F, g), Math.min(1F, b))
    }
}