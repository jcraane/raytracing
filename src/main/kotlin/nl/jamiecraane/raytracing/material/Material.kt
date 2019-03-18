package nl.jamiecraane.raytracing.material

import nl.jamiecraane.raytracing.scene.SceneDsl
import nl.jamiecraane.raytracing.extensions.component1
import nl.jamiecraane.raytracing.extensions.component2
import nl.jamiecraane.raytracing.extensions.component3
import java.awt.Color

/**
 * @param albedo the proportion of the incident light or radiation that is reflected by a surface, typically that of a planet or moon.
 * @param diffuseColor
 * @param specularComponent
 */
@SceneDsl
class Material(val diffuseColor: Color) {
    var albedo = Albedo(1F)
    var specularComponent = 0F
    var refractiveIndex = 1F

    fun applyLightIntensity(
        diffuseLightIntensity: Float,
        specularLightIntensity: Float,
        reflectColor: Color,
        refractColor: Color
    ): Color {
        val (color_r, color_g, color_b) = diffuseColor
        val (reflect_r, reflect_g, reflect_b) = reflectColor
        val (refract_r, refract_g, refract_b) = refractColor

        val r =
            color_r * diffuseLightIntensity * albedo.a + specularLightIntensity * albedo.b + (reflect_r * albedo.c) + (refract_r * albedo.d)
        val g =
            color_g * diffuseLightIntensity * albedo.a + specularLightIntensity * albedo.b + (reflect_g * albedo.c) + (refract_g * albedo.d)
        val b =
            color_b * diffuseLightIntensity * albedo.a + specularLightIntensity * albedo.b + (reflect_b * albedo.c) + (refract_b * albedo.d)
        return Color(Math.min(1F, r), Math.min(1F, g), Math.min(1F, b))
    }
}

