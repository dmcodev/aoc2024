package day01

import java.io.File
import kotlin.math.abs

fun main() {
    val input = File("src/day01/input.txt").readLines(charset = Charsets.UTF_8)
    val pairs = input.map { line -> line.split("\\s+".toRegex()).map { it.toLong() } }
    val left = pairs.map { it[0] }.sorted()
    val right = pairs.map { it[1] }.sorted()
    val rightCounted = right.groupingBy { it }.eachCount()
    println(
        left.zip(right).sumOf { pair -> abs(pair.first - pair.second) }
    )
    println(
        left.sumOf { it * (rightCounted[it] ?: 0) }
    )
}