package nl.jamiecraane.raytracing

import java.awt.Color

class Material(val albedo: Vect3 = Vect3(1F, 0F), val diffuseColor: Color, val specularComponent: Float = 0F)