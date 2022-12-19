import java.util.PriorityQueue

fun main() {
    fun part1(input: List<String>): Int {

        var start = Cords(0, 0)
        var end = Cords(0, 0)

        val heatmap = input.flatMapIndexed { y, row ->
            row.mapIndexed { x, elt ->

                val here = Cords(x, y)

                here to when (elt) {
                    'E' -> 25.also { end = Cords(x, y) }
                    'S' -> 0.also { start = Cords(x, y) }
                    else -> elt - 'a'
                }
            }
        }.toMap()

        val grid = Grid(input)

        return grid.findCost(
            start,
            isEnd = { it == end },
            canMove = { from, to -> heatmap[to]!! - heatmap[from]!! <= 1 },
        )
    }

    fun part2(input: List<String>): Int {
        var end = Cords(0, 0)

        val heatmap = input.flatMapIndexed { y, row ->
            row.mapIndexed { x, elt ->

                val here = Cords(x, y)

                here to when (elt) {
                    'E' -> 25.also { end = Cords(x, y) }
//                    'S' -> 0.also { start = Cords(x, y) }
                    else -> elt - 'a'
                }
            }
        }.toMap()

        val grid = Grid(input)

        return grid.findCost(
            end,
            isEnd = { heatmap[it] == 0 },
            canMove = { from, to -> heatmap[from]!! - heatmap[to]!! <= 1 },
        )
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    part1(testInput).also { println(it); check(it == 31) }

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}

private data class Cords(val x: Int, val y: Int) {
    fun checkBoundary(height: Int, width: Int) = this.x in 0 until width && this.y in 0 until height
}

private class Grid(input: List<String>) {
    val height = input.size
    val width = input[0].length

    val visited = List(width) { BooleanArray(height) { false } }

    fun findCost(
        start: Cords,
        isEnd: (Cords) -> Boolean,
        canMove: (Cords, Cords) -> Boolean
    ): Int {

        val queue = PriorityQueue<PathCost>().apply { add(PathCost(start, 0)) }

        while (true) {
            val latest = queue.poll()

            val cord = latest.cord

            if (visited[cord.x][cord.y]) {
                continue
            } else {
                visited[cord.x][cord.y] = true
            }

            val segmentsToMove = listOf(
                Cords(cord.x + 1, cord.y),
                Cords(cord.x - 1, cord.y),
                Cords(cord.x, cord.y + 1),
                Cords(cord.x, cord.y - 1)
            )
                .filter { it.checkBoundary(height, width) }
                .filter { canMove(cord, it) }

            if (segmentsToMove.any(isEnd)) {
                return latest.cost + 1
            }

            queue.addAll(segmentsToMove.map { PathCost(it, latest.cost + 1) })
        }
    }

    data class PathCost(val cord: Cords, val cost: Int) : Comparable<PathCost> {
        override fun compareTo(other: PathCost): Int =
            this.cost.compareTo(other.cost)
    }
}