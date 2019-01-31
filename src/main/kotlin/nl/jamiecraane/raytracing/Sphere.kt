package nl.jamiecraane.raytracing

class Sphere(val center: Vect3, val radius: Float) {
    fun rayIntersect(orig: Vect3, dir: Vect3, t0: Float): Boolean {
        val diameter = radius * radius
        val L = center - orig
//        val tca = L * dir
//        val d2 = L * L - tca * tca
//        if (d2 > diameter) return false

        return true
    }
}