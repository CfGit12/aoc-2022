private val input = buildMap<Coordinate, Char> {
    readFileAsLines("14.txt").forEach { line ->
        line.split(" -> ", ",")
            .chunked(2)
            .map { (l, r) -> Coordinate(l.toInt(), r.toInt()) }
            .windowed(2)
            .forEach { (leftCoord, rightCoord) ->
                (leftCoord.x between rightCoord.x).forEach { x ->
                    (leftCoord.y between rightCoord.y).forEach { y ->
                        put(Coordinate(x, y), '#')
                    }
                }
            }
    }
    put(Coordinate(500, 0), '+')
}

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}


private fun part1() = letTheSandFlow(hasFloor = false)
private fun part2() = letTheSandFlow(hasFloor = true)

fun letTheSandFlow(hasFloor: Boolean): Int {
    val cave = input.toMutableMap()
    val lowestRock = input.keys.maxOf { it.y }
    val source = cave.filterValues { it == '+' }.keys.first()

    fun dropSand(from: Coordinate): Coordinate? {
        return when {
            cave[from] == 'o' -> null
            !hasFloor && from.y == lowestRock -> null
            hasFloor && from.y == lowestRock + 1 -> from
            cave[from.below()] == null -> dropSand(from.below())
            cave[from.belowLeft()] == null -> dropSand(from.belowLeft())
            cave[from.belowRight()] == null -> dropSand(from.belowRight())
            else -> from
        }
    }

    var numberRested = 0

    while(true) {
        val restingPlace = dropSand(source)
        if (restingPlace != null) {
            cave[restingPlace] = 'o'
            numberRested++
        } else break
    }

    return numberRested
}

private fun Coordinate.below() = copy(y = y + 1)
private fun Coordinate.belowLeft() = copy(x = x - 1, y = y + 1)
private fun Coordinate.belowRight() = copy(x = x + 1, y = y + 1)
