import java.math.BigInteger

fun main() {
    fun part1(input: List<String>): Long {
        val monkeys = mutableListOf<Monkey>()

        val num = magicNumber(input)

        for (i in 0..input.size / 7) {
            monkeys.add(Monkey.parse(input.subList(i * 7, ((i + 1) * 7) - 1)))
        }

        repeat(20) {

            for (monkey in monkeys) {
                val passes = monkey.inspectAll(num)

                for (pass in passes) {
                    monkeys.find { it.id == pass.first }!!.items.add(pass.second)
                }

            }
        }

        return monkeys.map { it.inspections }.sorted().reversed().take(2).reduce(Long::times)
    }

    fun part2(input: List<String>): BigInteger {
        val monkeys = mutableListOf<Monkey>()

        val num = magicNumber(input)

        for (i in 0..input.size / 7) {
            monkeys.add(Monkey.parse(input.subList(i * 7, ((i + 1) * 7) - 1), 1))
        }

        repeat(10000) {

            for (monkey in monkeys) {
                val passes = monkey.inspectAll(num)

                for (pass in passes) {
                    monkeys.find { it.id == pass.first }!!.items.add(pass.second)
                }

            }
        }

        return monkeys.map { BigInteger.valueOf(it.inspections) }.sorted().reversed().take(2).reduce(BigInteger::times)

    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    part1(testInput).also { println(it); check(it == 10605L) }

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}

fun magicNumber(inputs: List<String>): Int {
    var magicNumber = 1
    for (i in 0..inputs.size / 7) {
        val monkeyId = i * 7
        magicNumber *= inputs[monkeyId + 3].substringAfter("Test: divisible by ").toInt()
    }
    return magicNumber
}

data class Monkey(
    val id: Int,
    val items: MutableList<Long>,
    val operation: Pair<Char, Any>,
    val divider: Int,
    val redirectTo: Pair<Int, Int>,
    val boredFactor: Int = 3
) {

    var inspections = 0L

    fun inspectAll(num: Int): List<Pair<Int, Long>> {
        val arr = mutableListOf<Pair<Int, Long>>()

        while (items.isNotEmpty()) {
            arr.add(inspect(num))
        }

        return arr
    }

    //Where, what
    private fun inspect(num: Int): Pair<Int, Long> {

        inspections++
        val item = operate(items.removeFirst()).floorDiv(boredFactor) % num

        return if (item % divider == 0L) {
            Pair(redirectTo.first, item)
        } else {
            Pair(redirectTo.second, item)
        }
    }

    private fun operate(num: Long): Long {
        return when (operation.first) {
            '+' -> num + (if (operation.second is String) num else operation.second as Long)
            '*' -> num * (if (operation.second is String) num else operation.second as Long)
            else -> throw IllegalArgumentException("unknown operator")
        }
    }


    companion object {
        fun parse(input: List<String>, bf: Int = 3): Monkey {
            val id = input[0].filter { it.isDigit() }.toInt()
            val items =
                input[1].substring("  Starting items:".length).split(',').map { it.trim().toLong() }.toMutableList()
            val operation =
                input[2].substring("  Operation: new = old ".length).split(' ')
                    .let { Pair(it[0][0], it[1].trim().let { num -> if (num == "old") "OLD" else num.toLong() }) }
            val divider =
                input[3].substring("  Test: divisible by ".length).trim().toInt()
            val redirectToFirst = input[4].substring("    If true: throw to monkey ".length).trim().toInt()
            val redirectToSecond = input[5].substring("    If false: throw to monkey ".length).trim().toInt()

            return Monkey(id, items, operation, divider, Pair(redirectToFirst, redirectToSecond), bf)
        }
    }
}