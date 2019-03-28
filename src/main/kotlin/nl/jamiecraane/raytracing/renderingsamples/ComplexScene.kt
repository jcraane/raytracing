package nl.jamiecraane.raytracing.renderingsamples

import nl.jamiecraane.raytracing.buildingblocks.Vect3
import nl.jamiecraane.raytracing.scene.scene
import java.util.*

val complexScene = scene {
    val materials = arrayOf(ivory, redRubber, mirror, glass)
    val random = Random()

    light(Vect3(-20.0, 20.0, 20.0)) { intensity = 1.5 }
    light(Vect3(30.0, 50.0, -25.0)) { intensity = 1.8 }
    light(Vect3(30.0, 20.0, 30.0)) { intensity = 1.7 }

    val range = -8..8 step 4

    for (x in range) {
        for (y in range) {
            if (y != 0 || x != 0) {
                for (z in 10..24 step 4) {
                    sphere(
                        center = Vect3(x.toDouble(), y.toDouble(), -z.toDouble()),
                        material = materials[random.nextInt(materials.size)]
                    ) {
                        radius = random.nextFloat() + 1.0
                    }
                }
            }
        }
    }
}