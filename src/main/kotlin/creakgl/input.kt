package creakgl

import org.lwjgl.BufferUtils
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWImage
import org.lwjgl.stb.STBImage.stbi_load
import org.lwjgl.stb.STBImage.stbi_set_flip_vertically_on_load

object Input: GLObject() {
    fun isKeyDown(key: Key): Boolean {
        return glfwGetKey(windowID, key.id) == GLFW_PRESS
    }
    var cursorMode = CursorMode.NORMAL
        set(value) {
            glfwSetInputMode(windowID, GLFW_CURSOR, value.id)
            field = value
        }
    fun setCursor(filepath: String) {
        val widthArray = intArrayOf(0)
        val heightArray = intArrayOf(0)
        val numChannelsArray = intArrayOf(0)
        val imageData = stbi_load(filepath, widthArray, heightArray, numChannelsArray, 0)
        val glfwImage = GLFWImage.malloc()
        glfwImage.set(widthArray[0], heightArray[0], imageData!!)
        val cursor = glfwCreateCursor(glfwImage, 0, 0)
        glfwSetCursor(windowID, cursor)
    }
    fun setWindowIcon(filePath: String) {
        val spriteWidth = BufferUtils.createIntBuffer(1)
        val spriteHeight = BufferUtils.createIntBuffer(1)
        val channels = BufferUtils.createIntBuffer(1)
        stbi_set_flip_vertically_on_load(false)
        val image = stbi_load(filePath, spriteWidth, spriteHeight, channels, 4)
        stbi_set_flip_vertically_on_load(true)
        val imageWidth = spriteWidth.get(0)
        val imageHeight = spriteHeight.get(0)
        val glfwImage = GLFWImage.malloc()
        val buffer = GLFWImage.malloc(1)
        glfwImage.set(imageWidth, imageHeight, image!!)
        buffer.put(0, glfwImage)
        glfwSetWindowIcon(windowID, buffer)
    }
}

fun loadGLFWImage(filepath: String) {
    val width = intArrayOf(0)
    val height = intArrayOf(0)
    val channels = intArrayOf(0)
    stbi_set_flip_vertically_on_load(false)
    val imageData = stbi_load(filepath, width, height, channels, 4)
    stbi_set_flip_vertically_on_load(true)
    if (imageData != null) {
        val glfwImage = GLFWImage.malloc()
        val buffer = GLFWImage.malloc(1)
        glfwImage.set(width[0], height[0], imageData)
        buffer.put(0, glfwImage)
    }
}

enum class CursorMode(val id: Int) {
    HIDDEN(GLFW_CURSOR_HIDDEN), DISABLED(GLFW_CURSOR_DISABLED), NORMAL(GLFW_CURSOR_NORMAL)
}

enum class Key(val id: Int) {
    A(GLFW_KEY_A),
    B(GLFW_KEY_B),
    C(GLFW_KEY_C),
    D(GLFW_KEY_D),
    E(GLFW_KEY_E),
    F(GLFW_KEY_F),
    G(GLFW_KEY_G),
    H(GLFW_KEY_H),
    I(GLFW_KEY_I),
    J(GLFW_KEY_J),
    K(GLFW_KEY_K),
    L(GLFW_KEY_L),
    M(GLFW_KEY_M),
    N(GLFW_KEY_N),
    O(GLFW_KEY_O),
    P(GLFW_KEY_P),
    Q(GLFW_KEY_Q),
    R(GLFW_KEY_R),
    S(GLFW_KEY_S),
    T(GLFW_KEY_T),
    U(GLFW_KEY_U),
    V(GLFW_KEY_V),
    W(GLFW_KEY_W),
    X(GLFW_KEY_X),
    Y(GLFW_KEY_Y),
    Z(GLFW_KEY_Z),
    RIGHT(GLFW_KEY_RIGHT),
    LEFT(GLFW_KEY_LEFT),
    UP(GLFW_KEY_UP),
    DOWN(GLFW_KEY_DOWN),
    SPACE(GLFW_KEY_SPACE),
    BACKSPACE(GLFW_KEY_BACKSPACE),
    LEFT_SHIFT(GLFW_KEY_LEFT_SHIFT),
    RIGHT_SHIFT(GLFW_KEY_LEFT_SHIFT),
    ESCAPE(GLFW_KEY_ESCAPE),
    ENTER(GLFW_KEY_ENTER),
    LEFT_ALT(GLFW_KEY_LEFT_ALT),
    RIGHT_ALT(GLFW_KEY_RIGHT_ALT),
    LEFT_CTRL(GLFW_KEY_LEFT_CONTROL),
    RIGHT_CTRL(GLFW_KEY_RIGHT_CONTROL)
}

enum class MouseButton(val code: Int) {
    LEFT(GLFW_MOUSE_BUTTON_LEFT),
    RIGHT(GLFW_MOUSE_BUTTON_RIGHT),
    MIDDLE(GLFW_MOUSE_BUTTON_MIDDLE),
    EXTRA1(GLFW_MOUSE_BUTTON_4),
    EXTRA2(GLFW_MOUSE_BUTTON_5),
    EXTRA3(GLFW_MOUSE_BUTTON_6),
    EXTRA4(GLFW_MOUSE_BUTTON_7),
    EXTRA5(GLFW_MOUSE_BUTTON_8),
}