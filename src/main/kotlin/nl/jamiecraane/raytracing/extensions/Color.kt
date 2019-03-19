package nl.jamiecraane.raytracing.extensions

import java.awt.Color

operator fun Color.component1() = getColorComponents(FloatArray(4))[0]
operator fun Color.component2() = getColorComponents(FloatArray(4))[1]
operator fun Color.component3() = getColorComponents(FloatArray(4))[2]
operator fun Color.component4() = getColorComponents(FloatArray(4))[3]