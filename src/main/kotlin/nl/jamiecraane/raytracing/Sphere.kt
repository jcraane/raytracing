package nl.jamiecraane.raytracing

import nl.jamiecraane.raytracing.material.Material

/**
 * Represents a sphere with a center and a radius. The default color is Color.BLACK.
 */
class Sphere(val center: Vect3, radius: Float, val material: Material) {
    private val diameter = radius * radius
    /**
     * @see https://www.scratchapixel.com/lessons/3d-basic-rendering/minimal-ray-tracer-rendering-simple-shapes/ray-sphere-intersection
     * @param dirNormalized Normalized direction vector
     * @return Pair<Boolean, Float> Pair containing a boolean which indicates if the ray intersected with the sphere and a float which is the distance of the sphere to the ray.
     */
    fun rayIntersect(origin: Vect3, dirNormalized: Vect3): Pair<Boolean, Float> {
        val L = center - origin
        val tca : Float = L.dotProduct(dirNormalized)
        if (tca < 0) {
            return false to 0F
        }
        // (a^2 + b^2 = c^2)
        val c2 = L.dotProduct(L)
        val b2 = tca * tca
        // d = a^2
        val d = Math.sqrt((c2 - b2).toDouble())
        val thc = Math.sqrt((diameter - (d*d)).toDouble()).toFloat()
        if (thc > diameter) return false to 0F
        var t0 : Float = tca - thc
        val t1 : Float = tca + thc
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
