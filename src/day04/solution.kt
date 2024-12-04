package day04

import java.io.File

fun main() {
    val map = File("src/day04/input.txt").readText(charset = Charsets.UTF_8)
        .lines().map { it.toCharArray() }.toTypedArray()

    fun findSequence(
        x: Int,
        y: Int,
        seek: CharArray,
        vectors: List<Pair<Int, Int>>,
        onFound: (Pair<Int, Int>) -> Unit
    ) {
        for (vector in vectors) {
            var ey = y
            var ex = x
            var i = 0
            while (ey >= 0 && ey < map.size && ex >= 0 && ex < map[0].size && i < seek.size && map[ey][ex] == seek[i]) {
                ex += vector.first
                ey += vector.second
                i++
            }
            if (i == seek.size) {
                onFound(vector)
            }
        }
    }

    fun partOne() {
        var count = 0
        val vectors = listOf(-1 to 0, -1 to -1, 0 to -1, 1 to -1, 1 to 0, 1 to 1, 0 to 1, -1 to 1,)
        val seek = "XMAS".toCharArray()
        map.indices.forEach { y ->
            map[0].indices.forEach { x ->
                findSequence(x, y, seek, vectors) {
                    count++
                }
            }
        }
        println(count)
    }

    fun partTwo() {
        val intersections = mutableListOf<Pair<Int, Int>>()
        val vectors = listOf(-1 to -1, 1 to -1, 1 to 1, -1 to 1,)
        val seek = "MAS".toCharArray()
        map.indices.forEach { y ->
            map[0].indices.forEach { x ->
                findSequence(x, y, seek, vectors) { vector ->
                    intersections.add(x + vector.first to y + vector.second)
                }
            }
        }
        println(intersections.groupingBy { it }.eachCount().filter { it.value > 1 }.size)
    }

    partOne()
    partTwo()
}

