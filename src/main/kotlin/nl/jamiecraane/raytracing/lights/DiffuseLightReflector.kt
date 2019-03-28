package nl.jamiecraane.raytracing.lights

import nl.jamiecraane.raytracing.buildingblocks.Vect3

object DiffuseLightReflector {
    fun calculateIntensity(lightDirection: Vect3, normalVector: Vect3, light: Light) =
        light.intensity * Math.max(0.0, lightDirection.dotProduct(normalVector))
}