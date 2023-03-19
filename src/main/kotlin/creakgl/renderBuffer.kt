package creakgl

// TODO: initializing ebo can cause other ebo to be unbound
class RenderBuffer(val attributeSizes: IntArray) {
    val vao = VAO()
    val vbo = VBO()
    val ebo = EBO()

    val vertices = mutableListOf<Float>()
    val indices = mutableListOf<Int>()

    init {
        vao.ebo = ebo
    }

    fun addVertices(vertices: FloatArray) {

    }
    fun addIndices() {

    }
    fun addLocal() {

    }
    fun upload() {

    }
}