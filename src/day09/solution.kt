package day09

import java.io.File

fun main() {
    val input = File("src/day09/input.txt").readLines().first()
    val diskTemplate = mutableListOf<Int>()
    var nextBlockId = 0
    var blockEmpty = false
    input.forEach {
        val blockSize = it.digitToInt()
        if (blockEmpty) {
            repeat(blockSize) { diskTemplate.add(-1) }
        } else {
            repeat(blockSize) { diskTemplate.add(nextBlockId) }
            nextBlockId++
        }
        blockEmpty = !blockEmpty
    }

    fun partOne() {
        val disk = diskTemplate.toMutableList()
        var i = 0
        var j = disk.size - 1
        while (i < j) {
            if (disk[i] != -1) {
                i++
                continue
            }
            if (disk[j] == -1) {
                j--
                continue
            }
            disk[i] = disk[j]
            disk[j] = -1
            i++
            j--
        }
        var sum = 0L
        disk.asSequence()
            .takeWhile { it != -1 }
            .forEachIndexed { index, blockId -> sum += index * blockId }
        println(sum)
    }

    fun partTwo() {
        val disk = diskTemplate.toMutableList()
        val blocks = mutableMapOf<Int, Pair<Int, Int>>()
        for (blockId in (nextBlockId - 1) downTo 0) {
            val start = disk.indexOfFirst { it == blockId }
            var end = start
            while (end < disk.size && disk[end] == blockId) end++
            blocks[blockId] = start to end
        }
        val emptyBlocks = mutableListOf<Pair<Int, Int>>()
        var start = -1
        for (i in 0 until disk.size) {
            if (disk[i] == -1) {
                if (start == -1) start = i
            } else {
                if (start != -1) emptyBlocks.add(start to i)
                start = -1
            }
        }
        if (start != -1) emptyBlocks.add(start to disk.size)
        blocks.entries.sortedByDescending { it.key }.forEach { (blockId, range) ->
            val blockSize = range.second - range.first
            val matchingEmptyBlockIndex = emptyBlocks.indexOfFirst { it.second - it.first >= blockSize }
            if (matchingEmptyBlockIndex != -1) {
                val targetBlock = emptyBlocks[matchingEmptyBlockIndex]
                if (targetBlock.first < range.first) { // move only to empty block closer to start
                    for (i in targetBlock.first until targetBlock.first + blockSize) {
                        disk[i] = blockId // copy block to empty space
                    }
                    for (i in range.first until range.second) {
                        disk[i] = -1 // erase copied-from space
                    }
                    if (blockSize == targetBlock.second - targetBlock.first) {
                        emptyBlocks.removeAt(matchingEmptyBlockIndex) // empty block filled completely
                    } else {
                        // empty block shrunk
                        emptyBlocks[matchingEmptyBlockIndex] = (targetBlock.first) + blockSize to targetBlock.second
                    }
                }
            }
        }
        var sum = 0L
        disk.asSequence()
            .forEachIndexed { index, blockId ->
                if (blockId != -1) {
                    sum += index * blockId
                }
            }
        println(sum)
    }

    partOne()
    partTwo()
}