package nl.jamiecraane.raytracing.raytracer

import nl.jamiecraane.raytracing.buildingblocks.Ray
import nl.jamiecraane.raytracing.buildingblocks.Vect3
import java.util.*

enum class RenderMode {
    PLAIN {
        override fun computeRays(i: Int, j: Int, z: Float, orig: Vect3, halfWidth: Float, halfHeight: Float): List<Ray> {
            val x: Float = (i + 0.5F) - halfWidth
            val y: Float = -(j + 0.5F) + halfHeight
            val dir = Vect3(x, y, z).normalize()
            return listOf(orig to dir)
        }
    }, SUPERSAMPLING {
        override fun computeRays(i: Int, j: Int, z: Float, orig: Vect3, halfWidth: Float, halfHeight: Float): List<Ray> {
            val rays = mutableListOf<Ray>()
            val centerX: Float = (i + 0.5F) - halfWidth
            val centerY: Float = -(j + 0.5F) + halfHeight
            rays += orig to Vect3(centerX, centerY, z).normalize()
            rays += orig to Vect3(centerX - 0.5F, centerY - 0.5F, z).normalize()
            rays += orig to Vect3(centerX - 0.5F, centerY + 0.5F, z).normalize()
            rays += orig to Vect3(centerX + 0.5F, centerY - 0.5F, z).normalize()
            rays += orig to Vect3(centerX + 0.5F, centerY + 0.5F, z).normalize()
            return rays
        }
    }, STOCHASTIC {
        override fun computeRays(i: Int, j: Int, z: Float, orig: Vect3, halfWidth: Float, halfHeight: Float): List<Ray> {
            val rays = mutableListOf<Ray>()
            val leftX: Float = (i + 0.5F) - halfWidth
            val topY: Float = -(j + 0.5F) + halfHeight
            for (i in 0 until 9) {
                rays += orig to (Vect3(leftX + random.nextFloat() - 0.5F, topY + random.nextFloat() - 0.5F, z).normalize())
            }
            return rays
        }
    };

    abstract fun computeRays(i: Int, j: Int, z: Float, orig: Vect3, halfWidth: Float, halfHeight: Float) : List<Ray>

    companion object {
        private val random = Random()
    }
}