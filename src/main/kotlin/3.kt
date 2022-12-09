private val input = readFileAsLines("3.txt")

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

private fun part1() =
    input.sumOf { backpack ->
        backpack.splitIntoCompartments().findCommonItem().toPriority()
    }

private fun part2() =
    input.chunked(3).sumOf { groupOfBackpacks ->
        groupOfBackpacks.findCommonItem().toPriority()
    }

fun String.splitIntoCompartments(): List<String> =
    this.chunked(this.length / 2)

fun List<String>.findCommonItem() =
    this.map { it.toSet() }.reduce { acc, chars -> acc.intersect(chars) }.first()

fun Char.toPriority() = if (this.isUpperCase()) this.code - 38 else this.code - 96
