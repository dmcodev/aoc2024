package day08

import java.io.File

fun main() {
    val lines = File("src/day08/input.txt").readLines().map { it.toCharArray() }
    val maxX = lines[0].size - 1
    val maxY = lines.size - 1
    val map = mutableMapOf<Pair<Int, Int>, Char>()
    lines.forEachIndexed { iy, chars ->
        chars.forEachIndexed { ix, char ->
            map[ix to iy] = char
        }
    }

    val groups = map.entries.groupBy({ it.value }, { it.key })
        .filter { it.key in 'a'..'z' || it.key in 'A'..'Z' || it.key in '0'..'9' }
    val linePatterns = mutableListOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()
    groups.forEach { (_, positions) ->
        for (i in 0..< positions.size - 1) {
            for (j in (i + 1)..< positions.size) {
                val p1 = positions[i]
                val p2 = positions[j]
                val dx = p2.first - p1.first
                val dy = p2.second - p1.second
                linePatterns.add(p2 to (dx to dy))
                linePatterns.add(p1 to (-dx to -dy))
            }
        }
    }

    fun partOne() {
        val syncPoints = mutableSetOf<Pair<Int, Int>>()
        linePatterns.forEach { (point, vector) ->
            val syncPoint = point.first + vector.first to point.second + vector.second
            if (syncPoint.first in 0 .. maxX && syncPoint.second in 0 .. maxY) {
                syncPoints.add(syncPoint)
            }
        }
        println(syncPoints.size)
    }

    fun partTwo() {
        val syncPoints = mutableSetOf<Pair<Int, Int>>()
        linePatterns.forEach { (point, vector) ->
            syncPoints.add(point)
            var previousPoint = point
            while (true) {
                val syncPoint = previousPoint.first + vector.first to previousPoint.second + vector.second
                if (syncPoint.first in 0 .. maxX && syncPoint.second in 0 .. maxY) {
                    syncPoints.add(syncPoint)
                    previousPoint = syncPoint
                } else break
            }
        }
        println(syncPoints.size)
    }

    partOne()
    partTwo()
}