package day06

import java.io.File

private class VectorsIterator : Iterator<Pair<Int, Int>> {
    private val sequence = listOf(0 to -1, 1 to 0, 0 to 1, -1 to 0)
    private var index = 0
    override fun hasNext(): Boolean = true
    override fun next(): Pair<Int, Int> {
        if (index == sequence.size) {
            index = 0
        }
        return sequence[index++]
    }
}

private fun partOne(startX: Int, startY: Int, map: Map<Pair<Int, Int>, Char>): Set<Pair<Int, Int>> {
    val visited = mutableSetOf<Pair<Int, Int>>()
    visited.add(startX to startY)
    val vectors = VectorsIterator()
    var vector = vectors.next()
    var x = startX
    var y = startY
    while (true) {
        val nx = x + vector.first
        val ny = y + vector.second
        if (!map.containsKey(nx to ny)) {
            break
        }
        if (map[nx to ny] != '.') {
            vector = vectors.next()
        } else {
            x = nx
            y = ny
            visited.add(x to y)
        }
    }
    println(visited.size)
    return visited
}

private fun partTwo(startX: Int, startY: Int, map: Map<Pair<Int, Int>, Char>, path: Set<Pair<Int, Int>>) {
    var counter = 0
    val potentialPositions = map.entries.asSequence()
        .filter { path.contains(it.key) }
        .filter { it.key != startX to startY }
        .filter { it.value == '.' }
        .toList()
    outer@ for (entry in potentialPositions) {
        val hackedMap = map.toMutableMap()
        hackedMap[entry.key] = '#'
        val visited = mutableSetOf<Triple<Int, Int, Pair<Int, Int>>>()
        val vectors = VectorsIterator()
        var vector = vectors.next()
        visited.add(Triple(startX, startY, vector))
        var x = startX
        var y = startY
        while (true) {
            val nx = x + vector.first
            val ny = y + vector.second
            if (!hackedMap.containsKey(nx to ny)) {
                continue@outer
            }
            if (hackedMap[nx to ny] != '.') {
                vector = vectors.next()
            } else {
                x = nx
                y = ny
                if (visited.contains(Triple(x, y, vector))) {
                    counter++
                    continue@outer
                }
                visited.add(Triple(x, y, vector))
            }
        }
    }
    println(counter)
}

fun main() {
    val input = File("src/day06/input.txt").readLines()
        .map { line -> line.toCharArray() }
    val map = mutableMapOf<Pair<Int, Int>, Char>()
    var startX = -1
    var startY = -1
    input.forEachIndexed { iy, chars ->
        chars.forEachIndexed { ix, char ->
            map[ix to iy] = char
            if (char == '^') {
                startX = ix
                startY = iy
                map[ix to iy] = '.'
            }
        }
    }
    val path = partOne(startX, startY, map)
    partTwo(startX, startY, map, path)
}