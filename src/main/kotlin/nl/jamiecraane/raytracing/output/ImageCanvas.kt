package nl.jamiecraane.raytracing.output

import java.awt.Graphics2D
import java.awt.Image
import java.awt.geom.AffineTransform

class ImageCanvas() : BlankCanvas() {
    var image: Image? = null

    override fun doPaint(graphics2D: Graphics2D, oldTransform: AffineTransform) {
        image?.let { graphics2D.drawImage(it, 0, 0, null) }
    }
}