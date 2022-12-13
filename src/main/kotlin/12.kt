private val input = buildMap {
    readFileAsLines("12.txt")
        .mapIndexed { y, s ->
            s.mapIndexed { x, c ->
                put(Coordinate(x, y), c)
            }
        }
}

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

private fun part1() = findShortestPath('S')

private fun part2() = findShortestPath('S', 'a')

private fun findShortestPath(vararg startingChars: Char): Int {
    val start = input.filterValues { it in startingChars }.keys

    val visitedCoordinates = mutableSetOf<Coordinate>()
    val candidateCoordinates = start.toMutableSet()
    val smallestDistances = start.associateBy({it}, {0}).toMutableMap()

    val maxX = input.keys.maxOf { it.x }
    val maxY = input.keys.maxOf { it.y }

    while (true) {
        val currentCoordinate = candidateCoordinates.minByOrNull { smallestDistances.getOrDefault(it, Int.MAX_VALUE) } ?: break

        val neighbours = currentCoordinate
            .getSurroundingCoordinates(minX = 0, minY = 0, maxX = maxX, maxY = maxY)
            .filter { it !in visitedCoordinates && input[currentCoordinate]!!.isWithinOneOf(input[it]!!) }

        for (neighbour in neighbours) {
            candidateCoordinates.add(neighbour)

            val distanceFromHere = smallestDistances[currentCoordinate]!! + 1
            if (distanceFromHere < smallestDistances.getOrDefault(neighbour, Int.MAX_VALUE)) {
                smallestDistances[neighbour] = distanceFromHere
            }
        }

        visitedCoordinates.add(currentCoordinate)
        candidateCoordinates.remove(currentCoordinate)
    }

    return smallestDistances[input.filterValues { it == 'E' }.keys.first()]!!
}

private fun Char.coerce() = if (this == 'S') 'a' else if (this == 'E') 'z' else this
private fun Char.isWithinOneOf(other: Char) = other.coerce() - this.coerce() <= 1
