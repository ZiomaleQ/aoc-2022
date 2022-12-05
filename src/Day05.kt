import java.util.Scanner

fun main() {
    fun part1(input: List<String>): String {

        val splitIndex = input.withIndex().first { it.value.isBlank() }.index

        val configuration = input.subList(0, splitIndex)
        val moves = input.subList(splitIndex + 1, input.size)

        val columns = mutableListOf<Int>()
        val boxes = mutableListOf<Box>()

        for (line in configuration) {
            if (!line.contains('[') || !line.contains(']')) {
                columns.addAll(line.filter { !it.isWhitespace() }.split("").filter { it.isNotBlank() }
                    .map { it.toInt() })
            } else {
                for ((index, char) in line.withIndex()) {
                    if (char.isLetter()) {
                        val numOfColumn = (index - 1) / 4

                        boxes.add(Box(char, numOfColumn + 1))
                    }
                }
            }
        }

        val mappedMoves = moves.map {
            val scanner = Scanner(it)

            scanner.next()
            val num = scanner.nextInt()
            scanner.next()
            val from = scanner.nextInt()
            scanner.next()
            val to = scanner.nextInt()

            Move(num, from, to)
        }

        val stacks: List<MutableList<Box>> = List(columns.size) { ind ->
            boxes.filter { it.column == (ind + 1) }.toMutableList()
        }

        for (move in mappedMoves) {
            var moved = 0

            while (move.num > moved) {
                stacks[move.to - 1].add(0, stacks[move.from - 1].removeFirst())

                moved++
            }
        }

        return stacks.joinToString("") { it.firstOrNull()?.id.toString() }
    }

    fun part2(input: List<String>): String {
        val splitIndex = input.withIndex().first { it.value.isBlank() }.index

        val configuration = input.subList(0, splitIndex)
        val moves = input.subList(splitIndex + 1, input.size)

        val columns = mutableListOf<Int>()
        val boxes = mutableListOf<Box>()

        for (line in configuration) {
            if (!line.contains('[') || !line.contains(']')) {
                columns.addAll(line.filter { !it.isWhitespace() }.split("").filter { it.isNotBlank() }
                    .map { it.toInt() })
            } else {
                for ((index, char) in line.withIndex()) {
                    if (char.isLetter()) {
                        val numOfColumn = (index - 1) / 4

                        boxes.add(Box(char, numOfColumn + 1))
                    }
                }
            }
        }

        val mappedMoves = moves.map {
            val scanner = Scanner(it)

            scanner.next()
            val num = scanner.nextInt()
            scanner.next()
            val from = scanner.nextInt()
            scanner.next()
            val to = scanner.nextInt()

            Move(num, from, to)
        }

        val stacks: List<MutableList<Box>> = List(columns.size) { ind ->
            boxes.filter { it.column == (ind + 1) }.toMutableList()
        }

        for (move in mappedMoves) {
            var moved = 0

            while (move.num > moved) {
                stacks[move.to - 1].add(moved, stacks[move.from - 1].removeFirst())

                moved++
            }
        }

        return stacks.joinToString("") { it.firstOrNull()?.id.toString() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}

data class Box(val id: Char, val column: Int) {
    override fun toString(): String {
        return "$id"
    }
}

data class Move(val num: Int, val from: Int, val to: Int)