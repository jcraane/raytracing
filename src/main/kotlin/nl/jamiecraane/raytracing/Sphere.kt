package nl.jamiecraane.raytracing

/**
 * Represents a sphere with a center and a radius. The default color is Color.BLACK.
 */
class Sphere(val center: Vect3, private val radius: Float, val material: Material) {
    /**
     * @return Pair<Boolean, Float> Pair containing a boolean which indicates if the ray intersected with the sphere and a float which is the distance of the sphere to the ray.
     */
    fun rayIntersect(orig: Vect3, dir: Vect3): Pair<Boolean, Float> {
        val diameter = radius * radius
        val L = center - orig
        val tca : Float = L.dotProduct(dir)
        val d2 : Float = (L.dotProduct(L) - (tca * tca))
        if (d2 > diameter) return false to 0F
        val thc : Float = Math.sqrt((diameter - d2).toDouble()).toFloat()
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