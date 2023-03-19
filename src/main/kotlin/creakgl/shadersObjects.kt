package creakgl

import org.lwjgl.opengl.GL20.*
import java.io.File

var activeShaderProgram: ShaderProgram? = null

class FragmentShader(source: String) : GLObject() {
    val id = glCreateShader(GL_FRAGMENT_SHADER)

    init {
        glShaderSource(id, source)
        glCompileShader(id)
        if (glGetShaderi(id, GL_COMPILE_STATUS) == GL_FALSE) {
            val len = glGetShaderi(id, GL_INFO_LOG_LENGTH)
            throw Exception("FragmentShader compilation failed: " + glGetShaderInfoLog(id, len))
        }
    }

    companion object {
        fun loadFromFile(filepath: String): FragmentShader {
            val source = File(filepath).readText()
            return FragmentShader(source)
        }
    }
}

class VertexShader(source: String) : GLObject() {
    val id = glCreateShader(GL_VERTEX_SHADER)

    init {
        glShaderSource(id, source)
        glCompileShader(id)
        if (glGetShaderi(id, GL_COMPILE_STATUS) == GL_FALSE) {
            val len = glGetShaderi(id, GL_INFO_LOG_LENGTH)
            throw Exception("VertexShader compilation failed: " + glGetShaderInfoLog(id, len))
        }
    }
    companion object {
        fun loadFromFile(filepath: String): VertexShader {
            val source = File(filepath).readText()
            return VertexShader(source)
        }
    }
}

class ShaderProgram(vertexShader: VertexShader, fragmentShader: FragmentShader) : GLObject() {
    val id = glCreateProgram()

    init {
        glAttachShader(id, vertexShader.id)
        glAttachShader(id, fragmentShader.id)
        glLinkProgram(id)
        if (glGetProgrami(id, GL_LINK_STATUS) == GL_FALSE) {
            val len = glGetProgrami(id, GL_INFO_LOG_LENGTH)
            throw Exception(glGetProgramInfoLog(id, len))
        }
        glDeleteShader(vertexShader.id)
        glDeleteShader(fragmentShader.id)
    }

    private val uniformQueue = mutableMapOf<String, Any>()

    fun setUniform(name: String, value: Any) {
        processUniform(name, value)
    }

    private fun processUniform(name: String, value: Any) {
        if (activeShaderProgram == this)
            uploadUniform(name, value)
        else
            uniformQueue[name] = value
    }

    private fun uploadUniform(name: String, value: Any) {
        val location = glGetUniformLocation(id, name)
        when (value) {
            is Int -> glUniform1i(location, value)
            is Float -> glUniform1f(location, value)
            is Vec2 -> glUniform2f(location, value.x, value.y)
            is Mat4 -> glUniformMatrix4fv(location, false, value.asFloatArray())
            else -> throw Exception("invalid uniform type")
        }
    }

    private fun processUniformQueue() {
        for ((name, value) in uniformQueue) {
            uploadUniform(name, value)
        }
        uniformQueue.clear()
    }

    fun use() {
        glUseProgram(id)
        activeShaderProgram = this
        processUniformQueue()
    }
}