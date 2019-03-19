package nl.jamiecraane.raytracing.lights

import nl.jamiecraane.raytracing.buildingblocks.Vect3
import nl.jamiecraane.raytracing.scene.SceneDsl

@SceneDsl
data class Light(val position: Vect3) {
    var intensity: Float = 1F
}