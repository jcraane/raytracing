package nl.jamiecraane.raytracing.raytracer

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import nl.jamiecraane.raytracing.WhatToRender
import nl.jamiecraane.raytracing.buildingblocks.Vect3
import nl.jamiecraane.raytracing.extensions.averageColors
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
private const val recursionDepth = 4
private val renderMode = RenderMode.PLAIN
private val hitPoints = BoundedList<Vect3>(10, mutableListOf())
private val whatToRender = EnumSet.of(
    WhatToRender.FLAT,
    WhatToRender.DIFFUSE,
    WhatToRender.SHADOWS,
    WhatToRender.SPECULAR,
    WhatToRender.REFLECTION,
    WhatToRender.REFRACTION
)

private const val halfWidth = width / 2.0
private const val halfHeight = height / 2.0
private val backgroundColor = Color(0.2F, 0.7F, 0.8F)
private const val fov = Math.PI / 3.0

private fun render(
    spheres: List<Sphere>,
    lights: List<Light>,
    renderCheckerBoard: Boolean
): IntArray {
    val size = width * height
    val pixels = IntArray(size)
    val orig = Vect3(0.0, 0.0, 0.0)
    val fovAngle = 2.0 * Math.tan(fov / 2.0)

    val dispatcher = Dispatchers.Default
    val executionTime = StopWatch.timeIt {
        runBlocking {
            for (j in 0 until height) {
                for (i in 0 until width) {
                    launch(dispatcher) {
                        val index = i + j * width
                        val z: Double = -height / fovAngle
                        val colors =
                            renderMode.computeRays(i, j, z, orig, halfWidth, halfHeight)
                                .map { ray ->
                                    castRay(orig, ray.direction, spheres, lights, 0, renderCheckerBoard)
                                }
                        pixels[index] = averageColors(colors).rgb
                    }
                }
            }
        }
    }
    println(executionTime.toMillis())

//todo trace rays from orig to hitpoint. Hoe mappen we deze rays op de juiste pixels?.
//  Use a second pass for interect testing with the rays we want to visualize.
    return pixels
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
                result.hit - result.normalVector.scale(0.001)
            } else {
                result.hit + result.normalVector.scale(0.001)
            }
            val reflectColor = castRay(reflectOrig, reflectDir, spheres, lights, depth + 1, renderCheckerBoard)

            val refractDir = Refractor.refract(dir, result.normalVector, result.material.refractiveIndex).normalize()
            val refractOrig = if (refractDir.dotProduct(result.normalVector) < 0) {
                result.hit - result.normalVector.scale(0.001)
            } else {
                result.hit + result.normalVector.scale(0.001)
            }
            val refractColor = castRay(refractOrig, refractDir, spheres, lights, depth + 1, renderCheckerBoard)

            var diffuseLightIntensity = 0.0
            var specularLightIntensity = 0.0
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
        hit - normalVector.scale(0.001)
    } else {
        hit + normalVector.scale(0.001)
    }
}

private fun sceneIntersect(
    orig: Vect3,
    dir: Vect3,
    spheres: List<Sphere>,
    renderCheckerBoard: Boolean
): IntersectResult {
    var sphereDist = Double.MAX_VALUE
    var material = Material(diffuseColor = Color.BLACK)
    material.specularComponent = 0.0
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

    var checkedBoardDist = Double.MAX_VALUE
    if (renderCheckerBoard) {
        if (Math.abs(dir.y) > 0.001) {
            val d = -(orig.y + 4) / dir.y // the checkerboard plane has equation y = -4
            val pt = orig + dir.scale(d)
            if (d > 0 && Math.abs(pt.x) < 12 && pt.z < -10 && pt.z > -30 && d < sphereDist) {
                checkedBoardDist = d
                hitPoint = pt
                normalVector = Vect3(0.0, 1.0, 0.0)
                val i = ((.5 * hitPoint.x + 1000).toInt() + (.5 * hitPoint.z).toInt()) and 1
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

