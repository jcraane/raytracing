package nl.jamiecraane.raytracing

class Sphere(val center: Vect3, val radius: Float) {
    fun rayIntersect(orig: Vect3, dir: Vect3): Boolean {
        val diameter = radius * radius
        val L = center - orig
        val tca : Float = L.dotProduct(dir)
        val d2 : Float = (L.dotProduct(L) - (tca * tca))
        if (d2 > diameter) return false
        val thc : Float = Math.sqrt((diameter - d2).toDouble()).toFloat()
        var t0 : Float = tca - thc
        val t1 : Float = tca + thc
        if (t0 < 0) {
            t0 = t1
        }
        return if (t0 < 0) {
            false
        } else {
            return true
        }
    }
}