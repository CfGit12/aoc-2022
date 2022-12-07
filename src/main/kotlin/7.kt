private val rootDir = buildDirectoryTree(readFileAsLines("7.txt"))

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

private fun part1() =
    rootDir
        .allSubDirectories
        .filter { it.size <= 100_000 }.sumOf { it.size }

private fun part2() =
    rootDir
        .allSubDirectories
        .filter { it.size > 30_000_000 - (70_000_000 - rootDir.size) }
        .minOf { it.size }

private sealed class Node {
    abstract val name: String
}

private data class File(
    override val name: String,
    val size: Int
) : Node() {
    override fun toString() = "$size $name"
}

private data class Directory(
    override val name: String,
    val parent: Directory? = null,
) : Node() {
    override fun toString() = "dir $name"

    private val files: MutableList<File> = mutableListOf()
    private val subDirectories: MutableList<Directory> = mutableListOf()

    val allSubDirectories: List<Directory>
        get() = subDirectories + subDirectories.flatMap { it.allSubDirectories }

    val size: Int
        get() = files.sumOf { it.size } + subDirectories.sumOf { it.size }

    fun getSubDirectory(name: String) =
        subDirectories.firstOrNull { it.name == name }

    fun addChild(child: Node) {
        when(child) {
            is Directory -> subDirectories.add(child)
            is File -> files.add(child)
        }
    }
}

private sealed class Command
private object Ls : Command()
private class Cd(val path: String) : Command()

private fun String.parseCommand(): Command =
    when {
        startsWith("ls") -> Ls
        startsWith("cd") -> Cd(substring(3))
        else -> error("unknown command")
    }

private fun buildDirectoryTree(inputLines: List<String>): Directory {
    val homeDirectory = Directory("/")
    var currentDirectory = homeDirectory

    fun processInput(input: List<String>) {
        if (input.isEmpty()) return

        when (val command = input.first().substring(2).parseCommand()) {
            is Cd -> {
                currentDirectory = if (command.path == "..") {
                    currentDirectory.parent ?: homeDirectory
                } else {
                    currentDirectory.getSubDirectory(command.path) ?: currentDirectory
                }
                processInput(input.drop(1))
            }

            is Ls -> {
                val nodes = input.drop(1).takeWhile { !it.startsWith("$") }
                for (node in nodes) {
                    if (node.startsWith("dir")) {
                        val name = node.substring(4)
                        val newDirectory = Directory(name, parent = currentDirectory)
                        currentDirectory.addChild(newDirectory)
                    } else {
                        val (size, name) = node.split(" ")
                        val newFile = File(name, size.toInt())
                        currentDirectory.addChild(newFile)
                    }
                }
                processInput(input.drop(1).dropWhile { !it.startsWith("$") })
            }
        }
    }

    processInput(inputLines)

    return homeDirectory
}
