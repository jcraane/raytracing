package nl.jamiecraane.raytracing.lights

import nl.jamiecraane.raytracing.Vect3
import nl.jamiecraane.raytracing.material.Material

object SpecularLightReflector {
    fun calculateIntensity(
        lightDirection: Vect3,
        normalVector: Vect3,
        rayDirection: Vect3,
        light: Light,
        material: Material
    ): Float {
        return (Math.pow(
            Math.max(
                0F,
                Reflector.reflect(lightDirection, normalVector).dotProduct(rayDirection)
            ).toDouble(), material.specularComponent.toDouble()
        ) * light.intensity).toFloat()
    }
}