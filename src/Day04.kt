fun main() {
    fun part1(input: List<String>): Int {
        var summary = 0

        for (line in input) {
            val (first, second) = line.split(',').let { Pair(it[0], it[1]) }

            val firstRange = first.split('-').let { it[0].toInt()..it[1].toInt() }
            val secondRange = second.split('-').let { it[0].toInt()..it[1].toInt() }

            val isInFirst = firstRange.fold(true) { sum, curr -> curr in secondRange && sum }
            val isInSecond = secondRange.fold(true) { sum, curr -> curr in firstRange && sum }
            if (isInFirst || isInSecond) {
                summary++
            }
        }

        return summary
    }

    fun part2(input: List<String>): Int {
        var summary = 0

        for (line in input) {
            val (first, second) = line.split(',').let { Pair(it[0], it[1]) }

            val firstRange = first.split('-').let { it[0].toInt()..it[1].toInt() }
            val secondRange = second.split('-').let { it[0].toInt()..it[1].toInt() }

            val isThere = firstRange.any { secondRange.contains(it) }

            if (isThere) {
                summary++
            }
        }

        return summary
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}