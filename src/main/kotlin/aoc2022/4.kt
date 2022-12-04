package aoc2022

private val input = readResourceFileAsLines("4.txt")
    .map { line ->
        val (l, r) = line.split(",")
        fun String.toRange() = split("-").let { (a, b) -> a.toInt()..b.toInt() }
        l.toRange() to r.toRange()
    }

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

private fun part1() = input
    .count { (elfARange, elfBRange) ->
        elfARange.all { it in elfBRange } || elfBRange.all { it in elfARange }
    }

private fun part2() = input
    .count { (elfARange, elfBRange) ->
        elfARange.any { it in elfBRange } || elfBRange.any { it in elfARange }
    }
