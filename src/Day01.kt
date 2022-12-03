fun main() {
    fun part1(input: List<String>): Int {

        val elves = mutableListOf<Int>()

        var currentElf = 0

        for (line in input) {
            if (line.isEmpty()) elves.add(currentElf).also { currentElf = 0 }
            else {
                currentElf += line.toInt()

            }
        }

        return elves.max()
    }

    fun part2(input: List<String>): Int {

        val elves = mutableListOf<Int>()

        var currentElf = 0

        for (line in input) {
            if (line.isEmpty()) elves.add(currentElf).also { currentElf = 0 }
            else {
                currentElf += line.toInt()

            }
        }

        return elves.sorted().takeLast(3).sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}