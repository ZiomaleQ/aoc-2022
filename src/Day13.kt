fun main() {
    fun part1(input: List<String>): Int {

        var sum = 0


        for (i in 0..(input.size / 3)) {

            val leftPacket = Packet.parse(input[(i * 3)])
            val rightPacket = Packet.parse(input[(i * 3) + 1])
            val inOrder = leftPacket.rightOrder(rightPacket)

            if (inOrder) sum += (i + 1)
        }

        return sum
    }

    fun part2(input: List<String>): Int {
        val packets = mutableListOf<Packet>()

        for (i in 0..(input.size / 3)) {
            Packet.parse(input[(i * 3)]).also { packets.add(it) }
            Packet.parse(input[(i * 3) + 1]).also { packets.add(it) }
        }

        val first = Packet.parse("[[2]]").also { packets.add(it) }
        val second = Packet.parse("[[6]]").also { packets.add(it) }

        packets.sort()

        return packets.indexOf(first).inc() * packets.indexOf(second).inc()

    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    part1(testInput).also { println(it); check(it == 13) }
    part2(testInput).also { println(it); check(it == 140) }

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}

private data class Packet(val elements: List<CodeNode>) : Comparable<Packet> {
    fun rightOrder(right: Packet): Boolean {
        for ((index, elt) in elements.withIndex()) {
            val rightElt = right.elements.getOrNull(index) ?: return false

            if (elt is NumberNode && rightElt is NumberNode) {
                if (elt == rightElt) continue

                return elt < rightElt
            } else {
                return if (elt is ArrayNode && rightElt is ArrayNode) {
                    val comp = elt.compare(rightElt)

                    if (comp == 0) continue
                    else comp == -1

                } else {
                    val enclosedLeft = if (elt is NumberNode) ArrayNode(mutableListOf(elt)) else elt
                    val enclosedRight = if (rightElt is NumberNode) ArrayNode(mutableListOf(rightElt)) else rightElt

                    val comp = (enclosedLeft as ArrayNode).compare(enclosedRight as ArrayNode)

                    if (comp == 0) continue
                    else comp == -1
                }
            }
        }

        return true
    }

    override fun compareTo(other: Packet): Int {

        for ((index, elt) in elements.withIndex()) {
            val rightElt = other.elements.getOrNull(index) ?: return 1

            if (elt is NumberNode && rightElt is NumberNode) {
                if (elt == rightElt) continue

                return if (elt < rightElt) -1 else 1
            } else {
                return if (elt is ArrayNode && rightElt is ArrayNode) {
                    val comp = elt.compare(rightElt)

                    if (comp == 0) continue
                    else comp

                } else {
                    val enclosedLeft = if (elt is NumberNode) ArrayNode(mutableListOf(elt)) else elt
                    val enclosedRight = if (rightElt is NumberNode) ArrayNode(mutableListOf(rightElt)) else rightElt

                    val comp = (enclosedLeft as ArrayNode).compare(enclosedRight as ArrayNode)

                    if (comp == 0) continue
                    else comp
                }
            }
        }

        return when (elements.size) {
            other.elements.size -> 0
            else -> elements.size - other.elements.size
        }
    }

    companion object {
        fun parse(string: String): Packet {
            val tokens = Tokenizer(string).tokenizeCode()
            return Packet(
                Parser(tokens).parse().let { if (it[0] is ArrayNode) (it[0] as ArrayNode).nodes else listOf(it[0]) })
        }
    }

    override fun toString() = "[${elements.joinToString()}]"
}

//Taken from parts heh
private class Tokenizer(private val code: String) {
    private var rules: MutableList<Rule> = mutableListOf(
        Rule(true, "OPERATOR", "[],"),
        Rule(false, "NUMBER", "[0-9]"),
    )
    private var current = 0

    fun tokenizeCode(): MutableList<Token> {
        val code = this.code.toCharArray()
        val tokens = mutableListOf<Token>()
        val rulesLocal = rules.groupBy { it.isSingle }
        while (current < code.size) {
            var found = false
            var expr: String

            for (rule in rulesLocal[true] ?: listOf()) {
                if (found) break
                if (rule.rule.toCharArray().find { it == peek() } != null) {
                    found = tokens.add(Token(rule.name, "${peekNext()}", 1))
                    break
                }
            }
            for (rule in rulesLocal[false] ?: listOf()) {
                if (found) break
                if (rule.test(code[current])) {
                    expr = code[current++].toString().also { found = true }
                    while (current < code.size && rule.test(peek())) expr = "$expr${peekNext()}"
                    tokens.add(Token(rule.name, expr, expr.length))
                }
            }
            if (!found) current++
        }
        return tokens.map { it.parse() }.toMutableList()
    }

    private fun peek(): Char = code[current]
    private fun peekNext(): Char = code[current++]

    private data class Rule(var isSingle: Boolean, var name: String, var rule: String) {
        override fun toString(): String = "[ $isSingle, '$name', /$rule/ ]"
        fun test(check: Any): Boolean = rule.toRegex(RegexOption.IGNORE_CASE).matches("$check")
    }
}

private data class Token(var type: String, var value: String, val length: Int) {
    private var parsed: Boolean = false
    override fun toString(): String = "Token of type $type with value $value"
    fun parse(): Token {
        if (parsed) return this
        val operators = mapOf(
            ',' to "COMMA",
            '[' to "LEFT_BRACKET",
            ']' to "RIGHT_BRACKET"
        )
        when (type) {
            "OPERATOR" -> type = operators[value[0]] ?: "ERROR-XD"
        }
        return this.also { parsed = true }
    }
}

private class Parser(private val code: MutableList<Token>) {
    private var lastToken: Token = Token("x", "x", 0)
    private var tokens: MutableList<CodeNode> = mutableListOf()
    fun parse(): MutableList<CodeNode> {
        lastToken = getEOT()
        try {
            while (code.size != 0) {
                tokens.add(primary())
            }
        } catch (err: Error) {
            println(code.take(2))
            println(err.message)
        }
        return tokens
    }

    private fun primary(): CodeNode = when {
        match("LEFT_BRACKET") -> {
            val list = mutableListOf<CodeNode>()
            if (peek().type != "RIGHT_BRACKET") {
                do {
                    list.add(primary())
                } while (match("COMMA"))
            }
            advance()
            ArrayNode(list)
        }

        match("NUMBER", "STRING") -> NumberNode(lastToken.value.toInt())
        else -> error("Expected expression. Got ${peek().value}")
    }

    private fun match(vararg types: String): Boolean = if (peek().type in types) true.also { advance() } else false
    private fun advance(): Token = code.removeFirst().also { lastToken = it }
    private fun error(message: String): Nothing = throw Error(message)
    private fun peek(): Token = if (code.size > 0) code[0] else getEOT()
    private fun getEOT(): Token = Token("EOT", "EOT", 3)
}

private sealed interface CodeNode

private data class ArrayNode(val nodes: MutableList<CodeNode>) : CodeNode {
    //- 1 left side, 0, middle, 1 right side
    fun compare(other: ArrayNode): Int {
        for ((index, leftElt) in nodes.withIndex()) {
            val rightElt = other.nodes.getOrNull(index) ?: return 1

            if (leftElt is NumberNode && rightElt is NumberNode) {
                if (leftElt == rightElt) continue
                else return if (leftElt < rightElt) -1 else 1
            }

            if (leftElt is ArrayNode && rightElt is ArrayNode) {
                val comp = leftElt.compare(rightElt)

                if (comp == 0) continue
                else return comp
            }

            val enclosedLeft = if (leftElt is NumberNode) ArrayNode(mutableListOf(leftElt)) else leftElt
            val enclosedRight = if (rightElt is NumberNode) ArrayNode(mutableListOf(rightElt)) else rightElt

            val comp = (enclosedLeft as ArrayNode).compare(enclosedRight as ArrayNode)

            if (comp == 0) continue
            else return comp
        }

        return when (nodes.size) {
            other.nodes.size -> 0
            else -> nodes.size - other.nodes.size
        }
    }

    override fun toString() = "[${nodes.joinToString()}]"
}

private data class NumberNode(val int: Int) : CodeNode, Comparable<NumberNode> {
    override fun compareTo(other: NumberNode): Int =
        this.int.compareTo(other.int)

    override fun toString() = int.toString()
}