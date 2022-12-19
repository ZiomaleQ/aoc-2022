import java.util.Scanner
import kotlin.math.pow
import kotlin.math.sqrt

fun main() {
    fun part1(input: List<String>): Int {

        val tail = Node.Tail(0, 0)
        val head = Node.Head(0, 0)

        val positions = mutableListOf<Pair<Int, Int>>()

        for (line in input) {
            val move = Scanner(line).let { HeadMove(HeadMoveDirection(it.next()[0]), it.nextInt()) }

            while (move.value > 0) {
                head.move(move)
                tail.catchUp(head.x, head.y).also { positions.add(it) }
                move.value--
            }
        }

        positions.add(Pair(tail.x, tail.y))

        return positions.toSet().size
    }

    fun part2(input: List<String>): Int {

        val nodes = listOf(
            Node.Head(0, 0),
            Node.Tail(0, 0),
            Node.Tail(0, 0),
            Node.Tail(0, 0),
            Node.Tail(0, 0),
            Node.Tail(0, 0),
            Node.Tail(0, 0),
            Node.Tail(0, 0),
            Node.Tail(0, 0),
            Node.Tail(0, 0),
        )

        val positions = mutableListOf<Pair<Int, Int>>()

        for (line in input) {
            val move = Scanner(line).let { HeadMove(HeadMoveDirection(it.next()[0]), it.nextInt()) }

            while (move.value > 0) {

                (nodes[0] as Node.Head).move(move)

                var lastNode = nodes[0]

                for (index in nodes.indices) {
                    if(index == 0) continue

                    (nodes[index] as Node.Tail).catchUp(lastNode.x, lastNode.y).let { if (index == 9) positions.add(it) }

                    lastNode = nodes[index]
                }

                move.value--
            }
        }

        positions.add(Pair(nodes[9].x, nodes[9].y))

        return positions.toSet().size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
//    part1(testInput).also { println(it); check(it == 13) }
    part2(testInput).also { println(it); check(it == 36) }

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}

data class HeadMove(val direction: HeadMoveDirection, var value: Int)

private sealed class Node(var x: Int, var y: Int) {

    class Tail(x: Int, y: Int) : Node(x, y) {
        fun catchUp(otherX: Int, otherY: Int): Pair<Int, Int> {
            val oldCords = Pair(x, y)

            val distance = sqrt((x - otherX).toDouble().pow(2) + (y - otherY).toDouble().pow(2))

            if (x != otherX && y != otherY) {

                if (distance < 2) return oldCords

                val nextX = if (x < otherX) x + 1 else x - 1
                val nextY = if (y < otherY) y + 1 else y - 1

                x = nextX
                y = nextY

                return oldCords
            }
            return if (x != otherX) {

                if (distance < 2) return oldCords

                val nextX = if (x < otherX) x + 1 else x - 1

                x = nextX

                oldCords
            } else {
                if (distance < 2) return oldCords

                val nextY = if (y < otherY) y + 1 else y - 1

                y = nextY

                oldCords
            }
        }
    }

    class Head(x: Int, y: Int) : Node(x, y) {
        fun move(move: HeadMove) = when (move.direction) {
            HeadMoveDirection.LEFT -> x -= 1
            HeadMoveDirection.RIGHT -> x += 1
            HeadMoveDirection.DOWN -> y -= 1
            HeadMoveDirection.UP -> y += 1
        }
    }
}

enum class HeadMoveDirection {
    RIGHT, LEFT, UP, DOWN
}

fun HeadMoveDirection(ch: Char): HeadMoveDirection = when (ch) {
    'R' -> HeadMoveDirection.RIGHT
    'L' -> HeadMoveDirection.LEFT
    'U' -> HeadMoveDirection.UP
    'D' -> HeadMoveDirection.DOWN

    //Will not happen
    else -> HeadMoveDirection.RIGHT
}