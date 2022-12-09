fun readFile(name: String) = object {}::class.java.classLoader.getResource(name)!!.readText().trimEnd()

fun readFileAsLines(name: String) = readFile(name).lines()

data class Coordinate(
    val x: Int,
    val y: Int
)

fun Coordinate.getSurroundingCoordinatesIncDiagonals() = listOf(
    Coordinate(x, y - 1), Coordinate(x, y + 1), Coordinate(x + 1, y), Coordinate(x - 1, y),
    Coordinate(x + 1, y + 1), Coordinate(x + 1, y - 1), Coordinate(x - 1, y - 1), Coordinate(x - 1, y + 1),
)
