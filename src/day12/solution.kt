package day12

import java.io.File
import java.util.*

private data class Point(val x: Int, val y: Int)

private infix fun Point.moveTowards(direction: Direction) = Point(x + direction.dx, y + direction.dy)

private infix fun Int.point(y: Int): Point = Point(this, y)

private data class Side(val from: Point, val to: Point, val outside: Direction)

private enum class Direction(val dx: Int, val dy: Int) {
    UP(0, -1),
    RIGHT(1, 0),
    DOWN(0, 1),
    LEFT(-1, 0)
}

private data class Area(val points: Set<Point>, val sides: Set<Side>, val continuousSidesNumber: Int)

fun main() {
    val lines = File("src/day12/input.txt").readLines()
    val map = mutableMapOf<Point, Char>()
    lines.forEachIndexed { iy, chars ->
        chars.forEachIndexed { ix, char ->
            map[Point(ix, iy)] = char
        }
    }

    val areas = mutableListOf<Area>()
    val mapPointQueue = LinkedList<Point>()
    val visitedPoints = mutableSetOf<Point>()
    map.forEach { mapPointQueue.add(it.key) }

    while (mapPointQueue.isNotEmpty()) {
        val mapPoint = mapPointQueue.removeFirst()
        if (visitedPoints.contains(mapPoint)) {
            continue
        }
        val areaPoints = mutableSetOf<Point>()
        val areaPointQueue = LinkedList<Point>()
        areaPointQueue.add(mapPoint)
        val areaSides = mutableSetOf<Side>()

        while (areaPointQueue.isNotEmpty()) {
            val areaPoint = areaPointQueue.removeFirst()
            if (visitedPoints.contains(areaPoint)) {
                continue
            }
            val pointType = map.getValue(areaPoint)
            val neighbourPoints = Direction.entries.asSequence()
                .map { it to areaPoint.moveTowards(it) }
                .filter { map.containsKey(it.second) }
                .filter { map.getValue(it.second) == pointType }
                .toMap()
            if (!neighbourPoints.containsKey(Direction.LEFT)) {
                areaSides.add(Side(areaPoint, (areaPoint.x point areaPoint.y + 1), Direction.LEFT))
            }
            if (!neighbourPoints.containsKey(Direction.UP)) {
                areaSides.add(Side(areaPoint, (areaPoint.x + 1 point areaPoint.y), Direction.UP))
            }
            if (!neighbourPoints.containsKey(Direction.RIGHT)) {
                areaSides.add(Side((areaPoint.x + 1 point areaPoint.y), (areaPoint.x + 1 point areaPoint.y + 1), Direction.RIGHT))
            }
            if (!neighbourPoints.containsKey(Direction.DOWN)) {
                areaSides.add(Side((areaPoint.x point areaPoint.y + 1), (areaPoint.x + 1 point areaPoint.y + 1), Direction.DOWN))
            }
            neighbourPoints.values.forEach { areaPointQueue.add(it) }
            areaPoints.add(areaPoint)
            visitedPoints.add(areaPoint)
        }
        var continuousSidesNumber = 0
        fun countContinuousSides(directionSelector: (Side) -> Boolean, coordinateSelector: (Point) -> Int) {
            val matchingSides = areaSides.asSequence()
                .filter { directionSelector(it) }
                .map { coordinateSelector(it.from) to it.outside }
                .sortedBy { it.first }
                .toList()
            var lastCoordinate: Int? = null
            var lastOutside: Direction? = null
            for ((coordinate, outside) in matchingSides) {
                if (coordinate - 1 != lastCoordinate || outside != lastOutside) {
                    continuousSidesNumber++
                }
                lastCoordinate = coordinate
                lastOutside = outside
            }
        }
        (0 .. lines[0].length).forEach { x -> countContinuousSides({ it.from.x == x && it.to.x == x }) { it.y } }
        (0 .. lines.size).forEach { y -> countContinuousSides({ it.from.y == y && it.to.y == y }) { it.x } }
        areas.add(Area(areaPoints, areaSides, continuousSidesNumber))
    }

    fun partOne() {
        println(
            areas.sumOf { it.sides.size * it.points.size }
        )
    }

    fun partTwo() {
        println(
            areas.sumOf { it.continuousSidesNumber * it.points.size }
        )
    }

    partOne()
    partTwo()
}