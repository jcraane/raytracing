package nl.jamiecraane.raytracing

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import nl.jamiecraane.raytracing.buildingblocks.Vect3
import nl.jamiecraane.raytracing.lights.*
import nl.jamiecraane.raytracing.material.Material
import nl.jamiecraane.raytracing.objects.Sphere
import nl.jamiecraane.raytracing.output.ImageCanvas
import nl.jamiecraane.raytracing.output.RawImage
import nl.jamiecraane.raytracing.renderingsamples.simpleScene
import nl.jamiecraane.raytracing.scene.Scene
import nl.jamiecraane.raytracing.scene.scene
import nl.jamiecraane.raytracing.util.StopWatch
import java.awt.Color
import javax.swing.JFrame

fun main() {
    renderStaticImage(simpleScene)
//    animationTest(ivory)
}

private fun createComplexScene() = scene {

}

//todo create an interesting scene with lots of objects.
private fun renderStaticImage(scene: Scene) {
    val pixels = render(
        scene.getSpheres(), scene.getLights()
    )

    val imageCanvas = createJFrame()
    imageCanvas.image = RawImage(pixels, width, height).image
//    RayTracedImage(width, height, RawImage(pixels, width, height)).writeImageToFile("image.jpg")
}

private fun animationTest(ivory: Material) {
    val imageCanvas = createJFrame()

    /*var lightX = -50F
    while (lightX < 60F) {
        val pixels = render(
            listOf(
                Sphere(Vect3(-3F, 0F, -16F), 2F, ivory),
                Sphere(Vect3(7F, 5F, -18F), 4F, ivory)
            ),
            listOf(
                Light(Vect3(-lightX, 20F, 20F), 1.5F),
                Light(Vect3(30F, 50F, -25F), 1.8F)*//*,
                Light(Vect3(30F, 20F, 30F), 1.7F)*//*
            )
        )
        imageCanvas.image = RawImage(pixels, width, height).image
        lightX += 1F
    }*/
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

private const val width = 1024
private const val height = 768
private val backgroundColor = Color(0.2F, 0.7F, 0.8F)
private const val fov = Math.PI / 3.0
private const val recursionDepth = 4
// Convenience for now. Replace global data structure with proper encapsulation.

private fun render(spheres: List<Sphere>, lights: List<Light>): IntArray {
    val size = width * height
    val pixels = IntArray(size)

//    todo visualize rays. Should be drawn in 3d.

    val dispatcher = Dispatchers.Default
    runBlocking {
        val executionTime = StopWatch.timeIt {
            for (j in 0 until height) {
                for (i in 0 until width) {
                    GlobalScope.launch(dispatcher) {
                        val index = i + j * width
                        val x: Float = (i + 0.5F) - (width / 2F)
                        val y: Float = -(j + 0.5F) + (height / 2F)
                        val z: Float = -height / (2F * Math.tan(fov / 2F)).toFloat()
                        val dir = Vect3(x, y, z).normalize()
                        val orig = Vect3(0F, 0F, 0F)
                        pixels[index] = castRay(orig, dir, spheres, lights, 0).rgb
                    }
                }
            }
        }

        println(executionTime.toMillis())
    }

    return pixels
}

private fun castRay(orig: Vect3, dir: Vect3, spheres: List<Sphere>, lights: List<Light>, depth: Int): Color {
    val result = sceneIntersect(orig, dir, spheres)

    return if (depth > recursionDepth || !result.intersect) {
        backgroundColor
    } else {
        if (result.hit != null && result.normalVector != null) {
            val reflectDir = Reflector.reflect(dir, result.normalVector).normalize()
            val reflectOrig = if (reflectDir.dotProduct(result.normalVector) < 0) {
                result.hit - result.normalVector.scale(0.001F)
            } else {
                result.hit + result.normalVector.scale(0.001F)
            }
            val reflectColor = castRay(reflectOrig, reflectDir, spheres, lights, depth + 1)

            val refractDir = Refractor.refract(dir, result.normalVector, result.material.refractiveIndex).normalize()
            val refractOrig = if (refractDir.dotProduct(result.normalVector) < 0) {
                result.hit - result.normalVector.scale(0.001F)
            } else {
                result.hit + result.normalVector.scale(0.001F)
            }
            val refractColor = castRay(refractOrig, refractDir, spheres, lights, depth + 1)

            var diffuseLightIntensity = 0F
            var specularLightIntensity = 0F
            for (light in lights) {
                val lightDir = (light.position - result.hit).normalize()

                val shadowOrigin = isPointInShadowOfLights(lightDir, result.normalVector, result.hit)
                val shadowResult = sceneIntersect(shadowOrigin, lightDir, spheres)
                if (!shadowResult.intersect) {
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
                refractColor
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

private fun sceneIntersect(orig: Vect3, dir: Vect3, spheres: List<Sphere>): IntersectResult {
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

//    todo add checkerboard.

    return IntersectResult(hitPoint, normalVector, material, sphereDist < 1000)
}

class IntersectResult(
    val hit: Vect3? = null,
    val normalVector: Vect3? = null,
    val material: Material,
    val intersect: Boolean
)

