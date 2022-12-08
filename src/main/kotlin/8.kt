private val input = readFileAsLines("8.txt").map { line -> line.map { it.digitToInt() } }

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

private fun part1(): Int {
    var count = 0
    for (x in input.indices) {
        for (y in input.indices) {
            if (isVisible(x, y, input)) count++
        }
    }
    return count
}

private fun part2(): Int {
    var highest = 0
    for (x in 1..input.size - 2) {
        for (y in 1..input.size - 2) {
            val scenicScore = scenicScore(x, y, input)
            if (scenicScore > highest) highest = scenicScore
        }
    }
    return highest
}

fun isVisible(x: Int, y: Int, grid: List<List<Int>>) =
    isVisibleFromNorth(x, y, grid) ||
            isVisibleFromSouth(x, y, grid) ||
            isVisibleFromEast(x, y, grid) ||
            isVisibleFromWest(x, y, grid)

fun isVisibleFromNorth(x: Int, y: Int, grid: List<List<Int>>) =
    (y - 1 downTo 0).all { yPos -> grid[x][yPos] < grid[x][y] }

fun isVisibleFromSouth(x: Int, y: Int, grid: List<List<Int>>) =
    (y + 1 until grid.size).all { yPos -> grid[x][yPos] < grid[x][y] }

fun isVisibleFromEast(x: Int, y: Int, grid: List<List<Int>>) =
    (x + 1 until grid[x].size).all { xPos -> grid[xPos][y] < grid[x][y] }

fun isVisibleFromWest(x: Int, y: Int, grid: List<List<Int>>) =
    (x - 1 downTo 0).all { xPos -> grid[xPos][y] < grid[x][y] }

fun scenicScore(x: Int, y: Int, grid: List<List<Int>>) =
    visibleTreesFromNorth(x, y, grid) *
            visibleTreesFromSouth(x, y, grid) *
            visibleTreesFromEast(x, y, grid) *
            visibleTreesFromWest(x, y, grid)

fun visibleTreesFromNorth(x: Int, y: Int, grid: List<List<Int>>) =
    (y - 1 downTo 1).takeWhile { yPos -> grid[x][y] > grid[x][yPos] }.count() + 1

fun visibleTreesFromSouth(x: Int, y: Int, grid: List<List<Int>>) =
    (y + 1 until grid.size - 1).takeWhile { yPos -> grid[x][y] > grid[x][yPos] }.count() + 1

fun visibleTreesFromEast(x: Int, y: Int, grid: List<List<Int>>) =
    (x + 1 until grid.size - 1).takeWhile { xPos -> grid[x][y] > grid[xPos][y] }.count() + 1

fun visibleTreesFromWest(x: Int, y: Int, grid: List<List<Int>>) =
    (x - 1 downTo 1).takeWhile { xPos -> grid[x][y] > grid[xPos][y] }.count() + 1
