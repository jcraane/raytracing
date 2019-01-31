package nl.jamiecraane.raytracing

data class Vect3(val x: Float, val y: Float, val z: Float = 0F) {
    infix operator fun plus(other: Vect3) = Vect3(x + other.x, y + other.y, z + other.z)

    infix operator fun minus(other: Vect3) = Vect3(x - other.x, y - other.y, z - other.z)

    fun dotProduct(other: Vect3) = (this.x * other.x) + (this.y * other.y) + (this.z * other.z)

    fun crossProduct(other: Vect3): Vect3 {
        val cx = (y * other.z) - (z * other.y)
        val cy = (z * other.x) - (x * other.z)
        val cz = (x * other.y) - (y * other.x)
        return Vect3(cx, cy, cz)
    }

    override infix operator fun equals(other: Any?): Boolean {
        return if (other is Vect3) {
            x == other.x && y == other.y && z == other.z
        } else {
            false
        }
    }
}