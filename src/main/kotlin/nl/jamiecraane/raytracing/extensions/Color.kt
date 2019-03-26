package nl.jamiecraane.raytracing.extensions

import java.awt.Color

operator fun Color.component1() = getColorComponents(FloatArray(4))[0]
operator fun Color.component2() = getColorComponents(FloatArray(4))[1]
operator fun Color.component3() = getColorComponents(FloatArray(4))[2]
operator fun Color.component4() = getColorComponents(FloatArray(4))[3]

fun averageColors(colors: List<Color>): Color {
    return (colors
        .map { color ->
            val (r, g, b) = color
            RGB(r, g, b)
        }
        .reduce { acc, color ->
            acc + color
        } / colors.size.toFloat()).toColor()
}

/**
 * Holder for rgb values to manipulate them.
 */
data class RGB(val r: Float, val g: Float, val b: Float) {
    infix operator fun plus(other: RGB): RGB {
        val (r, g, b) = this
        val (ro, go, bo) = other
        return RGB(r + ro, g + go, b + bo)
    }

    infix operator fun div(fraction: Float): RGB {
        val (r, g, b) = this
        return RGB(r / fraction, g / fraction, b / fraction)
    }

    fun toColor() = Color(r, g, b)
}