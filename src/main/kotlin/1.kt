private val input = readFile("1.txt")

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

private fun part1() =
    calorieAmounts.sumOfHighest(1)

private fun part2() =
    calorieAmounts.sumOfHighest(3)

private val calorieAmounts =
    input
        .split("\n\n")
        .map { it.lines().sumOf(String::toInt) }

private fun List<Int>.sumOfHighest(n: Int) =
    sortedDescending().take(n).sum()
