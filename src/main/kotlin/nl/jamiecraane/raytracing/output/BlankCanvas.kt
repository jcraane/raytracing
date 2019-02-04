package nl.jamiecraane.raytracing.output

import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.geom.AffineTransform
import javax.swing.JPanel

abstract class BlankCanvas : JPanel() {
    override fun paint(g: Graphics?) {
        val graphics2D = g as Graphics2D
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        doPaint(graphics2D, g.transform)
    }

    abstract fun doPaint(graphics2D: Graphics2D, oldTransform: AffineTransform)
}
