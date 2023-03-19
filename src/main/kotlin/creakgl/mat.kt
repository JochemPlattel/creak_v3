package creakgl

import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

class Mat4(
    e11: Float, e12: Float, e13: Float, e14: Float,
    e21: Float, e22: Float, e23: Float, e24: Float,
    e31: Float, e32: Float, e33: Float, e34: Float,
    e41: Float, e42: Float, e43: Float, e44: Float
) {
    val entries = arrayOf(
        floatArrayOf(e11, e21, e31, e41),
        floatArrayOf(e12, e22, e32, e42),
        floatArrayOf(e13, e23, e33, e43),
        floatArrayOf(e14, e24, e34, e44)
    )

    fun asFloatArray() = floatArrayOf(
        this[1, 1], this[2, 1], this[3, 1], this[4, 1],
        this[1, 2], this[2, 2], this[3, 2], this[4, 2],
        this[1, 3], this[2, 3], this[3, 3], this[4, 3],
        this[1, 4], this[2, 4], this[3, 4], this[4, 4]
    )

    operator fun get(row: Int, column: Int): Float {
        return entries[column - 1][row - 1]
    }

    operator fun set(row: Int, column: Int, value: Float) {
        entries[column - 1][row - 1] = value
    }

    operator fun times(right: Mat4): Mat4 {
        val new = zeroes()
        for (i in 1..4) {
            for (j in 1..4) {
                var sum = 0f
                for (k in 1..4) {
                    sum += this[i, k] * right[k, j]
                }
                new[i, j] = sum
            }
        }
        return new
    }

    fun copy() = Mat4(
        this[1, 1], this[1, 2], this[1, 3], this[1, 4],
        this[2, 1], this[2, 2], this[2, 3], this[2, 4],
        this[3, 1], this[3, 2], this[3, 3], this[3, 4],
        this[4, 1], this[4, 2], this[4, 3], this[4, 4]
    )

    operator fun plus(other: Mat4): Mat4 {
        val new = copy()
        for (i in 1..4) {
            for (j in 1..4) {
                new[i, j] += other[i, j]
            }
        }
        return new
    }

    operator fun times(scalar: Float): Mat4 {
        val new = copy()
        for (i in 1..4) {
            for (j in 1..4) {
                new[i, j] *= scalar
            }
        }
        return new
    }

    operator fun times(vec4: Vec4) = Vec4(
        this[1, 1] * vec4.x + this[1, 2] * vec4.y + this[1, 3] * vec4.z + this[1, 4] * vec4.w,
        this[2, 1] * vec4.x + this[2, 2] * vec4.y + this[2, 3] * vec4.z + this[2, 4] * vec4.w,
        this[3, 1] * vec4.x + this[3, 2] * vec4.y + this[3, 3] * vec4.z + this[3, 4] * vec4.w,
        this[4, 1] * vec4.x + this[4, 2] * vec4.y + this[4, 3] * vec4.z + this[4, 4] * vec4.w
    )

    fun scale(x: Float, y: Float, z: Float) = this * scaling(x, y, z)

    fun rotate(radians: Float) = this * rotation(radians)

    fun translate(x: Float, y: Float, z: Float) = this * translation(x, y, z)

    fun transposed(): Mat4 {
        val new = zeroes()
        for (i in 1..4) {
            for (j in 1..4) {
                new[i, j] = this[j, i]
            }
        }
        return new
    }

    override fun toString(): String {
        return """
            [${this[1, 1]} ${this[1, 2]} ${this[1, 3]} ${this[1, 4]}]
            [${this[2, 1]} ${this[2, 2]} ${this[2, 3]} ${this[2, 4]}]
            [${this[3, 1]} ${this[3, 2]} ${this[3, 3]} ${this[3, 4]}]
            [${this[4, 1]} ${this[4, 2]} ${this[4, 3]} ${this[4, 4]}]
        """.trimIndent()
    }

    companion object {
        fun zeroes() = Mat4(
            0f, 0f, 0f, 0f,
            0f, 0f, 0f, 0f,
            0f, 0f, 0f, 0f,
            0f, 0f, 0f, 0f
        )

        fun identity() = Mat4(
            1f, 0f, 0f, 0f,
            0f, 1f, 0f, 0f,
            0f, 0f, 1f, 0f,
            0f, 0f, 0f, 1f
        )

        fun scaling(x: Float, y: Float, z: Float) = Mat4(
            x, 0f, 0f, 0f,
            0f, y, 0f, 0f,
            0f, 0f, z, 0f,
            0f, 0f, 0f, 1f
        )

        fun rotation(radians: Float): Mat4 {
            val cos = cos(radians)
            val sin = sin(radians)
            return Mat4(
                cos, -sin, 0f, 0f,
                sin, cos, 0f, 0f,
                0f, 0f, 1f, 0f,
                0f, 0f, 0f, 1f
            )
        }

        fun rotation(rad: Float, axis: Axis): Mat4 {
            val cos = cos(rad)
            val sin = sin(rad)
            return when(axis) {
                Axis.X -> Mat4(
                    1f, 0f, 0f, 0f,
                    0f, cos, -sin, 0f,
                    0f, sin, cos, 0f,
                    0f, 0f, 0f, 1f
                )
                Axis.Y -> Mat4(
                    cos, 0f, sin, 0f,
                    0f, 1f, 0f, 0f,
                    -sin, 0f, cos, 0f,
                    0f, 0f, 0f, 1f
                )
                Axis.Z -> Mat4(
                    cos, -sin, 0f, 0f,
                    sin, cos, 0f, 0f,
                    0f, 0f, 1f, 0f,
                    0f, 0f, 0f, 1f
                )
            }
        }

        fun rotation(x: Float, y: Float, z: Float) = rotation(x, Axis.X) * rotation(y, Axis.Y) * rotation(z, Axis.Z)

        fun translation(x: Float, y: Float, z: Float) = Mat4(
            1f, 0f, 0f, x,
            0f, 1f, 0f, y,
            0f, 0f, 1f, z,
            0f, 0f, 0f, 1f
        )

        fun perspective(aspectRatio: Float, fovRadians: Float, far: Float, near: Float): Mat4 {
            val s = 1 / tan(fovRadians / 2)
            return Mat4(
                s / aspectRatio, 0f, 0f, 0f,
                0f, s, 0f, 0f,
                0f, 0f, (far + near) / (far - near), 1f,
                0f, 0f, 2 * far * near / (near - far), 0f
            )
        }
        fun perspectiveNEW(aspectRatio: Float, fovRadians: Float, far: Float, near: Float): Mat4 {
            val t = 1 / tan(fovRadians / 2)
            return Mat4(
                t / aspectRatio, 0f, 0f, 0f,
                0f, t, 0f, 0f,
                0f, 0f, (far + near) / (far - near), 2 * far * near / (near - far),
                0f, 0f, 1f, 0f
            )
        }
    }
}

enum class Axis {
    X, Y, Z
}