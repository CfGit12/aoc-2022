package aoc2022

fun readResourceFile(name: String) = object {}::class.java.classLoader.getResource(name)!!.readText().trim()

fun readResourceFileAsLines(name: String) = readResourceFile(name).lines()
