package creakgl

import kotlin.math.*

class Vec2(var x: Float = 0f, var y: Float = 0f) {
    val sqrMagnitude
        get() = x * x + y * y
    val magnitude
        get() = sqrt(x * x + y * y)
    val angle: Float
        get() {
            val a = atan2(y , x)
            return if (a > 0) a else a + 2 * PI.toFloat()
        }

    val normalized: Vec2
        get() {
            if (this.x == 0f && this.y == 0f)
                return zero
            return this / magnitude
        }

    val rightPerpendicular get() = Vec2(y, -x)

    val leftPerpendicular get() = Vec2(-y, x)

    operator fun component1(): Float {
        return x
    }
    operator fun component2(): Float {
        return y
    }

    operator fun minus(other: Vec2): Vec2 {
        return Vec2(x - other.x, y - other.y)
    }

    operator fun plus(other: Vec2): Vec2 {
        return Vec2(x + other.x, y + other.y)
    }

    operator fun unaryMinus(): Vec2 {
        return Vec2(-x, -y)
    }

    operator fun div(num: Float): Vec2 {
        return Vec2(x / num, y / num)
    }

    operator fun times(num: Float): Vec2 {
        return Vec2(x * num, y * num)
    }

    infix fun dot(other: Vec2): Float {
        return x * other.x + y * other.y
    }

    val xx
        get() = Vec2(x, x)
    val yx
        get() = Vec2(y, x)
    val yy
        get() = Vec2(y, y)

    fun rotate(angle: Float): Vec2 {
        val newX = cos(angle) * x - sin(angle) * y
        val newY = sin(angle) * x + cos(angle) * y
        return Vec2(newX, newY)
    }

    fun rotateAround(pivot: Vec2, angle: Float): Vec2 {
        var vec = this - pivot
        vec = vec.rotate(angle)
        return vec + pivot
    }

    override fun toString(): String {
        return "$x, $y"
    }

    companion object {
        val zero
            get() = Vec2(0f, 0f)
        val one
            get() = Vec2(1f, 1f)
        val right
            get() = Vec2(1f, 0f)
        val left
            get() = Vec2(-1f, 0f)
        val up
            get() = Vec2(0f, 1f)
        val down
            get() = Vec2(0f, -1f)
        fun fromAngle(angle: Float): Vec2 {
            return Vec2(cos(angle), sin(angle))
        }
        fun lerp(Vec1: Vec2, Vec2: Vec2, t: Float): Vec2 {
            return Vec2(Vec1.x + (Vec2.x - Vec1.x) * t, Vec1.y + (Vec2.y - Vec1.y) * t)
        }
        fun intersection(
            point1: Vec2,
            direction1: Vec2,
            point2: Vec2,
            direction2: Vec2
        ): Vec2 {
            val num = direction1.y * (point2.x - point1.x) + direction1.x * (point1.y - point2.y)
            val den = direction1.x * direction2.y - direction1.y * direction2.x
            val b = num / den
            return point2 + b * direction2
        }
        fun bisection(vector1: Vec2, vector2: Vec2): Vec2 {
            return Vec2(vector1.x + vector2.x, vector1.y + vector2.y).normalized
        }
        fun angle(vector1: Vec2, vector2: Vec2): Float {
            return acos((vector1.x * vector2.x + vector1.y * vector2.y) / (vector1.magnitude * vector2.magnitude))
        }
        fun distance(vec1: Vec2, vec2: Vec2): Float {
            return sqrt((vec1.x - vec2.x) * (vec1.x - vec2.x) + (vec1.y - vec2.y) * (vec1.y - vec2.y))
        }
    }
}

operator fun Float.times(other: Vec2): Vec2 {
    return Vec2(this * other.x, this * other.y)
}

class Vec3(var x: Float, var y: Float, var z: Float) {

}

data class Vec4(var x: Float, var y: Float, var z: Float, var w: Float) {
    operator fun get(index: Int): Float {
        return floatArrayOf(x, y, z, w)[index - 1]
    }
    fun swizzle(index1: Int, index2: Int, index3: Int, index4: Int) = Vec4(
        this[index1], this[index2], this[index3], this[index4]
    )
}