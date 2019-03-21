package nl.jamiecraane.raytracing

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import nl.jamiecraane.raytracing.buildingblocks.Vect3
import nl.jamiecraane.raytracing.extensions.component1
import nl.jamiecraane.raytracing.extensions.component2
import nl.jamiecraane.raytracing.extensions.component3
import nl.jamiecraane.raytracing.lights.*
import nl.jamiecraane.raytracing.material.Material
import nl.jamiecraane.raytracing.objects.Sphere
import nl.jamiecraane.raytracing.output.ImageCanvas
import nl.jamiecraane.raytracing.output.RawImage
import nl.jamiecraane.raytracing.renderingsamples.simpleScene
import nl.jamiecraane.raytracing.scene.Scene
import nl.jamiecraane.raytracing.util.BoundedList
import nl.jamiecraane.raytracing.util.StopWatch
import java.awt.Color
import java.util.*
import javax.swing.JFrame

//todo create soft shadows, perhaps using stochastic ray tracing.
fun main() {
    renderStaticImage(simpleScene, true)
//    renderStaticImage(complexScene, false)
//    animationTest(ivory)
}

private fun renderStaticImage(scene: Scene, renderCheckerBoard: Boolean = true) {
    val pixels = render(
        scene.getSpheres(), scene.getLights(), renderCheckerBoard
    )

    val imageCanvas = createJFrame()
    imageCanvas.image = RawImage(pixels, width, height).image
//    RayTracedImage(width, height, RawImage(pixels, width, height)).writeImageToFile("image.jpg")
}

private fun createJFrame(): ImageCanvas {
    val frame = JFrame("RayTracer")
    frame.setSize(width, height)
    val imageCanvas = ImageCanvas()
    frame.add(imageCanvas)
    frame.isVisible = true
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    return imageCanvas
}

// Convenience for now. Replace global data structure with proper encapsulation.
private const val width = 1024
private const val height = 768
private val backgroundColor = Color(0.2F, 0.7F, 0.8F)
private const val fov = Math.PI / 3.0
private const val recursionDepth = 4
private val hitPoints = BoundedList<Vect3>(10, mutableListOf())
private val whatToRender = EnumSet.of(
    WhatToRender.FLAT,
    WhatToRender.DIFFUSE,
    WhatToRender.SHADOWS,
    WhatToRender.SPECULAR,
    WhatToRender.REFLECTION,
    WhatToRender.REFRACTION
)

private fun render(
    spheres: List<Sphere>,
    lights: List<Light>,
    renderCheckerBoard: Boolean
): IntArray {
    val size = width * height
    val pixels = IntArray(size)

//    todo visualize rays. Should be drawn in 3d.

    val dispatcher = Dispatchers.Default
    val executionTime = StopWatch.timeIt {
        runBlocking {
            for (j in 0 until height) {
                for (i in 0 until width) {
                    launch(dispatcher) {
                        val index = i + j * width
                        val x: Float = (i + 0.5F) - (width / 2F)
                        val y: Float = -(j + 0.5F) + (height / 2F)
                        val z: Float = -height / (2F * Math.tan(fov / 2F)).toFloat()
                        val dir = Vect3(x, y, z).normalize()
                        val orig = Vect3(0F, 0F, 0F)
                        pixels[index] = castRay(orig, dir, spheres, lights, 0, renderCheckerBoard).rgb
                    }
                }
            }
        }
    }
    println(executionTime.toMillis())
    println("hitPoints = ${hitPoints}")

//todo trace rays from orig to hitpoint. Hoe mappen we deze rays op de juiste pixels?.
//  Use a second pass for interect testing with the rays we want to visualize.


    return pixels
}

private fun averageColors(colors: List<Color>): Color {
    var rtotal = 0F
    var gtotal = 0F
    var btotal = 0F
    colors.forEach { c ->
        val (r, g, b) = c
        rtotal += r
        gtotal += g
        btotal += b
    }

    return Color(rtotal / colors.size.toFloat(), gtotal / colors.size.toFloat(), btotal / colors.size.toFloat())
}

private fun castRay(
    orig: Vect3,
    dir: Vect3,
    spheres: List<Sphere>,
    lights: List<Light>,
    depth: Int,
    renderCheckerBoard: Boolean
): Color {
    val result = sceneIntersect(orig, dir, spheres, renderCheckerBoard)

    return if (depth > recursionDepth || !result.intersect) {
        backgroundColor
    } else {
        if (result.hit != null && result.normalVector != null) {
            hitPoints.add(result.hit)
            val reflectDir = Reflector.reflect(dir, result.normalVector).normalize()
            val reflectOrig = if (reflectDir.dotProduct(result.normalVector) < 0) {
                result.hit - result.normalVector.scale(0.001F)
            } else {
                result.hit + result.normalVector.scale(0.001F)
            }
            val reflectColor = castRay(reflectOrig, reflectDir, spheres, lights, depth + 1, renderCheckerBoard)

            val refractDir = Refractor.refract(dir, result.normalVector, result.material.refractiveIndex).normalize()
            val refractOrig = if (refractDir.dotProduct(result.normalVector) < 0) {
                result.hit - result.normalVector.scale(0.001F)
            } else {
                result.hit + result.normalVector.scale(0.001F)
            }
            val refractColor = castRay(refractOrig, refractDir, spheres, lights, depth + 1, renderCheckerBoard)

            var diffuseLightIntensity = 0F
            var specularLightIntensity = 0F
            for (light in lights) {
                val lightDir = (light.position - result.hit).normalize()

                val shadowOrigin = isPointInShadowOfLights(lightDir, result.normalVector, result.hit)
                val shadowResult = sceneIntersect(shadowOrigin, lightDir, spheres, renderCheckerBoard)
                if (!shadowResult.intersect || !whatToRender.contains(WhatToRender.SHADOWS)) {
                    diffuseLightIntensity += DiffuseLightReflector.calculateIntensity(
                        lightDir, result.normalVector, light
                    )
                    specularLightIntensity += SpecularLightReflector.calculateIntensity(
                        lightDir, result.normalVector, dir, light, result.material
                    )
                }
            }

            return result.material.applyLightIntensity(
                diffuseLightIntensity,
                specularLightIntensity,
                reflectColor,
                refractColor,
                whatToRender
            )
        } else {
            return backgroundColor
        }
    }
}

private fun isPointInShadowOfLights(
    lightDir: Vect3,
    normalVector: Vect3,
    hit: Vect3
): Vect3 {
    return if (lightDir.dotProduct(normalVector) < 0) {
        hit - normalVector.scale(0.001F)
    } else {
        hit + normalVector.scale(0.001F)
    }
}

private fun sceneIntersect(
    orig: Vect3,
    dir: Vect3,
    spheres: List<Sphere>,
    renderCheckerBoard: Boolean
): IntersectResult {
    var sphereDist = Float.MAX_VALUE
    var material = Material(diffuseColor = Color.BLACK)
    material.specularComponent = 0F
    var hitPoint: Vect3? = null
    var normalVector: Vect3? = null
    for (sphere in spheres) {
        val (intersect, distance) = sphere.rayIntersect(orig, dir)
        if (intersect && distance < sphereDist) {
            sphereDist = distance
            hitPoint = orig + dir.scale(distance)
            normalVector = (hitPoint - sphere.center).normalize()
            material = sphere.material
        }
    }

    var checkedBoardDist = Float.MAX_VALUE
    if (renderCheckerBoard) {
        if (Math.abs(dir.y) > 0.001F) {
            val d = -(orig.y + 4) / dir.y // the checkerboard plane has equation y = -4
            val pt = orig + dir.scale(d)
            if (d > 0 && Math.abs(pt.x) < 12 && pt.z < -10 && pt.z > -30 && d < sphereDist) {
                checkedBoardDist = d
                hitPoint = pt
                normalVector = Vect3(0F, 1F, 0F)
                val i = ((.5F * hitPoint.x + 1000).toInt() + (.5F * hitPoint.z).toInt()) and 1
                val diffuseColor = if (i == 1) {
                    Color(.3F, .3F, .3F)
                } else {
                    Color(.3F, .2F, .1F)
                }
                material = Material(diffuseColor)
            }
        }
    }

    return IntersectResult(hitPoint, normalVector, material, Math.min(sphereDist, checkedBoardDist) < 1000)
}

class IntersectResult(
    val hit: Vect3? = null,
    val normalVector: Vect3? = null,
    val material: Material,
    val intersect: Boolean
)

