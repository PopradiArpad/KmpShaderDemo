package com.popradiarpad.kmpshaderdemo.util

import co.touchlab.kermit.Logger

fun initLogger() {
    // Set a global default tag for ALL logs
    Logger.setTag("AiWorkflow")
}

val l = Logger