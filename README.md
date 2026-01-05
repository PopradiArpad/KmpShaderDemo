# KMP Shader Demo: Universal Shaders for Android, iOS, and Web

https://github.com/user-attachments/assets/9ae9a95b-6e84-4fa0-b4cc-1edcd466d287

[Read the full guide in this Medium article](https://medium.com/@popradi.arpad11/integrating-shaders-into-a-compose-multiplatform-project-1bb4e55aced1)

## The Shader Runner
Implemented as a modifier.

### Platform specific implementation
The shader runner is platform specific: one for Android, and one for all other platforms.
The shader language is *SkSL - Skia Shading Language*,
allowing to write one shader for all platforms.

### Inputs for the Shader
The shader runner delivers time and tap position to the shader.

## The Shader in the Video
is documented in the code.

## Build and Run
As usual from Android Studio.
