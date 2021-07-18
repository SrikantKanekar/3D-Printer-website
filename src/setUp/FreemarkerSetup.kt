package com.example.setUp

import freemarker.cache.ClassTemplateLoader
import io.ktor.application.*
import io.ktor.freemarker.*

fun Application.freemarkerSetup() {
    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(
            this::class.java.classLoader,
            "templates"
        )
    }
}