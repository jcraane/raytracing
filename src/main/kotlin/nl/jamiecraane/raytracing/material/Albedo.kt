package nl.jamiecraane.raytracing.material

/**
 * Albedo: the proportion of the incident light or radiation that is reflected by a surface.
 */
data class Albedo(val a: Float, val b: Float = 0F, val c: Float = 0F, val d: Float = 0F)