package com.example.util

import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

object FileHandler {

    fun createFile(objectId: String): File {
        return File("uploads/$objectId")
    }

    fun deleteFile(objectId: String): Boolean {
        return File("uploads/$objectId").delete()
    }

    fun fileExists(objectId: String): Boolean {
        return File("uploads/$objectId").exists()
    }

    fun readFileByteArray(objectId: String): ByteArray {
        return Files.readAllBytes(Paths.get("uploads/$objectId"))
    }
}