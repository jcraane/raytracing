package nl.jamiecraane.raytracing.lights

import nl.jamiecraane.raytracing.Vect3

/**
 * Snell's law.
 */
object Refractor {
    fun refract(I: Vect3, N: Vect3, eta_t: Float, eta_i: Float = 1F): Vect3 {
        val cosi = -Math.max(-1F, Math.min(1F, I.dotProduct(N)))
        if (cosi < 0) {
            return refract(I, -N, eta_i, eta_t)
        }
        val eta = eta_i / eta_t
        val k = 1 - eta * eta * (1 - cosi * cosi)
        return if (k < 0) {
            return Vect3(1F, 0F, 0F)
        } else {
            I.scale(eta) + N.scale(eta * cosi - Math.sqrt(k.toDouble()).toFloat())
        }
    }
}