package day02

import java.io.File
import kotlin.math.sign

fun main() {
    val input = File("src/day02/input.txt").readLines(charset = Charsets.UTF_8)
    println(
        input.filter { it.numbers().isCorrect() }.count()
    )
    println(
        input.filter { line -> line.numbers().permutations().any { it.isCorrect() } }.count()
    )
}

private fun Iterable<Int>.isCorrect(): Boolean {
    val iterator = iterator()
    var diffSign: Int? = null
    var previous = iterator.next()
    while (iterator.hasNext()) {
        val next = iterator.next()
        val diff = next - previous
        previous = next
        if (diffSign == null) {
            diffSign = diff.sign
        } else if (diffSign != diff.sign) {
            return false
        }
        if (diffSign == 0) return false
        if (diffSign < 0 && diff !in -3 .. -1) return false
        if (diffSign > 0 && diff !in 1 .. 3) return false
    }
    return true
}

private fun List<Int>.permutations(): Sequence<Iterable<Int>> {
    var omittedIndex = 0
    return sequenceOf(this) + generateSequence {
        if (omittedIndex < size) omitIndex(omittedIndex).also { omittedIndex++ } else null
    }
}

private fun Iterable<Int>.omitIndex(omittedIndex: Int): Iterable<Int> {
    val iterator = iterator()
    return object : Iterable<Int> {
        override fun iterator(): Iterator<Int> {
            return object : Iterator<Int> {
                var index = 0
                override fun hasNext(): Boolean {
                    if (index == omittedIndex) {
                        iterator.next()
                        index++
                    }
                    return iterator.hasNext()
                }
                override fun next(): Int {
                    if (index == omittedIndex) {
                        iterator.next()
                        index++
                    }
                    index++
                    return iterator.next()
                }
            }
        }
    }
}

private fun String.numbers(): List<Int> = split("\\s+".toRegex()).map { it.toInt() }