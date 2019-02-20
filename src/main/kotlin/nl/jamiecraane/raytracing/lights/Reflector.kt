package nl.jamiecraane.raytracing.lights

import nl.jamiecraane.raytracing.Vect3

object Reflector {
//    todo we can re-use this for specular light but the input parameters for specular light are not correct yet.
    fun reflect(I: Vect3, N: Vect3): Vect3 {
        return I - (N.scale(2F).scale(I.dotProduct(N)))
    }
}