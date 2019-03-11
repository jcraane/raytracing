package nl.jamiecraane.raytracing

fun main() {
    val center = Vect3(-3.0F, 0F, -16.0F)
    val origin = Vect3(0.50000006F, 0.75000006F, -10.700962F)
    val vOriginToCenter = center - origin

    // Expected:  L(-3.000500,-0.000750,-4.866451)
    println("L = $vOriginToCenter")
}