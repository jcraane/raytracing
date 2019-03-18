package nl.jamiecraane.raytracing.lights

import nl.jamiecraane.raytracing.buildingblocks.Vect3

object Reflector {
    fun reflect(I: Vect3, N: Vect3): Vect3 {
        return I - (N.scale(2F).scale(I.dotProduct(N)))
    }
}