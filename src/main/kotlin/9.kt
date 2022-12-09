import kotlin.math.abs

private val input = readFileAsLines("9.txt")
    .map {
        val (direction, amount) = it.split(" ")
        direction to amount.toInt()
    }


fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

private fun part1() = simulateRope(2)

private fun part2() = simulateRope(10)

fun simulateRope(size: Int): Int {
    val tailCoordinates = mutableSetOf<Coordinate>()
    val coordinates = MutableList(size) { Coordinate(0, 0) }

    for ((direction, amount) in input) {
        repeat(amount) {
            when (direction) {
                "U" -> coordinates[0] = coordinates[0].above()
                "D" -> coordinates[0] = coordinates[0].below()
                "L" -> coordinates[0] = coordinates[0].toLeft()
                "R" -> coordinates[0] = coordinates[0].toRight()
            }
            for (i in 1 until coordinates.size) {
                coordinates[i] = coordinates[i].moveTowards(coordinates[i - 1])
            }

            tailCoordinates.add(coordinates.last())
        }
    }

    return tailCoordinates.size
}

fun Coordinate.toRight() = Coordinate(x + 1, y)
fun Coordinate.toLeft() = Coordinate(x - 1, y)
fun Coordinate.above() = Coordinate(x, y + 1)
fun Coordinate.below() = Coordinate(x, y - 1)

fun Coordinate.moveTowards(other: Coordinate): Coordinate {
    if (abs(x - other.x) > 1 || abs(y - other.y) > 1) {
        val around = getSurroundingCoordinatesIncDiagonals().toSet()
        val aroundOther = other.getSurroundingCoordinatesIncDiagonals().toSet()
        val intersected = (around intersect aroundOther).toList()

        return intersected.firstOrNull { it.x == x && it.x == other.x }
            ?: intersected.firstOrNull { it.y == y && it.y == other.y }
            ?: intersected.first { it.x != x && it.y != y }
    }
    return this
}
