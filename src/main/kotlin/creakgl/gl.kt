package creakgl

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL.createCapabilities
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GLUtil
import org.lwjgl.stb.STBImage.stbi_set_flip_vertically_on_load
import java.io.File
import java.io.PrintStream
import kotlin.reflect.KType

internal var GLInit = false
internal var windowID = 0L
fun assertGL() {
    if (!GLInit) {
        initGL()
        GLInit = true
    }
}
fun initGL() {
    glfwInit()
    glfwDefaultWindowHints()
    glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE)
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
    windowID = glfwCreateWindow(1, 1, "", 0L, 0L)
    glfwMakeContextCurrent(windowID)
    //glfwShowWindow(id)
    //opengl setup
    glfwSwapInterval(0)
    createCapabilities()
    val log = PrintStream(File("src/main/resources/log.txt"))
    GLUtil.setupDebugMessageCallback(log)
    stbi_set_flip_vertically_on_load(true)
}

fun openWindow() {
    glfwShowWindow(windowID)
}

fun <T> T.update(block: T.() -> Unit): T {
    updateFunctions.add {
        block()
    }
    return this
}

val updateFunctions = mutableListOf<() -> Unit>()

fun updateContinuous() {
    updateFunctions.forEach {
        it()
    }
}

object Window {
    fun open(width: Int, height: Int, title: String) {
        assertGL()
        glfwShowWindow(windowID)
        glfwSetWindowSize(windowID, width, height)
        glViewport(0, 0, width, height)
        glfwSetWindowTitle(windowID, title)
    }
    fun startUpdateLoop(updateFunction: () -> Unit) {
        while (!glfwWindowShouldClose(windowID)) {
            glfwPollEvents()
            glClear(GL_COLOR_BUFFER_BIT)
            updateTime()
            updateFunction()
            updateContinuous()
            glfwSwapBuffers(windowID)
        }
    }
}

abstract class GLObject {
    init {
        assertGL()
    }
}