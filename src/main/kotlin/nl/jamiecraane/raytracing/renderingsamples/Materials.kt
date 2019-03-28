package nl.jamiecraane.raytracing.renderingsamples

import nl.jamiecraane.raytracing.material.Albedo
import nl.jamiecraane.raytracing.scene.material
import java.awt.Color

val ivory = material(Color(0.4F, 0.4F, 0.3F)) {
    albedo = Albedo(0.6F, 0.3F, 0.1F)
    specularComponent = 50.0
}

val redRubber = material(Color(0.3F, 0.1F, 0.1F)) {
    albedo = Albedo(0.9F, 0.1F)
    specularComponent = 10.0
}

val mirror = material(Color(1.0F, 1.0F, 1.0F)) {
    albedo = Albedo(0.0F, 10.0F, 0.8F)
    specularComponent = 1425.0
}

val glass = material(Color(0.6F, 0.7F, 0.8F)) {
    albedo = Albedo(0.0F, 0.5F, 0.1F, 0.8F)
    specularComponent = 125.0
    refractiveIndex = 1.5
}