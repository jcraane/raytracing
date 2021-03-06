package nl.jamiecraane.raytracing.scene

import nl.jamiecraane.raytracing.buildingblocks.Vect3
import nl.jamiecraane.raytracing.lights.Light
import nl.jamiecraane.raytracing.material.Material
import nl.jamiecraane.raytracing.objects.Sphere
import java.awt.Color

@DslMarker
annotation class SceneDsl

/**
 * Kotlin builder for building scenes.
 */
@SceneDsl
class Scene {
    private val spheres = mutableListOf<Sphere>()
    private val lights = mutableListOf<Light>()

    fun sphere(center: Vect3, material: Material, init: Sphere.() -> Unit) {
        val sphere = Sphere(center, material)
        sphere.init()
        spheres.add(sphere)
    }

    fun light(position: Vect3, init: Light.() -> Unit) {
        val light = Light(position)
        light.init()
        lights.add(light)
    }

    fun getSpheres() = spheres.toList()
    fun getLights() = lights.toList()

    override fun toString(): String {
        return "Scene(spheres=$spheres, lights=$lights)"
    }
}

@SceneDsl
fun scene(init: Scene.() -> Unit): Scene {
    val scene = Scene()
    scene.init()
    return scene
}

/**
 * Materials can be created outside of the scene scope.
 */
fun material(diffuseColor: Color, init: Material.() -> Unit): Material {
    val material = Material(diffuseColor)
    material.init()
    return material
}