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

    fun scale(scalar: Float) = Vect3(x * scalar, y * scalar, z * scalar)

    /**
     * Calculates the length (magnitude) of the vector.
     */
    fun magnitude() = Math.sqrt(
        Math.pow(x.toDouble(), 2.0) +
                Math.pow(y.toDouble(), 2.0) +
                Math.pow(z.toDouble(), 2.0)
    )

    fun normalize(): Vect3 {
        val m = magnitude()
        return Vect3((x / m).toFloat(), (y / m).toFloat(), (z / m).toFloat())
    }

    override infix operator fun equals(other: Any?): Boolean {
        return if (other is Vect3) {
            x == other.x && y == other.y && z == other.z
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        return result
    }
}