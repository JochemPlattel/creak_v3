package creakgl

import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30.glBindVertexArray
import org.lwjgl.opengl.GL30.glGenVertexArrays

var boundVBO: VBO? = null
var boundVAO: VAO? = null

fun unbindVBO() {
    glBindBuffer(GL_ARRAY_BUFFER, 0)
    boundVBO = null
}

fun unbindVAO() {
    glBindVertexArray(0)
    boundVAO = null
}

fun unbindEBO() {
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)
}

class VAO(): GLObject() {
    val id = glGenVertexArrays()
    var ebo: EBO? = null
        set(value) {
            bind()
            // TODO: bind 0 buffer if null
            if (value == null)
                unbindEBO()
            else
                value.bind()
            field = value
        }
    fun bind() {
        glBindVertexArray(id)
        boundVAO = this
    }

    fun setAttributePointers(vbo: VBO, sizes: IntArray) {
        bind()
        vbo.bind()
        var pointer = 0L
        for (i in sizes.indices) {
            glVertexAttribPointer(
                i,
                sizes[i],
                GL_FLOAT,
                false,
                sizes.sum() * Float.SIZE_BYTES,
                pointer * Float.SIZE_BYTES
            )
            glEnableVertexAttribArray(i)
            pointer += sizes[i]
        }
    }

    fun setAttributePointer(vbo: VBO, index: Int, size: Int, stride: Int, start: Int) {
        bind()
        vbo.bind()
        glVertexAttribPointer(
            index,
            size,
            GL_FLOAT,
            false,
            stride * Float.SIZE_BYTES,
            (start * Float.SIZE_BYTES).toLong()
        )
        glEnableVertexAttribArray(index)
    }

    fun draw(start: Int, count: Int, primitive: DrawPrimitive) {
        bind()
        glDrawArrays(primitive.id, start, count)
    }

    fun drawIndexed(start: Long, count: Int, primitive: DrawPrimitive) {
        // TODO: indices
        bind()
        glDrawElements(primitive.id, count, GL_UNSIGNED_INT, start * Int.SIZE_BYTES)
    }
}

class VBO(): GLObject() {
    val id = glGenBuffers()
    fun createDataStore(capacity: Long, usage: BufferUsage = BufferUsage.STATIC) {
        bind()
        glBufferData(GL_ARRAY_BUFFER, capacity * Float.SIZE_BYTES, usage.id)
    }

    fun createDataStore(floatArray: FloatArray, usage: BufferUsage = BufferUsage.STATIC) {
        bind()
        glBufferData(GL_ARRAY_BUFFER, floatArray, usage.id)
    }

    fun subData(startIndex: Long, data: FloatArray) {
        bind()
        glBufferSubData(GL_ARRAY_BUFFER, startIndex * Float.SIZE_BYTES, data)
    }

    fun bind() {
        glBindBuffer(GL_ARRAY_BUFFER, id)
    }
}
class EBO(): GLObject() {
    val id = glGenBuffers()
    fun createDataStore(capacity: Long, usage: BufferUsage = BufferUsage.STATIC) {
        bind()
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, capacity * Int.SIZE_BYTES, usage.id)
    }

    fun createDataStore(intArray: IntArray, usage: BufferUsage = BufferUsage.STATIC) {
        bind()
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, intArray, usage.id)
    }

    fun subData(startIndex: Long, data: IntArray) {
        bind()
        glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, startIndex * Int.SIZE_BYTES, data)
    }

    fun bind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, id)
    }
}

enum class DrawPrimitive(val id: Int) {
    TRIANGLES(GL_TRIANGLES),
    TRIANGLE_STRIP(GL_TRIANGLE_STRIP),
    TRIANGLE_FAN(GL_TRIANGLE_FAN),
    LINES(GL_LINES),
    LINE_STRIP(GL_LINE_STRIP),
    LINE_LOOP(GL_LINE_LOOP),
    POINTS(GL_POINTS)
}

enum class BufferUsage(val id: Int) {
    STATIC(GL_STATIC_DRAW), DYNAMIC(GL_DYNAMIC_DRAW), STREAM(GL_STREAM_DRAW)
}