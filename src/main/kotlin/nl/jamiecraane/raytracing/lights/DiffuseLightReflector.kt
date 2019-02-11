package nl.jamiecraane.raytracing.lights

import nl.jamiecraane.raytracing.Vect3

object DiffuseLightReflector {
    fun calculateIntensity(lightDirection: Vect3, normalVector: Vect3, light: Light) =
        light.intensity * Math.max(0F, lightDirection.dotProduct(normalVector))
}