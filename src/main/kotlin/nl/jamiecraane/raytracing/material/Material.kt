package nl.jamiecraane.raytracing.material

import nl.jamiecraane.raytracing.WhatToRender
import nl.jamiecraane.raytracing.extensions.component1
import nl.jamiecraane.raytracing.extensions.component2
import nl.jamiecraane.raytracing.extensions.component3
import nl.jamiecraane.raytracing.scene.SceneDsl
import java.awt.Color
import java.util.*

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
        refractColor: Color,
        whatToRender: EnumSet<WhatToRender>
    ): Color {
        val (color_r, color_g, color_b) = diffuseColor
        val (reflect_r, reflect_g, reflect_b) = reflectColor
        val (refract_r, refract_g, refract_b) = refractColor

        var r = color_r
        var g = color_g
        var b = color_b

        if (whatToRender.contains(WhatToRender.DIFFUSE)) {
            val diffuseColor = diffuseLightIntensity * albedo.a
            r *= diffuseColor
            g *= diffuseColor
            b *= diffuseColor
        }

        if (whatToRender.contains(WhatToRender.SPECULAR)) {
            val specularColor = specularLightIntensity * albedo.b
            r += specularColor
            g += specularColor
            b += specularColor
        }

        if (whatToRender.contains(WhatToRender.REFLECTION)) {
            r += reflect_r * albedo.c
            g += reflect_g * albedo.c
            b += reflect_b * albedo.c
        }

        if (whatToRender.contains(WhatToRender.REFRACTION)) {
            r += refract_r * albedo.d
            g += refract_g * albedo.d
            b += refract_b * albedo.d
        }

        return Color(Math.min(1F, r), Math.min(1F, g), Math.min(1F, b))
    }
}
