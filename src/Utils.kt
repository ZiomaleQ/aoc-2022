import java.io.File
/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt")
    .readLines()

data class Cords(val x: Int, val y: Int)

enum class Axis {
    X, Y, EQ, None
}

fun <T> List<T>.groupEvery(num: Int = 2): MutableList<List<T>> {
    val lastIndex = this.size - num

    return (0..lastIndex).map { this.subList(it, it + num) }.toMutableList()
}