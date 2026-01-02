# Shader integration in Compose Multiplatform Demo targeting Android, iOS, Web, Desktop (JVM)

https://github.com/user-attachments/assets/9ae9a95b-6e84-4fa0-b4cc-1edcd466d287

## The Shader Runner
Implemented as a modifier.

### Platform specific implementation
The shader runner is platform specific: one for Android the other is for all the other platforms
using Skiko (Skia for Kotlin) as graphic engine. The sh


ader language is SkSL - Skia Shading Language,
allowing to write one shader for all platforms.

### Inputs for the Shader
The shader runner delivers time and tap position.

## The Shader on the image
is documented in the code.

## Build and Run

As usual from Android Studio.
