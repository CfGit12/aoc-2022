import kotlin.math.abs

private val input = readFileAsLines("15.txt").map { line ->
    val regex = "Sensor at x=(-*\\d+), y=(-*\\d+): closest beacon is at x=(-*\\d+), y=(-*\\d+)".toRegex()
    val (sx, sy, bx, by) = regex.find(line)!!.groupValues.drop(1).map { it.toInt() }
    SensorAndBeacon(Coordinate(sx, sy), Coordinate(bx, by))
}


fun main() {
    //println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

private fun part1(): Int {
    val lowestX = input.minOf { it.sensor.x - it.manhattanDistance }
    val highestX = input.maxOf { it.sensor.x + it.manhattanDistance }

    val sensorAndBeaconCoordinates = input.flatMap { listOf(it.sensor, it.beacon) }

    var count = 0
    outer@ for (x in lowestX .. highestX) {
        val coordinate = Coordinate(x, 10)
        for (sensorAndBeacon in input) {
            val distanceToSensor = manhattanDistanceBetween(coordinate, sensorAndBeacon.sensor)
            //println("coordinate: $coordinate, sensor: $sensorAndBeacon.sensor, mDistance: ${sensorAndBeacon.manhattanDistance}, distanceToSensor: $distanceToSensor")
            if (distanceToSensor <= sensorAndBeacon.manhattanDistance && coordinate !in sensorAndBeaconCoordinates) {
                //println("WITHIN DISTANCE, INCREASE COUNT")
                count++
                continue@outer
            }
        }
        //println("COULD BE HERE")
    }

    return count
}

private fun part2(): Coordinate? {
    for (sensorAndBeacon in input) {
        val perimeterCoords = sensorAndBeacon.perimeterCoordinates()
        for (perimeterCoord in perimeterCoords) {
            val isNotWithinABeacon = input.none { perimeterCoord.isWithinManhattanDistanceOf(it) }
            if (isNotWithinABeacon && perimeterCoord.x >= 0 && perimeterCoord.x <= 4_000_000 && perimeterCoord.y >= 0 && perimeterCoord.y <= 4_000_000) {
                return perimeterCoord
            }
        }
    }

    return null
}

data class SensorAndBeacon(val sensor: Coordinate, val beacon: Coordinate) {
    val manhattanDistance = manhattanDistanceBetween(sensor, beacon)
    //val perimeterCoordinates = perimeterCoordinates()

    fun perimeterCoordinates() = buildSet {
        val outerPerimeterDistance = manhattanDistance + 1
        (-outerPerimeterDistance..outerPerimeterDistance).forEach { xDelta ->
            val y1Delta = outerPerimeterDistance - abs(xDelta)
            val y2Delta = -y1Delta
            add(Coordinate(sensor.x + xDelta, sensor.y + y1Delta))
            add(Coordinate(sensor.x + xDelta, sensor.y + y2Delta))
        }
    }

}

private fun MutableMap<Coordinate, Char>.putIfNotObject(coordinate: Coordinate, char: Char) {
    if (this[coordinate] == null || this[coordinate] == '#') {
        this[coordinate] = char
    }
}

private fun Coordinate.isWithinManhattanDistanceOf(sensorAndBeacon: SensorAndBeacon) =
    manhattanDistanceBetween(this, sensorAndBeacon.sensor) <= sensorAndBeacon.manhattanDistance

private fun manhattanDistanceBetween(first:Coordinate, second:Coordinate) =
    abs(first.x - second.x) + abs(first.y - second.y)
