package nl.jamiecraane.raytracing

import nl.jamiecraane.raytracing.buildingblocks.Vect3
import org.junit.Assert.*
import org.junit.Test

class Vect3Test {
    @Test
    fun add() {
        assertEquals(
            Vect3(10.0, 14.0, 7.0), Vect3(
                2.0,
                4.0,
                6.0
            ) + Vect3(8.0, 10.0, 1.0)
        )
    }

    @Test
    fun subtract() {
        assertEquals(
            Vect3(-6.0, -6.0, 5.0), Vect3(
                2.0,
                4.0,
                6.0
            ) - Vect3(8.0, 10.0, 1.0)
        )
    }

    @Test
    fun dotProduct() {
        assertEquals(44.0, Vect3(
            6.0,
            8.0
        ).dotProduct(Vect3(2.0, 4.0)))
        assertEquals(74.0, Vect3(
            6.0,
            8.0,
            3.0
        ).dotProduct(Vect3(2.0, 4.0, 10.0)))
        assertEquals(13.2, Vect3(
            -3.0,
            0.0,
            -16.0
        ).dotProduct(Vect3(-0.544575, 0.418946, -0.726582)), 0.1)
    }

    @Test
    fun equalsTest() {
        assertTrue(Vect3(2.0, 3.0) == Vect3(2.0, 3.0))
        assertTrue(
            Vect3(2.0, 3.0, 5.0) == Vect3(
                2.0,
                3.0,
                5.0
            )
        )
        assertTrue(Vect3(2.0, 3.0) != Vect3(3.0, 3.0))
        assertTrue(
            Vect3(2.0, 3.0, 5.0) != Vect3(
                2.0,
                3.0,
                6.0
            )
        )
    }

    @Test
    fun crossProduct() {
        val v1 = Vect3(20.0, 30.0, 40.0)
        val v2 = Vect3(50.0, 60.0, 70.0)
        assertEquals(Vect3(-30.0, 60.0, -30.0), v1.crossProduct(v2))
    }

    @Test
    fun scale() {
        assertEquals(
            Vect3(4.0, 8.0, 16.0), Vect3(
                2.0,
                4.0,
                8.0
            ).scale(2.0))
    }

    @Test
    fun magnitude() {
        assertEquals(10.0, Vect3(6.0, 8.0).magnitude(), 0.1)
        assertEquals(10.7, Vect3(6.0, 8.0, 4.0).magnitude(), 0.1)
    }

    @Test
    fun normalize() {
        val n = Vect3(3.0, 5.0, 2.0).normalize()
        assertEquals(0.48, n.x, 0.01)
        assertEquals(0.81, n.y, 0.01)
        assertEquals(0.32, n.z, 0.01)
    }

    @Test
    fun angle() {
        assertEquals(85.2, Vect3(
            6.0,
            -2.0,
            -3.0
        ).angleBetween(Vect3(1.0, 1.0, 1.0)), 0.1)
    }

    @Test
    fun isOrthogonal() {
        assertTrue(
            Vect3(2.0, 4.0).isOrthogonalTo(
                Vect3(
                    4.0,
                    -2.0
                )
            ))
        assertFalse(
            Vect3(
                2.0,
                4.0
            ).isOrthogonalTo(Vect3(1.0, -2.0)))
    }
}