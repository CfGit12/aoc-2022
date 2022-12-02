package aoc2022

private val input = readResourceFileAsLines("2.txt")
    .map {
        val (elf, you) = it.split(" ")
        elf to you
    }

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

private fun part1() =
    input
        .sumOf { (elfInput, yourInput) ->
            battleAndScore(elfInput.toRPS(), yourInput.toRPS())
        }

private fun part2() =
    input
        .sumOf { (elfInput, yourInput) ->
            val elfMove = elfInput.toRPS()
            val yourMove = determineMove(elfMove, yourInput.toDesiredOutcome())
            battleAndScore(elfMove, yourMove)
        }


private enum class RPS(val pointsValue: Int) {
    ROCK(1), PAPER(2), SCISSORS(3)
}

private fun String.toRPS(): RPS =
    when (this) {
        "A", "X" -> RPS.ROCK
        "B", "Y" -> RPS.PAPER
        "C", "Z" -> RPS.SCISSORS
        else -> error("invalid input")
    }

private enum class Outcome(val pointsValue: Int) {
    WIN(6), LOSE(0), DRAW(3)
}

private fun String.toDesiredOutcome(): Outcome =
    when (this) {
        "X" -> Outcome.LOSE
        "Y" -> Outcome.DRAW
        "Z" -> Outcome.WIN
        else -> error("invalid input")
    }

private fun battle(elfMove: RPS, yourMove: RPS): Outcome =
    when {
        yourMove == RPS.ROCK && elfMove == RPS.SCISSORS -> Outcome.WIN
        yourMove == RPS.PAPER && elfMove == RPS.ROCK -> Outcome.WIN
        yourMove == RPS.SCISSORS && elfMove == RPS.PAPER -> Outcome.WIN
        yourMove == elfMove -> Outcome.DRAW
        else -> Outcome.LOSE
    }

private fun battleAndScore(elfMove: RPS, yourMove: RPS): Int =
    battle(elfMove, yourMove).pointsValue + yourMove.pointsValue

private fun determineMove(elfMove: RPS, outcome: Outcome): RPS =
    RPS.values().first { battle(elfMove, it) == outcome }
