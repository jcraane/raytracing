package nl.jamiecraane.raytracing

import nl.jamiecraane.raytracing.buildingblocks.Vect3
import org.junit.Assert.*
import org.junit.Test

class Vect3Test {
    @Test
    fun add() {
        assertEquals(
            Vect3(10F, 14F, 7F), Vect3(
                2F,
                4F,
                6F
            ) + Vect3(8F, 10F, 1F)
        )
    }

    @Test
    fun subtract() {
        assertEquals(
            Vect3(-6F, -6F, 5F), Vect3(
                2F,
                4F,
                6F
            ) - Vect3(8F, 10F, 1F)
        )
    }

    @Test
    fun dotProduct() {
        assertEquals(44F, Vect3(
            6F,
            8F
        ).dotProduct(Vect3(2F, 4F)))
        assertEquals(74F, Vect3(
            6F,
            8F,
            3F
        ).dotProduct(Vect3(2F, 4F, 10F)))
        assertEquals(13.2F, Vect3(
            -3F,
            0F,
            -16F
        ).dotProduct(Vect3(-0.544575F, 0.418946F, -0.726582F)), 0.1F)
    }

    @Test
    fun equalsTest() {
        assertTrue(Vect3(2F, 3F) == Vect3(2F, 3F))
        assertTrue(
            Vect3(2F, 3F, 5F) == Vect3(
                2F,
                3F,
                5F
            )
        )
        assertTrue(Vect3(2F, 3F) != Vect3(3F, 3F))
        assertTrue(
            Vect3(2F, 3F, 5F) != Vect3(
                2F,
                3F,
                6F
            )
        )
    }

    @Test
    fun crossProduct() {
        val v1 = Vect3(2F, 3F, 4F)
        val v2 = Vect3(5F, 6F, 7F)
        assertEquals(Vect3(-3F, 6F, -3F), v1.crossProduct(v2))
    }

    @Test
    fun scale() {
        assertEquals(
            Vect3(4F, 8F, 16F), Vect3(
                2F,
                4F,
                8F
            ).scale(2F))
    }

    @Test
    fun magnitude() {
        assertEquals(10.0, Vect3(6F, 8F).magnitude(), 0.1)
        assertEquals(10.7, Vect3(6F, 8F, 4F).magnitude(), 0.1)
    }

    @Test
    fun normalize() {
        val n = Vect3(3F, 5F, 2F).normalize()
        assertEquals(0.48F, n.x, 0.01F)
        assertEquals(0.81F, n.y, 0.01F)
        assertEquals(0.32F, n.z, 0.01F)
    }

    @Test
    fun angle() {
        assertEquals(85.2F, Vect3(
            6F,
            -2F,
            -3F
        ).angleBetween(Vect3(1F, 1F, 1F)), 0.1F)
    }

    @Test
    fun isOrthogonal() {
        assertTrue(
            Vect3(2F, 4F).isOrthogonalTo(
                Vect3(
                    4F,
                    -2F
                )
            ))
        assertFalse(
            Vect3(
                2F,
                4F
            ).isOrthogonalTo(Vect3(1F, -2F)))
    }
}