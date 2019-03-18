package nl.jamiecraane.raytracing.renderingsamples

import nl.jamiecraane.raytracing.buildingblocks.Vect3
import nl.jamiecraane.raytracing.scene.scene

val simpleScene = scene {
    sphere(center = Vect3(-3F, 0F, -16F), material = ivory) { radius = 2F }
    sphere(center = Vect3(-1F, -1.5F, -12F), material = glass) { radius = 2F }
    sphere(center = Vect3(1.5F, -0.5F, -18F), material = redRubber) { radius = 3F }
    sphere(center = Vect3(7F, 5F, -18F), material = mirror) { radius = 4F }

    light(Vect3(-20F, 20F, 20F)) { intensity = 1.5F }
    light(Vect3(30F, 50F, -25F)) { intensity = 1.8F }
    light(Vect3(30F, 20F, 30F)) { intensity = 1.7F }
}