package nl.jamiecraane.raytracing.renderingsamples

import nl.jamiecraane.raytracing.buildingblocks.Vect3
import nl.jamiecraane.raytracing.scene.scene
import java.util.*

val complexScene = scene {
    val materials = arrayOf(ivory, redRubber, mirror, glass)
    val random = Random()

    light(Vect3(-20F, 20F, 20F)) { intensity = 1.5F }
    light(Vect3(30F, 50F, -25F)) { intensity = 1.8F }
    light(Vect3(30F, 20F, 30F)) { intensity = 1.7F }

    val range = -8..8 step 4

    for (x in range) {
        for (y in range) {
            for (z in 10..24 step 4) {
                sphere(
                    center = Vect3(x.toFloat(), y.toFloat(), -z.toFloat()),
                    material = materials[random.nextInt(materials.size)]
                ) {
                    radius = random.nextFloat() + 1F
                }
            }
        }
    }
}