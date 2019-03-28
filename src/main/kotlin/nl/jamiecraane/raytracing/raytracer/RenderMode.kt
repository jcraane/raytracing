package nl.jamiecraane.raytracing.raytracer

import nl.jamiecraane.raytracing.buildingblocks.Ray
import nl.jamiecraane.raytracing.buildingblocks.Vect3
import java.util.*

enum class RenderMode {
    PLAIN {
        override fun computeRays(i: Int, j: Int, z: Double, orig: Vect3, halfWidth: Double, halfHeight: Double): List<Ray> {
            val x: Double = (i + 0.5) - halfWidth
            val y: Double = -(j + 0.5) + halfHeight
            val dir = Vect3(x, y, z).normalize()
            return listOf(orig to dir)
        }
    },
    SUPERSAMPLING {
        override fun computeRays(i: Int, j: Int, z: Double, orig: Vect3, halfWidth: Double, halfHeight: Double): List<Ray> {
            val rays = mutableListOf<Ray>()
            val centerX: Double = (i + 0.5) - halfWidth
            val centerY: Double = -(j + 0.5) + halfHeight
            rays += orig to Vect3(centerX, centerY, z).normalize()
            rays += orig to Vect3(centerX - 0.5, centerY - 0.5, z).normalize()
            rays += orig to Vect3(centerX - 0.5, centerY + 0.5, z).normalize()
            rays += orig to Vect3(centerX + 0.5, centerY - 0.5, z).normalize()
            rays += orig to Vect3(centerX + 0.5, centerY + 0.5, z).normalize()
            return rays
        }
    },
    STOCHASTIC {
        override fun computeRays(i: Int, j: Int, z: Double, orig: Vect3, halfWidth: Double, halfHeight: Double): List<Ray> {
            val rays = mutableListOf<Ray>()
            val leftX: Double = (i + 0.5) - halfWidth
            val topY: Double = -(j + 0.5) + halfHeight
            for (i in 0 until 9) {
                rays += orig to (Vect3(leftX + random.nextFloat() - 0.5, topY + random.nextFloat() - 0.5, z).normalize())
            }
            return rays
        }
    };

    abstract fun computeRays(i: Int, j: Int, z: Double, orig: Vect3, halfWidth: Double, halfHeight: Double): List<Ray>

    companion object {
        private val random = Random()
    }
}