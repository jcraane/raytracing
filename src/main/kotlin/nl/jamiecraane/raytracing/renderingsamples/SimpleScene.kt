package nl.jamiecraane.raytracing.renderingsamples

import nl.jamiecraane.raytracing.buildingblocks.Vect3
import nl.jamiecraane.raytracing.scene.scene

val simpleScene = scene {
    sphere(center = Vect3(-3.0, 0.5, -16.0), material = ivory) { radius = 2.0 }
    sphere(center = Vect3(-1.0, -1.5, -12.0), material = glass) { radius = 2.0 }
    sphere(center = Vect3(1.5, -0.5, -18.0), material = redRubber) { radius = 3.0 }
    sphere(center = Vect3(7.0, 5.0, -18.0), material = mirror) { radius = 4.0 }
//    sphere(center = Vect3(4.0, -1.5.0, -13.0), material = mirror) { radius = 3.0 }

    light(Vect3(-20.0, 20.0, 20.0)) { intensity = 1.5 }
    light(Vect3(30.0, 50.0, -25.0)) { intensity = 1.8 }
    light(Vect3(30.0, 20.0, 30.0)) { intensity = 1.7 }
}