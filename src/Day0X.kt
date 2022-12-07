fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day0X_test")
    part1(testInput).also { println(it); check(it == 1) }

    val input = readInput("Day0X")
    println(part1(input))
    println(part2(input))
}