package nl.jamiecraane.raytracing

import com.sun.image.codec.jpeg.JPEGCodec
import org.apache.commons.io.FileUtils
import java.awt.Color
import java.awt.Toolkit
import java.awt.image.BufferedImage
import java.awt.image.MemoryImageSource
import java.io.ByteArrayOutputStream
import java.io.File

fun main() {
    val ivory = Material(Color(1F, 0F, 0F))
    val redRubber = Material(Color(0F, 1F, 0F))
    render(
        listOf(
            Sphere(Vect3(-3F, 0F, -16F), 2F, ivory),
            Sphere(Vect3(-1F, -1.5F, -12F), 2F, redRubber),
            Sphere(Vect3(1.5F, -0.5F, -18F), 3F, redRubber),
            Sphere(Vect3(7F, 5F, -18F), 4F, ivory)
        ),
        listOf(Light(Vect3(-20F, 20F, 20F), 1.5F))
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

    writeImageToDisk(pixels, "image.jpg")
}

private fun castRay(orig: Vect3, dir: Vect3, spheres: List<Sphere>, lights: List<Light>): Color {
    val result = sceneIntersect(orig, dir, spheres)
    return if (!result.intersect) {
        backgroundColor
    } else {
        var diffuseLightIntensity = 0F
        for (light in lights) {
            if (result.hit != null && result.N != null) {
                val lightDir = (light.position - result.hit).normalize()
                diffuseLightIntensity += light.intensity * Math.max(0F, lightDir.dotProduct(result.N))
            }
        }
        val r = Math.min(255F, result.material.diffuseColor.red * diffuseLightIntensity)
        val g = Math.min(255F, result.material.diffuseColor.green * diffuseLightIntensity)
        val b = Math.min(255F, result.material.diffuseColor.blue * diffuseLightIntensity)
        Color(r.toInt(), g.toInt(), b.toInt())
    }
}

private fun sceneIntersect(orig: Vect3, dir: Vect3, spheres: List<Sphere>): IntersectResult {
    var sphereDist = Float.MAX_VALUE
    var material = Material(Color.BLACK)
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

//todo minimize JPEG compression
private fun writeImageToDisk(pixels: IntArray, fileName: String) {
    val source = MemoryImageSource(width, height, pixels, 0, width)
    val bi = BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR)
    val graphics = bi.createGraphics()
    val image = Toolkit.getDefaultToolkit().createImage(source)
    graphics.drawImage(image, 0, 0, null)

    ByteArrayOutputStream().use {
        val encoder = JPEGCodec.createJPEGEncoder(it)
        encoder.encode(bi)
        FileUtils.writeByteArrayToFile(File(fileName), it.toByteArray())
    }
}
