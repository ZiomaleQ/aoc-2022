fun main() {
    fun part1(input: List<String>): Int {
        var score = 0

        for (line in input) {
            val enemyMove = toEnum(line[0])
            val myMove = toEnum(line[2])

            score += (myMove.ordinal + 1) + isWin(enemyMove, myMove)
        }

        return score
    }

    fun part2(input: List<String>): Int {
        var score = 0

        for (line in input) {
            val enemyMove = toEnum(line[0])
            val outcome = toOutcome(line[2])
            val myMove = getMove(enemyMove, outcome)

            score += (myMove.ordinal + 1) + isWin(enemyMove, myMove)
        }

        return score
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}

/* 6 for the win, 3 for the draw, 0 for lose */
fun isWin(left: Moves, right: Moves): Int {
    if (left == right) return 3

    if (left == Moves.SCISSORS && right == Moves.PAPER) return 0

    if (left == Moves.SCISSORS && right == Moves.ROCK) return 6

    if (left == Moves.ROCK && right == Moves.PAPER) return 6

    if (left == Moves.ROCK && right == Moves.SCISSORS) return 0

    if (left == Moves.PAPER && right == Moves.SCISSORS) return 6

    if (left == Moves.PAPER && right == Moves.ROCK) return 0

    return 0
}

fun getMove(left: Moves, outcome: Outcome): Moves {
    if (outcome == Outcome.DRAW) return left

    if (outcome == Outcome.LOSE) {

        if (left == Moves.SCISSORS) return Moves.PAPER
        if (left == Moves.ROCK) return Moves.SCISSORS
        if (left == Moves.PAPER) return Moves.ROCK

    }

    if (outcome == Outcome.WIN) {
        if (left == Moves.SCISSORS) return Moves.ROCK
        if (left == Moves.ROCK) return Moves.PAPER
        if (left == Moves.PAPER) return Moves.SCISSORS
    }

    return Moves.ROCK
}

//[A, B, C, X, Y, Z]
//[65, 66, 67, 88, 89, 90]
fun toEnum(char: Char): Moves = when (char.code - 65 - if (char.code > 88) 23 else 0) {
    0 -> Moves.ROCK
    1 -> Moves.PAPER
    2 -> Moves.SCISSORS

    //Impossible anyway
    else -> Moves.ROCK
}

fun toOutcome(char: Char): Outcome = when (char.code - 88) {
    0 -> Outcome.LOSE
    1 -> Outcome.DRAW
    2 -> Outcome.WIN

    //Impossible anyway
    else -> Outcome.LOSE
}

enum class Moves {
    ROCK, PAPER, SCISSORS
}

enum class Outcome {
    DRAW, LOSE, WIN
}