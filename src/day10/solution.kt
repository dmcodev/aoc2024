package day10

import java.io.File

fun main() {
    val lines = File("src/day10/input.txt").readLines().map { it.toCharArray() }
    val map = mutableMapOf<Pair<Int, Int>, Int>()
    val startPositions = mutableSetOf<Pair<Int, Int>>()
    lines.forEachIndexed { iy, chars ->
        chars.forEachIndexed { ix, char ->
            if (char.isDigit()) {
                val number = char.digitToInt()
                map[ix to iy] = number
                if (number == 0) {
                    startPositions.add(ix to iy)
                }
            }
        }
    }

    val vectors = listOf(0 to -1, 1 to 0, 0 to 1, -1 to 0)

    fun solve(position: Pair<Int, Int>): Pair<Int, Set<Pair<Int, Int>>> {
        val value = map.getValue(position)
        if (value == 9) {
            return 1 to setOf(position)
        }
        return vectors.asSequence()
            .map { (position.first + it.first) to (position.second + it.second) }
            .filter { map.containsKey(it) }
            .filter { map.getValue(it) - value == 1 }
            .map { solve(it) }
            .ifEmpty { sequenceOf(0 to emptySet()) }
            .reduce { left, right -> left.first + right.first to left.second + right.second }
    }

    fun partOne() {
        println(
            startPositions.sumOf { solve(it).second.size }
        )
    }

    fun partTwo() {
        println(
            startPositions.sumOf { solve(it).first }
        )
    }

    partOne()
    partTwo()
}