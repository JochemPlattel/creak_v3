package creakgl

import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE
import org.lwjgl.opengl.GL13.*
import org.lwjgl.opengl.GL14.GL_MIRRORED_REPEAT
import org.lwjgl.opengl.GL30.glGenerateMipmap
import org.lwjgl.stb.STBImage.stbi_image_free
import org.lwjgl.stb.STBImage.stbi_load

class Texture(
    val filepath: String,
    val horizontalWrapping: TextureWrapping = TextureWrapping.REPEAT,
    val verticalWrapping: TextureWrapping = TextureWrapping.REPEAT,
    val magFiltering: TextureFiltering = TextureFiltering.NEAREST,
    val minFiltering: TextureFiltering = TextureFiltering.NEAREST,
    slot: Int = 0
    // TODO: border color
) {
    val id = glGenTextures()
    val width: Int
    val height: Int
    val numChannels: Int
    init {
        glActiveTexture(textureSlots[slot])
        glBindTexture(GL_TEXTURE_2D, id)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, horizontalWrapping.id)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, verticalWrapping.id)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, magFiltering.id)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, minFiltering.id)
        // TODO: mip map filtering

        val widthArray = intArrayOf(0)
        val heightArray = intArrayOf(0)
        val numChannelsArray = intArrayOf(0)
        val imageData = stbi_load(filepath, widthArray, heightArray, numChannelsArray, 0)
        if (imageData != null) {
            width = widthArray[0]
            height = heightArray[0]
            numChannels = numChannelsArray[0]
            val format = if (numChannels == 4) GL_RGBA else GL_RGB
            glTexImage2D(GL_TEXTURE_2D, 0, format, width, height, 0, format, GL_UNSIGNED_BYTE, imageData)
            glGenerateMipmap(GL_TEXTURE_2D)
            stbi_image_free(imageData)
        } else
            throw Exception("Failed to load image from \"$filepath\"")
    }
}

enum class TextureWrapping(val id: Int) {
    REPEAT(GL_REPEAT),
    MIRRORED_REPEAT(GL_MIRRORED_REPEAT),
    CLAMP_TO_EDGE(GL_CLAMP_TO_EDGE),
    CLAMP_TO_BORDER(GL_CLAMP_TO_BORDER)
}

enum class TextureFiltering(val id: Int) {
    NEAREST(GL_NEAREST), LINEAR(GL_LINEAR)
}

val textureSlots = intArrayOf(
    GL_TEXTURE0,
    GL_TEXTURE1,
    GL_TEXTURE2,
    GL_TEXTURE3,
    GL_TEXTURE4,
    GL_TEXTURE5,
    GL_TEXTURE6,
    GL_TEXTURE7,
)