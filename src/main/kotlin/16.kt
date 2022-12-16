private val input = readFileAsLines("16.txt")
private val valves = buildValves()
private val visitableValves = valves.filter { it.flowRate > 0 }.toSet()
private val start = valves.getByName("AA")

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

private fun part1() = highestFlow(start, emptySet(), 30, emptyList(), 0, visitableValves)

private fun part2() =
    (1 until (1 shl visitableValves.size - 1)).maxOf {
        val me = visitableValves.filterIndexed { idx, _ -> (it shr idx) % 2 == 1 }.toSet()
        val elephant = visitableValves.subtract(me)
        highestFlow(start, emptySet(), 26, emptyList(), 0, me) + highestFlow(start, emptySet(), 26, emptyList(), 0, elephant)
    }

private fun highestFlow(
    currentValve: Valve,
    openedValves: Set<Valve>,
    timeLeft: Int,
    flows: List<Int>,
    total: Int,
    visitableValves: Set<Valve>
): Int {
    if (timeLeft == 0) return total

    val targetValves = currentValve.getConnectedValvesAndDistances()
        .filter { (valve, distance) ->
            valve in visitableValves && valve !in openedValves && distance + 1 <= timeLeft
        }

    if (targetValves.isEmpty())
        return highestFlow(currentValve, openedValves, timeLeft - 1, flows, total + flows.sum(), visitableValves)

    return targetValves.map { (valve, distance) ->
        highestFlow(
            valve,
            openedValves + valve,
            timeLeft - distance - 1,
            flows + valve.flowRate,
            total + (flows.sum() * (distance + 1)),
            visitableValves
        )
    }.max()
}

private data class Valve(val name: String, val flowRate: Int) {
    private lateinit var connectedValves: List<Valve>
    private lateinit var distancesToOtherValves: Map<Valve, Int>

    fun getConnectedValvesAndDistances() = distancesToOtherValves

    fun addConnectedValves(valves: List<Valve>) {
        connectedValves = valves
    }

    fun computeDistancesToOtherValves() {
        val visited = mutableSetOf<Valve>()
        val candidates = mutableSetOf(this)
        val shortestDistances = candidates.associateBy({ it }, { 0 }).toMutableMap()

        while (true) {
            val currentValve = candidates.minByOrNull { shortestDistances[it] ?: Int.MAX_VALUE } ?: break
            val connectedValves = currentValve.connectedValves.filter { it !in visited }

            for (connectedValve in connectedValves) {
                candidates.add(connectedValve)
                val distanceFromHere = shortestDistances[currentValve]!! + 1
                if (distanceFromHere < (shortestDistances[connectedValve] ?: Int.MAX_VALUE)) {
                    shortestDistances[connectedValve] = distanceFromHere
                }
            }

            visited.add(currentValve)
            candidates.remove(currentValve)
        }

        distancesToOtherValves = shortestDistances.filterKeys { it != this }
    }

}

private fun buildValves(): List<Valve> {
    val valvesAndChildren = input.map { line ->
        val valveName = line.substringAfter("Valve ").substringBefore(" has")
        val flowRate = line.substringAfter("=").substringBefore(";").toInt()
        val children = line.substringAfter("valves ").substringAfter("valve ").split(", ")
        Valve(valveName, flowRate) to children
    }

    return valvesAndChildren.map { (valve, children) ->
        valve.addConnectedValves(
            children.map { childName -> valvesAndChildren.map { it.first }.getByName(childName) }
        )
        valve
    }.map {
        it.computeDistancesToOtherValves()
        it
    }
}

private fun List<Valve>.getByName(name: String): Valve = first { it.name == name }
