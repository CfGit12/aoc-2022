private val input = readFile("11.txt").split("\n\n")

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

private fun part1(): Long {
    val monkeys = parseMonkeys()
    return monkeyBusiness(monkeys, 20, mitigateFn = { it / 3 })
}

private fun part2(): Long {
    val monkeys = parseMonkeys()
    val product = monkeys.map { it.testAmount }.reduce { acc, l -> acc * l }
    return monkeyBusiness(monkeys, 10_000, mitigateFn = { it % product })
}

fun monkeyBusiness(monkeys: List<Monkey>, rounds: Int, mitigateFn: (Long) -> Long): Long {
    repeat(rounds) {
        repeat(monkeys.size) { i ->
            val thrownItems = monkeys[i].inspectAndThrowItems(mitigateFn)
            thrownItems.forEach { (item, monkey) ->
                monkeys[monkey.toInt()].receiveItem(item)
            }
        }
    }
    return monkeys.map { it.inspections.toLong() }.sortedDescending().take(2).reduce { acc, i -> acc * i }
}

data class Monkey(
    private val items: ArrayDeque<Long>,
    private val worryLevelCalculation: (Long) -> Long,
    private val throwTest: (Long) -> Long,
    val testAmount: Long
) {
    var inspections = 0

    fun receiveItem(item: Long) {
        items.addLast(item)
    }

    fun inspectAndThrowItems(mitigateFn: (Long) -> Long): List<Pair<Long, Long>> = buildList {
        while (items.isNotEmpty()) {
            inspections++
            val item = items.removeFirst()
            val newWorryLevel = mitigateFn(worryLevelCalculation(item))
            val monkeyToReceive = throwTest(newWorryLevel)
            add(newWorryLevel to monkeyToReceive)
        }
    }
}

private fun parseMonkeys(): List<Monkey> = buildList {
    input.forEach {monkeyString ->
        add(parseMonkey(monkeyString))
    }
}

private fun parseMonkey(monkeyString: String): Monkey {
    val monkeyLines = monkeyString.lines()
    val items = monkeyLines[1].removePrefix("  Starting items:").replace(" ", "").split(",").map { it.toLong() }

    val (left, operator, right) = monkeyLines[2].removePrefix("  Operation: new = ").split(" ")
    val worryLevelCalculation: (Long) -> Long = { old ->
        fun String.toValue() = this.toLongOrNull() ?: old
        if (operator == "+") left.toValue() + right.toValue() else left.toValue() * right.toValue()
    }

    val testAmount = monkeyLines[3].removePrefix("  Test: divisible by ").toLong()
    val throwA = monkeyLines[4].removePrefix("    If true: throw to monkey ").toLong()
    val throwB = monkeyLines[5].removePrefix("    If false: throw to monkey ").toLong()
    val throwTest: (Long) -> Long = { if (it % testAmount == 0L) throwA else throwB }

    return Monkey(ArrayDeque(items), worryLevelCalculation, throwTest, testAmount)
}
