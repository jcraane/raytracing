package nl.jamiecraane.raytracing.output

import java.awt.Image
import java.awt.Toolkit
import java.awt.image.BufferedImage
import java.awt.image.MemoryImageSource

/**
 * Converts an IntArray to a BufferedImage. This function is only useful in this project
 * since this project most IntArrays are images.
 */
fun IntArray.toBufferedImage(width: Int, height: Int): BufferedImage {
    val source = MemoryImageSource(width, height, this, 0, width)
    val bi = BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR)
    val graphics = bi.createGraphics()
    val image = Toolkit.getDefaultToolkit().createImage(source)
    graphics.drawImage(image, 0, 0, null)
    return bi
}

fun IntArray.toImage(width: Int, height: Int): Image {
    val source = MemoryImageSource(width, height, this, 0, width)
    return Toolkit.getDefaultToolkit().createImage(source)
}
