package nl.jamiecraane.raytracing.output

import java.awt.Image
import java.awt.Toolkit
import java.awt.image.BufferedImage
import java.awt.image.MemoryImageSource

class RawImage(val pixels: IntArray, val width: Int, val height: Int) {
    val bufferedImage: BufferedImage
    get() {
        val source = MemoryImageSource(width, height, pixels, 0, width)
        val bi = BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR)
        val graphics = bi.createGraphics()
        val image = Toolkit.getDefaultToolkit().createImage(source)
        graphics.drawImage(image, 0, 0, null)
        return bi
    }

    val image: Image
    get() {
        val source = MemoryImageSource(width, height, pixels, 0, width)
        return Toolkit.getDefaultToolkit().createImage(source)
    }
}