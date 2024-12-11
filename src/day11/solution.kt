package day11

import java.io.File

fun main() {
    val numbers = File("src/day11/input.txt").readLines().first().split(" ").map { it.toLong() }
    val results = mutableMapOf<Pair<Long, Int>, Long>()

    fun solve(number: Long, cycles: Int): Long {
        if (cycles == 0) {
            return 1
        }
        val resultKey = number to cycles
        results[resultKey]?.let { return it }
        if (number == 0L) {
            val result = solve(1, cycles - 1)
            results[resultKey] = result
            return result
        } else {
            val digits = number.toString()
            if (digits.length % 2 == 0) {
                val leftNumber = digits.substring(0, digits.length / 2).toLong()
                val rightNumber = digits.substring(digits.length / 2)
                    .replace("^0+".toRegex(), "")
                    .ifEmpty { "0" }
                    .toLong()
                val result = solve(leftNumber, cycles - 1) + solve(rightNumber, cycles - 1)
                results[resultKey] = result
                return result
            } else {
                val result = solve(number * 2024, cycles - 1)
                results[resultKey] = result
                return result
            }
        }
    }

    fun partOne() {
        println(
            numbers.sumOf { solve(it, 25) }
        )
    }

    fun partTwo() {
        println(
            numbers.sumOf { solve(it, 75) }
        )
    }

    partOne()
    partTwo()
}