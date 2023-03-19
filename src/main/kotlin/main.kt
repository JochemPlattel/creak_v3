import creakgl.*
import org.lwjgl.opengl.GL11.*

val vao = VAO()
val vbo = VBO()
val ebo = EBO()
val camera = Camera3D(1f, 0f, 10f, 1f).apply { rotation.x = .3f }

val vs = VertexShader("""
    #version 400
    layout (location = 0) in vec3 aPos;
    uniform mat4 projection;
    
    void main() {
        gl_Position = projection * vec4(aPos, 1);
    }
""".trimIndent())

val fs = FragmentShader("""
    #version 400
    out vec4 color;
    void main() {
        color = vec4(.5, .5, 1, 1);
    }
""".trimIndent())

val shader = ShaderProgram(vs, fs)

fun main() {
    Window.open(800, 800, "Hello Creak!")
    glPolygonMode(GL_FRONT_AND_BACK, GL_LINE)
    val (vertices, indices) = createGrid(10, 10)
    vbo.createDataStore(vertices)
    ebo.createDataStore(indices)
    println(vertices.size)
    println(indices.size)
    vao.ebo = ebo
    vao.setAttributePointers(vbo, intArrayOf(3))
    shader.use()
    Window.startUpdateLoop {
        shader.setUniform("projection", camera.projectionView())
        updatePlayer()
        vao.drawIndexed(0, indices.size, DrawPrimitive.TRIANGLES)
    }
}

fun createGrid(width: Int, height: Int): Pair<FloatArray, IntArray> {
    val vertices = mutableListOf<Float>()
    val indices = mutableListOf<Int>()
    var scale = 10
    for (i in 0..height) {
        for (j in 0..width) {
            val x = j
            val z = i
            vertices.add(x.toFloat())
            vertices.add(noise(x.toDouble() / scale, 0.0, z.toDouble() / scale).toFloat() * 20)
            vertices.add(z.toFloat())
        }
    }
    for (i in 0 until height) {
        for (j in 0 until width) {
            indices.add(i * (width + 1) + j)
            indices.add(i * (width + 1) + j + 1)
            indices.add((i + 1) * (width + 1) + j + 1)

            indices.add(i * (width + 1) + j)
            indices.add((i + 1) * (width + 1) + j + 1)
            indices.add((i + 1) * (width + 1) + j)
        }
    }
    return vertices.toFloatArray() to indices.toIntArray()
}

val playerSpeed = 500f

fun updatePlayer() {
    if (Input.isKeyDown(Key.A))
        camera.position.x -= playerSpeed * deltaTime
    if (Input.isKeyDown(Key.D))
        camera.position.x += playerSpeed * deltaTime
    if (Input.isKeyDown(Key.S))
        camera.position.z -= playerSpeed * deltaTime
    if (Input.isKeyDown(Key.W))
        camera.position.z += playerSpeed * deltaTime
    if (Input.isKeyDown(Key.SPACE))
        camera.position.y += playerSpeed * deltaTime
    if (Input.isKeyDown(Key.LEFT_SHIFT))
        camera.position.y -= playerSpeed * deltaTime
}