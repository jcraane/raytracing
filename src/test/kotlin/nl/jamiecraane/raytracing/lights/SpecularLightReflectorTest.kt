package nl.jamiecraane.raytracing.lights

import nl.jamiecraane.raytracing.Vect3
import org.junit.Assert
import org.junit.Test

class SpecularLightReflectorTest {
    @Test
    fun reflection() {
        val dir = Vect3(0.337570012F, 0.434018552F, -0.835269093F)
        val N = Vect3(-0.178841934F, 0.770059525F, 0.612392008F)
        val reflected = Reflector.reflect(dir, N)
        Assert.assertEquals(0.252F, reflected.x, 0.001F)
        Assert.assertEquals(0.800F, reflected.y, 0.001F)
        Assert.assertEquals(-0.544F, reflected.z, 0.001F)
    }
}