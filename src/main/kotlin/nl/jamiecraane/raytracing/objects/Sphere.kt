package nl.jamiecraane.raytracing.objects

import nl.jamiecraane.raytracing.buildingblocks.Vect3
import nl.jamiecraane.raytracing.material.Material
import nl.jamiecraane.raytracing.scene.SceneDsl

/**
 * Represents a sphere with a center and a radius. The default color is Color.BLACK.
 */
@SceneDsl
data class Sphere(val center: Vect3, val material: Material) {
    var radius = 1.0
    set(value) {
        field = value
        diameter = field * field
    }
    private var diameter = radius * radius
    /**
     * @see https://www.scratchapixel.com/lessons/3d-basic-rendering/minimal-ray-tracer-rendering-simple-shapes/ray-sphere-intersection
     * @param dirNormalized Normalized direction vector
     * @return Pair<Boolean, Double> Pair containing a boolean which indicates if the ray intersected with the sphere and a float which is the distance of the sphere to the ray.
     */
    fun rayIntersect(origin: Vect3, dirNormalized: Vect3): Pair<Boolean, Double> {
        val L = center - origin
        val tca : Double = L.dotProduct(dirNormalized)
        if (tca < 0) {
            return false to 0.0
        }
        // (a^2 + b^2 = c^2)
        val c2 = L.dotProduct(L)
        val b2 = tca * tca
        // d = a^2
        val d = Math.sqrt((c2 - b2).toDouble())
        val thc = Math.sqrt((diameter - (d*d)))
        if (thc > diameter) return false to 0.0
        var t0 : Double = tca - thc
        val t1 : Double = tca + thc
        if (t0 < 0) {
            t0 = t1
        }
        return if (t0 < 0) {
            false to t0
        } else {
            true to t0
        }
    }
}
