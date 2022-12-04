package aoc2022

private val input = readResourceFileAsLines("4.txt")
    .map { line ->
        val (elfALower, elfAHigher, elfBLower, elfBHigher) = line.split(",", "-").map(String::toInt)
        elfALower..elfAHigher to elfBLower..elfBHigher
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
        elfARange.any { it in elfBRange }
    }
