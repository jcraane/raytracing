package nl.jamiecraane.raytracing

import java.awt.Color

fun main() {
    val sphere = Sphere(Vect3(4F, 4F), 1F, Material(Color.BLACK))
    val orig = Vect3.originVector()
    val dir = Vect3(2F, 2F)
//    println(sphere.rayIntersect(orig, dir.normalize()))
    println(sphere.rayIntersect(orig, Vect3(3F, 1F).normalize()))

    println(Vect3(3F, 1F).crossProduct(Vect3(4F, 4F)))
    println(Vect3(3F, 1F).normalize())
}