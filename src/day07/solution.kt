package day07

import java.io.File

private fun solve(left: Long, right: List<Long>, index: Int, sum: Long, operators: List<(Long, Long) -> Long>): Boolean {
    if (index == right.size) {
        return sum == left
    }
    if (index == 0) {
        return solve(left, right, index + 1, right[index], operators)
    }
    return operators.asSequence()
        .map { operator -> solve(left, right, index + 1, operator(sum, right[index]), operators) }
        .reduce(Boolean::or)
}

private infix fun Long.concat(other: Long) = (toString() + other.toString()).toLong()

private fun partOne(input: List<Pair<Long, List<Long>>>) {
    val operators: List<(Long, Long) -> Long> = listOf(Long::plus, Long::times)
    println(
        input.asSequence()
            .filter { solve(it.first, it.second, 0, 0, operators) }
            .sumOf { it.first }
    )
}

private fun partTwo(input: List<Pair<Long, List<Long>>>) {
    val operators: List<(Long, Long) -> Long> = listOf(Long::plus, Long::times, Long::concat)
    println(
        input.asSequence()
            .filter { solve(it.first, it.second, 0, 0, operators) }
            .sumOf { it.first }
    )
}

fun main() {
    val input = File("src/day07/input.txt").readLines().map { line ->
        val left = line.substringBefore(':').toLong()
        val right = line.substringAfter(": ").split("\\s+".toRegex())
            .map { it.toLong() }
        left to right
    }
    partOne(input)
    partTwo(input)
}