package day03

import java.io.File

fun main() {
    val input = File("src/day03/input.txt").readText(charset = Charsets.UTF_8)
    val mulRegex = Regex("mul\\(([1-9][0-9]{0,2}),([1-9][0-9]{0,2})\\)", RegexOption.MULTILINE)
    println(
        mulRegex.findAll(input).sumOf { it.groupValues[1].toLong() * it.groupValues[2].toLong() }
    )
    val doRegex = Regex("do\\(\\)", RegexOption.MULTILINE)
    val dontRegex = Regex("don't\\(\\)", RegexOption.MULTILINE)
    val instructions = doRegex.findAll(input).map { it.range.first to Instruction.Start } +
            dontRegex.findAll(input).map { it.range.first to Instruction.Stop } +
            mulRegex.findAll(input).map { it.range.first to Instruction.Number(it.groupValues[1].toLong() * it.groupValues[2].toLong()) }
    var enabled = true
    var sum = 0L
    instructions.sortedBy { it.first }.map { it.second }.forEach {
        when(it) {
            is Instruction.Start -> enabled = true
            is Instruction.Stop -> enabled = false
            is Instruction.Number -> if (enabled) { sum += it.value }
        }
    }
    println(sum)
}

private sealed class Instruction {
    data object Start: Instruction()
    data object Stop: Instruction()
    data class Number(val value: Long): Instruction()
}