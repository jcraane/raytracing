package nl.jamiecraane.raytracing

import nl.jamiecraane.raytracing.lights.SpecularLightReflector

fun main() {
    /*val sphere = Sphere(Vect3(4F, 4F), 1F, Material(diffuseColor =  Color.BLACK))
    val orig = Vect3(2F, 1.5F)
    val dir = Vect3(2F, 2F)

    println(sphere.rayIntersect(orig, dir.normalize()))

    println(Vect3(3F, 1F).crossProduct(Vect3(4F, 4F)))
    println(Vect3(3F, 1F).normalize())*/

    // Vector projection
    /*val u = Vect3(3F, 3F)
    val v = Vect3(1F, 3F)
    val vnorm = v.normalize()
    println("v normalized = ${vnorm.magnitude()}")
    val dp = u.dotProduct(vnorm)
    println("dt product v normalized = $dp")
    val puv = vnorm.scale(dp)
    println("Vector puv = $puv")
    println("Lenght of puv = ${puv.magnitude()}")*/


//    todo this is not correct yet. See where the problem is.
    val dir = Vect3(0.337570012F, 0.434018552F, -0.835269093F)
    val N = Vect3(-0.178841934F, 0.770059525F, 0.612392008F)
    val reflected = SpecularLightReflector.reflect(dir, N)
    println(reflected)
}