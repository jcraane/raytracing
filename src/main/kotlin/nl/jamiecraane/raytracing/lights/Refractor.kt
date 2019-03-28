package nl.jamiecraane.raytracing.lights

import nl.jamiecraane.raytracing.buildingblocks.Vect3

/**
 * Snell's law.
 */
object Refractor {
    fun refract(I: Vect3, N: Vect3, eta_t: Double, eta_i: Double = 1.0): Vect3 {
        val cosi = -Math.max(-1.0, Math.min(1.0, I.dotProduct(N)))
        if (cosi < 0) {
            return refract(I, -N, eta_i, eta_t)
        }
        val eta = eta_i / eta_t
        val k = 1 - eta * eta * (1 - cosi * cosi)
        return if (k < 0) {
            return Vect3(1.0, 0.0, 0.0)
        } else {
            I.scale(eta) + N.scale(eta * cosi - Math.sqrt(k.toDouble()))
        }
    }
}