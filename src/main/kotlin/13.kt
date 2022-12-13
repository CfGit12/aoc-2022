import kotlinx.serialization.json.*

private val input = readFile("13.txt")
    .split("\n\n", "\n")
    .map { Json.parseToJsonElement(it) }

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

private fun comparePair(left: JsonElement, right: JsonElement): Int =
    when {
        left is JsonPrimitive && right is JsonPrimitive -> left.content.toInt().compareTo(right.content.toInt())
        left is JsonArray && right is JsonPrimitive -> comparePair(left, JsonArray(listOf(right)))
        left is JsonPrimitive && right is JsonArray -> comparePair(JsonArray(listOf(left)), right)
        left is JsonArray && right is JsonArray -> {
            var comparison = 0
            for ((l, r) in left zip right) {
                comparison = comparePair(l, r)
                if (comparison == 1 || comparison == -1) break
            }
            if (comparison == 0 && left.size > right.size) 1
            else if (comparison == 0 && left.size == right.size) 0
            else if (comparison == 0) -1
            else comparison
        }
        else -> error("shouldn't happen")
    }

private fun part1() =
    input.chunked(2).mapIndexed { index, (left, right) ->
        if (comparePair(left, right) == -1) index + 1 else 0
    }.sum()


private fun part2(): Int {
    val dividerPackets = listOf(Json.parseToJsonElement("[[2]]"), Json.parseToJsonElement("[[6]]"))
    val sorted = (input + dividerPackets)
        .sortedWith { left, right -> comparePair(left, right) }
    return dividerPackets.map { sorted.indexOf(it) + 1 }.reduce { acc, i -> acc * i }
}
