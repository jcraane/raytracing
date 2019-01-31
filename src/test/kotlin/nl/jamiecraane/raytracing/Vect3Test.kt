package nl.jamiecraane.raytracing

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class Vect3Test {

    @Test
    fun dotProduct() {
        assertEquals(44F, Vect3(6F, 8F).dotProduct(Vect3(2F, 4F)))
        assertEquals(74F, Vect3(6F, 8F, 3F).dotProduct(Vect3(2F, 4F, 10F)))
    }

    @Test
    fun equalsTest() {
        assertTrue(Vect3(2F, 3F) == Vect3(2F, 3F))
        assertTrue(Vect3(2F, 3F, 5F) == Vect3(2F, 3F, 5F))
        assertTrue(Vect3(2F, 3F) != Vect3(3F, 3F))
        assertTrue(Vect3(2F, 3F, 5F) != Vect3(2F, 3F, 6F))
    }

    @Test
    fun crossProduct() {
        val v1 = Vect3(2F, 3F, 4F)
        val v2 = Vect3(5F, 6F, 7F)
        assertEquals(Vect3(-3F, 6F, -3F), v1.crossProduct(v2))
    }
}