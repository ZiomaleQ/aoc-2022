import kotlin.math.min

fun main() {
    fun part1(input: List<String>): Int {

        val lines = input.flatMap {
            it.split("->")
                .map { segment ->
                    segment.trim().split(',').let { numbers -> Cords(numbers[0].toInt(), numbers[1].toInt()) }
                }
                .toMutableList().groupEvery()
        }

        val points = List(1000) { BooleanArray(500) { false } }

        for (drawLine in lines) {
            val start = drawLine[0]
            val end = drawLine[1]

            //Find same axis
            when (start.axis(end)) {
                Axis.EQ -> points[start.x][start.y] = true
                Axis.X -> {
                    val x = start.x

                    val startingY = start.y
                    val endingY = end.y


                    val stepDown = min(startingY, endingY) == endingY
                    val range = if (stepDown) endingY..startingY else startingY..endingY

                    for (y in range) {
                        points[x][y] = true
                    }
                }

                Axis.Y -> {
                    val y = start.y

                    val startingX = start.x
                    val endingX = end.x

                    val stepDown = min(startingX, endingX) == endingX
                    val range = if (stepDown) endingX..startingX else startingX..endingX

                    for (x in range) {
                        points[x][y] = true
                    }
                }

                Axis.None -> println("Welp fuck")
            }

        }

        return simulateSand(points)
    }

    fun part2(input: List<String>): Int {

        val lines = input.flatMap {
            it.split("->")
                .map { segment ->
                    segment.trim().split(',').let { numbers -> Cords(numbers[0].toInt(), numbers[1].toInt()) }
                }
                .toMutableList().groupEvery()
        }

        val highestY = lines.flatten().maxOf { it.y } + 2

        val points = List(1000) { BooleanArray(500) { it == highestY } }

        for (drawLine in lines) {
            val start = drawLine[0]
            val end = drawLine[1]

            //Find same axis
            when (start.axis(end)) {
                Axis.EQ -> points[start.x][start.y] = true
                Axis.X -> {
                    val x = start.x

                    val startingY = start.y
                    val endingY = end.y


                    val stepDown = min(startingY, endingY) == endingY
                    val range = if (stepDown) endingY..startingY else startingY..endingY

                    for (y in range) {
                        points[x][y] = true
                    }
                }

                Axis.Y -> {
                    val y = start.y

                    val startingX = start.x
                    val endingX = end.x

                    val stepDown = min(startingX, endingX) == endingX
                    val range = if (stepDown) endingX..startingX else startingX..endingX

                    for (x in range) {
                        points[x][y] = true
                    }
                }

                Axis.None -> println("Welp fuck")
            }

        }

        //Add one to include starting point
        return simulateSand2(points) + 1
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    part1(testInput).also { println(it); check(it == 24) }
    part2(testInput).also { println(it);check(it == 93) }

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}

private fun Cords.axis(other: Cords): Axis = when {
    this.x == other.x && this.y == other.y -> Axis.EQ
    this.x == other.x -> Axis.X
    this.y == other.y -> Axis.Y
    else -> Axis.None
}

private fun simulateSand(points: List<BooleanArray>): Int {
    var count = 0

    while (nextPosition(points) != null) {

//        if (count % 5 == 0) {
//
//            println(count)
//
//            for (line in points.subList(496, 505)) {
//                println(line.joinToString("") { if (it) "*" else " " })
//            }
//
//            println()
//        }

        count++
    }

    return count
}


private fun simulateSand2(points: List<BooleanArray>): Int {
    var count = 0

    while (nextPosition(points) != Cords(500, 0)) {
        count++
    }

    return count
}

private fun nextPosition(points: List<BooleanArray>): Cords? {
    var x = 500
    var y = 0

    while (true) {

        try {
            if (!points[x][y + 1]) {
                y++
            } else {
                if (!points[x - 1][y + 1]) {
                    x--
                    y++
                } else {
                    if (!points[x + 1][y + 1]) {
                        x++
                        y++
                    } else {
                        points[x][y] = true
                        return Cords(x, y)
                    }
                }
            }
        } catch (err: IndexOutOfBoundsException) {
            return null
        }

    }
}