package com.example

import com.example.config.configSetup
import com.example.di.koinSetup
import com.example.di.productionModules
import com.example.setUp.authSetup
import com.example.setUp.contentNegotiationSetup
import com.example.setUp.corsSetup
import com.example.setUp.routesSetup
import io.ktor.application.*
import org.koin.core.module.Module

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
@kotlin.jvm.JvmOverloads
fun Application.module(
    testing: Boolean = false,
    koinModules: List<Module> = productionModules
) {
    koinSetup(koinModules)
    configSetup(testing)
    contentNegotiationSetup()
    corsSetup()
    authSetup()
    routesSetup()
}