package nl.jamiecraane.raytracing.output

import com.sun.imageio.plugins.jpeg.JPEGImageWriter
import java.awt.image.BufferedImage
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
        val bufferedImage = pixels.toBufferedImage(width, height)

        ImageIO.createImageOutputStream(FileOutputStream(File(fileName))).use {
            writeImageToDisk(imageWriter, it, bufferedImage)
        }
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