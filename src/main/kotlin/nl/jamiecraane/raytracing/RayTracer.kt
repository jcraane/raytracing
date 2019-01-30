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
    render()
}

private const val width = 1024
private const val height = 768

private fun render() {
    val size = width * height
    val pixels = IntArray(size)
    for (j in 0 until height) {
        for (i in 0 until width) {
            val index = i + j * width
            pixels[index] = Color(j.toFloat() / height, i.toFloat() / width, 0F).rgb
        }
    }

    writeImageToDisk(pixels, "image.jpg")
}

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
