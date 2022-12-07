import java.util.Scanner

fun main() {
    fun part1(input: List<String>): Int {

        val fs = File(true, "/")
        val path = mutableListOf("/")
        var isLs = false

        for (line in input) {
            if (line.startsWith('$')) {
                val cmd = line.substring(1).trim().split(' ')

                when (cmd[0]) {
                    "cd" -> {
                        if (cmd[1] == "/") continue
                        if (cmd[1] == "..") {
                            path.removeLast()
                        } else {
                            path.add(cmd[1])
                        }
                    }

                    "ls" -> isLs = true
                }

                continue
            }

            if (isLs) {
                if (line.startsWith("dir")) {
                    val cmd = line.split(' ')[1]

                    var `where to add` = fs

                    for (folder in path.takeLast(path.size - 1)) {
                        `where to add` = `where to add`.contents.find { it.isDir && it.name == folder }!!
                    }

                    `where to add`.contents.add(File(true, cmd))

                } else {
                    val scan = Scanner(line)

                    val size = scan.nextInt()
                    val name = scan.next()

                    var `where to add` = fs

                    for (folder in path.takeLast(path.size - 1)) {
                        `where to add` = `where to add`.contents.find { it.isDir && it.name == folder }!!
                    }

                    `where to add`.contents.add(File(false, name, mutableListOf(), size))
                }
            }
        }

        fun getDirs(file: File): List<File> {
            if (!file.isDir) return emptyList()

            return listOf(file) + file.contents.flatMap { getDirs(it) }
        }

        val allTheDirs = getDirs(fs)

        var sum = 0

        for (dir in allTheDirs) {
            if (dir.sum < 100_000) sum += dir.sum
        }

        return sum
    }

    fun part2(input: List<String>): Int {

        val fs = File(true, "/")
        val path = mutableListOf("/")
        var isLs = false

        for (line in input) {
            if (line.startsWith('$')) {
                val cmd = line.substring(1).trim().split(' ')

                when (cmd[0]) {
                    "cd" -> {
                        if (cmd[1] == "/") continue
                        if (cmd[1] == "..") {
                            path.removeLast()
                        } else {
                            path.add(cmd[1])
                        }
                    }

                    "ls" -> isLs = true
                }

                continue
            }

            if (isLs) {
                if (line.startsWith("dir")) {
                    val cmd = line.split(' ')[1]

                    var `where to add` = fs

                    for (folder in path.takeLast(path.size - 1)) {
                        `where to add` = `where to add`.contents.find { it.isDir && it.name == folder }!!
                    }

                    `where to add`.contents.add(File(true, cmd))

                } else {
                    val scan = Scanner(line)

                    val size = scan.nextInt()
                    val name = scan.next()

                    var `where to add` = fs

                    for (folder in path.takeLast(path.size - 1)) {
                        `where to add` = `where to add`.contents.find { it.isDir && it.name == folder }!!
                    }

                    `where to add`.contents.add(File(false, name, mutableListOf(), size))
                }
            }
        }

        fun getDirs(file: File): List<File> {
            if (!file.isDir) return emptyList()

            return listOf(file) + file.contents.flatMap { getDirs(it) }
        }

        val availableSpace = 70_000_000 - fs.sum
        val neededSpace = 30_000_000 - availableSpace

        return getDirs(fs).minOf { if (it.sum > neededSpace) it.sum else availableSpace }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    part1(testInput).also { println(it); check(it == 95437) }

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}

data class File(
    val isDir: Boolean,
    val name: String,
    var contents: MutableList<File> = mutableListOf(),
    val size: Int = 0
) {
    val sum: Int
        get() = contents.sumOf { if (it.isDir) it.sum else it.size }
}