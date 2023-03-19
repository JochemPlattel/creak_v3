#version 400
in vec2 texCoords;
uniform sampler2D texSampler1;
uniform sampler2D texSampler2;
out vec4 outColor;

void main() {
    vec4 texColor = (texture(texSampler1, texCoords) + texture(texSampler2, texCoords)) / 2;
    outColor = vec4((texColor.x + texColor.y + texColor.z) / 3);
}