package nl.jamiecraane.raytracing.lights

import nl.jamiecraane.raytracing.scene.SceneDsl
import nl.jamiecraane.raytracing.buildingblocks.Vect3

@SceneDsl
class Light(val position: Vect3) {
    var intensity: Float = 1F
}