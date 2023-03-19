package creakgl

import org.lwjgl.glfw.GLFW.glfwGetTime

var time = 0f
    get() = glfwGetTime().toFloat()

private var lastTime = 0.0
var deltaTime = 0f
    private set

fun updateTime() {
    val currentTime = glfwGetTime()
    deltaTime = (currentTime - lastTime).toFloat()
    lastTime = currentTime
}