package nl.jamiecraane.raytracing

fun main() {
//    val one = Vect3(7F, 3F)
//    val sphere = Sphere(Vect3(10F, 6F), 3F, Material(Albedo(0.6F, 0.3F, 0.1F), Color(0.4F, 0.4F, 0.3F), 50F))
//    val intersect = sphere.rayIntersect(Vect3.originVector(), one.normalize())
//    println(intersect)

    println(Vect3(1F, 1F).normalize())
    println(Vect3(1F, 1F).normalize().dotProduct(Vect3(7F, 3F)))
    println((Vect3(7F, 3F).dotProduct(Vect3(1F, 1F))))

    val D = Vect3(7F, 3F)
    val dNormalized = D.normalize()
    val C = Vect3(10F, 6F)
    val O = Vect3.originVector()
    val L = C - O
    println("L = $L")

    val tca = L.dotProduct(dNormalized)
    println("tca = $tca")


    /*println("L.dotProduct(L) = ${L.dotProduct(L)}")
    println("Lengt L ^ 2 = ${L.magnitude() * L.magnitude()}") * /

    println(Vect3(7F, 3F).dotProduct(Vect3(7F, 3F)))
    println(Vect3(7F, 3F).magnitude() * (Vect3(7F, 3F).magnitude()))*/


}