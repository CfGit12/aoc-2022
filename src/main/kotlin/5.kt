typealias Stacks = MutableMap<Int, ArrayDeque<Char>>
typealias Instructions = List<Triple<Int, Int, Int>>

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

fun processInput(): Pair<Stacks, Instructions> {
    val (stackStrings, instructionsStrings) = readFile("5.txt").split("\n\n").map(String::lines)
    val numberOfStacks = stackStrings.reversed()[0].trim().split("\\s+".toRegex()).count()

    val stacks: Stacks = mutableMapOf()

    repeat(numberOfStacks) { i ->
        stacks[i + 1] = ArrayDeque()
    }

    fun extractCrates(input: String, limit: Int) {
        repeat(limit) { i ->
            val n = i + 1
            input.getOrNull(4 * (n - 1) + 1)?.let { char ->
                if (char.isLetter()) {
                    stacks[n]!!.addLast(char)
                }
            }
        }
    }

    stackStrings.reversed().drop(1).forEach { extractCrates(it, numberOfStacks) }

    val instructionRegex = "move (\\d+) from (\\d+) to (\\d+)".toRegex()
    val instructions = instructionsStrings.map { instructionString ->
        val matches = instructionRegex.find(instructionString)!!.groupValues
        Triple(matches[1].toInt(), matches[2].toInt(), matches[3].toInt())
    }
    return stacks to instructions
}

private fun part1(): String {
    val (stacks, instructions) = processInput()

    for ((moves, from, to) in instructions) {
        repeat(moves) {
            val popped = stacks[from]!!.removeLast()
            stacks[to]!!.addLast(popped)
        }
    }

    return stacks.values.map { it.last() }.joinToString("")
}

private fun part2(): String {
    val (stacks, instructions) = processInput()

    for ((amountToMove, from, to) in instructions) {
        val popped: List<Char> = buildList {
            repeat(amountToMove) {
                add(stacks[from]!!.removeLast())
            }
        }
        popped.reversed().forEach { stacks[to]!!.addLast(it) }
    }

    return stacks.values.map { it.last() }.joinToString("")
}
