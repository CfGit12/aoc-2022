private val input = readFileAsLines("10.txt").map { it.parseInstruction() }

fun main() {
    runCpu()
}

private fun runCpu() {
    val instructions = ArrayDeque(input)

    var currentInstruction: Instruction? = null
    var currentDuration = 0
    var currentCycle = 0

    var register = 1

    val crtStringBuilder = StringBuilder()

    fun drawToCrt(currentCycle: Int) {
        val position = (currentCycle - 1) % 40
        crtStringBuilder.append(if (position in register - 1..register + 1) "⚪" else "⚫")
    }

    val signalStrengths = mutableListOf<Int>()

    while (true) {
        currentCycle++

        // Set up the currently executing instruction
        if (currentInstruction == null) {
            if (instructions.isNotEmpty()) {
                val instruction = instructions.removeFirst()
                currentInstruction = instruction
                currentDuration = instruction.duration
            } else {
                break
            }
        }

        // Executing
        drawToCrt(currentCycle)
        if ((currentCycle + 20) % 40 == 0) {
            signalStrengths.add(currentCycle * register)
        }

        // Finish cycle
        currentDuration--
        if (currentDuration == 0) {
            when (currentInstruction) {
                is AddX -> register += currentInstruction.amount
                is NoOp -> Unit
            }
            currentInstruction = null
        }
    }

    // Render CRT
    val crtString = crtStringBuilder.toString()
    crtString.chunked(40).map { println(it) }

    println("Signal strength: ${signalStrengths.sum()}")
}

private sealed class Instruction {
    abstract val duration: Int
}

private data class AddX(val amount: Int) : Instruction() {
    override val duration = 2
}

private object NoOp : Instruction() {
    override val duration = 1
}

private fun String.parseInstruction(): Instruction =
    when {
        startsWith("addx") -> AddX(split(" ")[1].toInt())
        startsWith("noop") -> NoOp
        else -> error("invalid instruction")
    }
