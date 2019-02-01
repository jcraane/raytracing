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
    val sphere = Sphere(Vect3(-3F, 0F, -16F), 2F)
    render(sphere)
}

private const val width = 1024
private const val height = 768
private val backgroundColor = Color(0.2F, 0.7F, 0.8F)
private const val fov = Math.PI / 3.0

private fun render(sphere: Sphere) {
    val size = width * height
    val pixels = IntArray(size)

//    todo create a parallel render loop
    for (j in 0 until height) {
        for (i in 0 until width) {
            val index = i + j * width
//            val x: Float = ((2 * (i * 0.5F) / width - 1) * Math.tan(fov / 2.0) * width / height).toFloat()
//            val y: Float = ((2 * (j + 0.5F) / height - 1) * Math.tan(fov / 2.0)).toFloat()

            val x: Float = (i + 0.5F) - (width / 2)
            val y: Float = -(j + 0.5F) + (height / 2)
            val z: Float = -height / (2F * Math.tan(fov / 2F)).toFloat()
            val dir = Vect3(x, y, z).normalize()
            pixels[index] = castRay(Vect3(0F, 0F, 0F), dir, sphere).rgb
        }
    }

    writeImageToDisk(pixels, "image.jpg")
}

private fun castRay(orig: Vect3, dir: Vect3, sphere: Sphere): Color {
    return if (!sphere.rayIntersect(orig, dir)) {
        backgroundColor
    } else {
        Color(0.4F, 0.4F, 0.3F)
    }
}

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
