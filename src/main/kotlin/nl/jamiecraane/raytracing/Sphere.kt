package nl.jamiecraane.raytracing

import nl.jamiecraane.raytracing.material.Material

/**
 * Represents a sphere with a center and a radius. The default color is Color.BLACK.
 */
class Sphere(val center: Vect3, radius: Float, val material: Material) {
    private val diameter = radius * radius
    /**
     * @param dirNormalized Normalized direction vector
     * @return Pair<Boolean, Float> Pair containing a boolean which indicates if the ray intersected with the sphere and a float which is the distance of the sphere to the ray.
     */
    fun rayIntersect(origin: Vect3, dirNormalized: Vect3): Pair<Boolean, Float> {
//        println("center = $center")
//        println("origin = $origin")
        val vOriginToCenter = center - origin
//        println("dir = $dirNormalized")
//        println("L = $vOriginToCenter")
        val scalarOriginDir : Float = vOriginToCenter.dotProduct(dirNormalized)
//        println("tca = $scalarOriginDir")
        val distanceCenterToRay : Float = (vOriginToCenter.dotProduct(vOriginToCenter) - (scalarOriginDir * scalarOriginDir))
//        println("d2 $distanceCenterToRay")
        if (distanceCenterToRay > diameter) return false to 0F
        // Simple Pythagorean (a^2 + b^2 = c^2) to calculate the distance between = a^2 = c^2 - b^2 (c^2 = diameter and b^2 = distanceCenterToRay
        val a : Float = Math.sqrt((diameter - distanceCenterToRay).toDouble()).toFloat()
        var t0 : Float = scalarOriginDir - a
        val t1 : Float = scalarOriginDir + a
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