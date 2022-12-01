package aoc2022

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

fun part1() =
    calorieAmounts.sumOfHighest(1)

fun part2() =
    calorieAmounts.sumOfHighest(3)

private val calorieAmounts =
    readResourceFile("1.txt")
        .split("\n\n")
        .map { it.lines().sumOf(String::toInt) }

fun List<Int>.sumOfHighest(n: Int) =
    sortedDescending().take(n).sum()
