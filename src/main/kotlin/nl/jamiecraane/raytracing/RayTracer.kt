package nl.jamiecraane.raytracing

import java.awt.Color

fun main() {
    val ivory = Material(Vect3(0.6F, 0.3F), Color(0.4F, 0.4F, 0.3F), 50F)
    val redRubber = Material(Vect3(0.9F, 0.1F), Color(0.3F, 0.1F, 0.1F), 10F)
    render(
        listOf(
            Sphere(Vect3(-3F, 0F, -16F), 2F, ivory),
            Sphere(Vect3(-1F, -1.5F, -12F), 2F, redRubber),
            Sphere(Vect3(1.5F, -0.5F, -18F), 3F, redRubber),
            Sphere(Vect3(7F, 5F, -18F), 4F, ivory)
        ),
        listOf(
            Light(Vect3(-20F, 20F, 20F), 1.5F),
            Light(Vect3(30F, 50F, -25F), 1.8F),
            Light(Vect3(30F, 20F, 30F), 1.7F)
        )

    )
}

private const val width = 1024
private const val height = 768
private val backgroundColor = Color(0.2F, 0.7F, 0.8F)
private const val fov = Math.PI / 3.0

private fun render(spheres: List<Sphere>, lights: List<Light>) {
    val size = width * height
    val pixels = IntArray(size)

//    todo create a parallel render loop
    val start = System.currentTimeMillis()
    for (j in 0 until height) {
        for (i in 0 until width) {
            val index = i + j * width
            val x: Float = (i + 0.5F) - (width / 2)
            val y: Float = -(j + 0.5F) + (height / 2)
            val z: Float = -height / (2F * Math.tan(fov / 2F)).toFloat()
            val dir = Vect3(x, y, z).normalize()
            val orig = Vect3(0F, 0F, 0F)
            pixels[index] = castRay(orig, dir, spheres, lights).rgb
        }
    }
    val end = System.currentTimeMillis() - start
    println("$end")

    RayTracedImage(width, height, pixels).writeImageToFile("image.jpg")
}

private fun castRay(orig: Vect3, dir: Vect3, spheres: List<Sphere>, lights: List<Light>): Color {
    val result = sceneIntersect(orig, dir, spheres)
    return if (!result.intersect) {
        backgroundColor
    } else {
        var diffuseLightIntensity = 0F
        var specularLightIntensity = 0F
        for (light in lights) {
            if (result.hit != null && result.N != null) {
                val lightDir = (light.position - result.hit).normalize()
                diffuseLightIntensity += light.intensity * Math.max(0F, lightDir.dotProduct(result.N))
                specularLightIntensity += (Math.pow(
                    Math.max(
                        0F,
                        reflect(-lightDir, result.N).dotProduct(dir)
                    ).toDouble(), result.material.specularComponent.toDouble()
                ) * light.intensity).toFloat()
            }
        }

        val colors = result.material.diffuseColor.getColorComponents(FloatArray(4))
        val r = colors[0] * diffuseLightIntensity * result.material.albedo.x + specularLightIntensity * result.material.albedo.y
        val g = colors[1] * diffuseLightIntensity * result.material.albedo.x + specularLightIntensity * result.material.albedo.y
        val b = colors[2] * diffuseLightIntensity * result.material.albedo.x + specularLightIntensity * result.material.albedo.y
        Color(Math.min(1F, r), Math.min(1F, g), Math.min(1F, b))
    }
}

private fun reflect(I: Vect3, N: Vect3) : Vect3{
    return I - (N.scale(2F).crossProduct(I.crossProduct(N)))
}

private fun sceneIntersect(orig: Vect3, dir: Vect3, spheres: List<Sphere>): IntersectResult {
    var sphereDist = Float.MAX_VALUE
    var material = Material(diffuseColor =  Color.BLACK, specularComponent = 0F)
    var hit: Vect3? = null
    var N: Vect3? = null
    for (sphere in spheres) {
        val (intersect, distance) = sphere.rayIntersect(orig, dir)
        if (intersect && distance < sphereDist) {
            sphereDist = distance
            hit = orig + dir.scale(distance)
            N = (hit - sphere.center).normalize()
            material = sphere.material
        }
    }

    return IntersectResult(hit, N, material, sphereDist < 1000)
}

class IntersectResult(val hit: Vect3? = null, val N: Vect3? = null, val material: Material, val intersect: Boolean)

