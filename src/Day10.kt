import java.util.*

fun main() {
    fun part1(input: List<String>): Int {
        val state = mutableListOf(1)

        for (line in input) {
            val scan = Scanner(line)

            val op = scan.next()

            when (op) {
                "noop" -> state.add(0)
                "addx" -> {
                    state.add(0)
                    state.add(scan.nextInt())
                }
            }
        }

        var sum = 0

        for (i in 20..220 step 40) {
            sum += i * state.subList(0, i).sum()
        }

        return sum
    }

    fun part2(input: List<String>): String {

        val state = mutableListOf(1)

        for (line in input) {
            val scan = Scanner(line)

            val op = scan.next()

            when (op) {
                "noop" -> state.add(0)
                "addx" -> {
                    state.add(0)
                    state.add(scan.nextInt())
                }
            }
        }

        var lines = ""

        for (i in 0..220 step 40) {
            var display = ""
            for (cycle in i..i + 39) {
                val valueAt = state.subList(0, cycle + 1).sum()
                if (cycle - i == valueAt || cycle - i == valueAt + 1 || cycle - i == valueAt - 1) {
                    display += "*"
                } else {
                    //For clarity
                    display += " "
                }
            }
            lines += display + "\n"
        }

        return lines
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    part1(testInput).also { println(it); check(it == 13140) }

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}