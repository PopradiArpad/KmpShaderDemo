package com.popradiarpad.kmpshaderdemo

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform