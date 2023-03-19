package creakgl

class Camera3D(var fov: Float, var near: Float, var far: Float, var aspectRatio: Float) {
    var position = Vec3(0f, 0f, 0f)
    var rotation = Vec3(0f, 0f, 0f)
    fun projection() = Mat4.perspectiveNEW(aspectRatio, fov, far, near)
    fun view() = Mat4.rotation(-rotation.x, -rotation.y, -rotation.z) * Mat4.translation(-position.x, -position.y, -position.z)
    fun projectionView() = projection() * view()
}