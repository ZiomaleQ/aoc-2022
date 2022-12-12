fun main() {
    fun part1(input: List<String>): Int {

        var sum = 0

        val trees = input.map { line ->
            line.map { c ->
                Tree(c.digitToInt())
            }
        }

        for ((rowIndex, rowOfTrees) in trees.withIndex()) {
            if (rowIndex == 0 || rowIndex == trees.size - 1) {
                sum += rowOfTrees.size
                continue
            }

            for ((treeIndex, tree) in rowOfTrees.withIndex()) {
                if (treeIndex == 0 || treeIndex == rowOfTrees.size - 1) {
                    sum++
                    continue
                }

                val treesToLeft = rowOfTrees.subList(0, treeIndex)
                val treesToRight = rowOfTrees.subList(treeIndex + 1, rowOfTrees.size)
                val treesToTop = trees.subList(0, rowIndex).map { it[treeIndex] }
                val treesToBottom = trees.subList(rowIndex + 1, trees.size).map { it[treeIndex] }

                val isVisibleHorizontal =
                    treesToLeft.all { it.height < tree.height } || treesToRight.all { it.height < tree.height }
                val isVisibleVertical =
                    treesToTop.all { it.height < tree.height } || treesToBottom.all { it.height < tree.height }

                if (isVisibleVertical || isVisibleHorizontal) {
                    sum++
                }
            }
        }

        return sum
    }

    fun part2(input: List<String>): Int {
        val trees = input.map { line ->
            line.map { c ->
                Tree(c.digitToInt())
            }
        }.also { setScore(it) }

        return trees.flatten().maxOf { it.score }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    part1(testInput).also { println(it); check(it == 21) }
    part2(testInput).also { println(it); check(it == 8) }

    val input = readInput("Day08")

    println(part1(input))
    println(part2(input))
}

data class Tree(val height: Int, var score: Int = 0)

fun setScore(map: List<List<Tree>>) = map.forEachIndexed { y, row ->
    row.forEachIndexed { x, tree ->
        tree.score = scenicScore(map, x, y)
    }
}

fun scenicScore(map: List<List<Tree>>, x: Int, y: Int): Int {
    val currentHeight = map[y][x].height
    val directions = listOf(
        (x + 1..map[0].lastIndex).map { map[y][it].height },
        (x - 1 downTo 0).map { map[y][it].height },
        (y + 1..map.lastIndex).map { map[it][x].height },
        (y - 1 downTo 0).map { map[it][x].height }
    )

    return directions
        .map { dir ->
            (dir.indexOfFirst { it >= currentHeight } + 1)
                .takeIf { it != 0 }
                ?: dir.size
        }
        .reduce(Int::times)
}
