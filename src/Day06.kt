fun main() {
    fun part1(input: List<String>): Int {
        val inputLine = input[0]

        var index = 0

        while (index + 4 <= inputLine.length) {
            val mark = inputLine.substring(index, index + 4)

            if (mark.toCharArray().toSet().size == 4) {
                return index + 4
            } else {
                index++
            }
        }

        //Fallback
        return input.size
    }

    fun part2(input: List<String>): Int {
        val inputLine = input[0]

        var index = 0

        while (index + 14 <= inputLine.length) {
            val mark = inputLine.substring(index, index + 14)

            if (mark.toCharArray().toSet().size == 14) {
                return index + 14
            } else {
                index++
            }
        }

        //Fallback
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 7)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}