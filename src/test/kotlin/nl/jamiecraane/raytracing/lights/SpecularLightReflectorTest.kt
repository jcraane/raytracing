package nl.jamiecraane.raytracing.lights

import nl.jamiecraane.raytracing.buildingblocks.Vect3
import org.junit.Assert
import org.junit.Test

class SpecularLightReflectorTest {
    @Test
    fun reflection() {
        val dir = Vect3(0.337570012, 0.434018552, -0.835269093)
        val N = Vect3(-0.178841934, 0.770059525, 0.612392008)
        val reflected = Reflector.reflect(dir, N)
        Assert.assertEquals(0.252, reflected.x, 0.001)
        Assert.assertEquals(0.800, reflected.y, 0.001)
        Assert.assertEquals(-0.544, reflected.z, 0.001)
    }
}