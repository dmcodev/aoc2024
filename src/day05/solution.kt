package day05

import java.io.File
import java.util.*
import kotlin.Comparator

fun main() {
    val input = File("src/day05/input.txt").readText(charset = Charsets.UTF_8)
        .lines()
    val dividerIndex = input.indexOf("")
    val rules = TreeMap<Int, MutableSet<Int>>()
    input.subList(0, dividerIndex).map { it.split('|') }.forEach {
        val (left, right) = it.map(String::toInt)
        rules.computeIfAbsent(left) { mutableSetOf() }.add(right)
    }
    val order = object : Comparator<Int> {
        override fun compare(first: Int, second: Int): Int {
            val firstRule = rules[first]
            if (firstRule != null && firstRule.contains(second)) {
                return -1
            }
            val secondRule = rules[second]
            if (secondRule != null && secondRule.contains(first)) {
                return 1
            }
            error("Order not complete for: $first / $second")
        }
    }
    val correct = mutableListOf<List<Int>>()
    val incorrect = mutableListOf<List<Int>>()
    input.subList(dividerIndex + 1, input.size).forEach { line ->
        val numbers = line.split(',').map { it.toInt() }
        val sortedNumbers = numbers.sortedWith(order)
        if (numbers == sortedNumbers) {
            correct.add(numbers)
        } else {
            incorrect.add(numbers)
        }
    }
    println(
        correct.sumOf { it[it.size / 2] }
    )
    println(
        incorrect.map { it.sortedWith(order) }.sumOf { it[it.size / 2] }
    )
}

