private val input = readFile("6.txt")

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

private fun part1() = findPositionOfDistinctChars(4)

private fun part2() = findPositionOfDistinctChars(14)

private fun findPositionOfDistinctChars(n: Int) =
    input
        .windowed(n)
        .indexOfFirst { it.toSet().size == n } + n
