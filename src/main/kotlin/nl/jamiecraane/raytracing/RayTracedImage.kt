package nl.jamiecraane.raytracing

import com.sun.imageio.plugins.jpeg.JPEGImageWriter
import java.awt.Toolkit
import java.awt.image.BufferedImage
import java.awt.image.MemoryImageSource
import java.io.File
import java.io.FileOutputStream
import javax.imageio.IIOImage
import javax.imageio.ImageIO
import javax.imageio.ImageTypeSpecifier
import javax.imageio.plugins.jpeg.JPEGImageWriteParam
import javax.imageio.stream.ImageOutputStream

class RayTracedImage(private val width: Int, private val height: Int, private val pixels: IntArray) {
    fun writeImageToFile(fileName: String) {
        val imageWriter = ImageIO.getImageWritersByFormatName("jpg").next() as (JPEGImageWriter)
        val bufferedImage = drawPixelsToImage()

        ImageIO.createImageOutputStream(FileOutputStream(File(fileName))).use {
            writeImageToDisk(imageWriter, it, bufferedImage)
        }
    }

    private fun drawPixelsToImage(): BufferedImage {
        val source = MemoryImageSource(width, height, pixels, 0, width)
        val bi = BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR)
        val graphics = bi.createGraphics()
        val image = Toolkit.getDefaultToolkit().createImage(source)
        graphics.drawImage(image, 0, 0, null)
        return bi
    }

    private fun writeImageToDisk(
        imageWriter: JPEGImageWriter,
        it: ImageOutputStream?,
        bi: BufferedImage
    ) {
        imageWriter.output = it
        val imageMetaData = imageWriter.getDefaultImageMetadata(ImageTypeSpecifier(bi), null)
        imageWriter.write(imageMetaData, IIOImage(bi, null, null), getHighestCompressionQualityParams(imageWriter))
        imageWriter.dispose()
    }

    private fun getHighestCompressionQualityParams(imageWriter: JPEGImageWriter): JPEGImageWriteParam {
        val jpegParams = imageWriter.defaultWriteParam as JPEGImageWriteParam
        jpegParams.compressionMode = JPEGImageWriteParam.MODE_EXPLICIT
        val hightestCompressionQuality = 1F
        jpegParams.compressionQuality = hightestCompressionQuality
        return jpegParams
    }

}