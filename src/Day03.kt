fun main() {
    fun part1(input: List<String>): Int {
        var sum = 0

        for (line in input) {
            val splitIndex = line.length / 2

            val firstSection = line.substring(0, splitIndex)
            val secondSection = line.substring(splitIndex)

            val dupes = firstSection.filter { secondSection.contains(it) }.toSet()

            val score = dupes.map { if (it.isLowerCase()) it.code - 96 else it.code - 38 }.sum()

            sum += score
        }

        return sum
    }

    //    println(('A'..'Z').map{it.code - 38})
    //    println(('a'..'z').map{it.code - 96})

    fun part2(input: List<String>): Int {
        var sum = 0

        val list = mutableListOf<String>()

        for (line in input) {
            list.add(line)
            if (list.size == 3) {
                val dupes = list[0].filter { list[1].contains(it) && list[2].contains(it) }.toSet()

                sum += dupes.sumOf { if (it.isLowerCase()) it.code - 96 else it.code - 38 }.also { list.clear() }
            }
        }

        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}