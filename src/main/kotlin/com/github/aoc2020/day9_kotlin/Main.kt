@file:JvmName("Main")

package com.github.aoc2020.day9_kotlin

import java.lang.IllegalStateException
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import java.util.stream.Collectors
import kotlin.math.max
import kotlin.math.min

fun isValid(input:List<Long>, pre:Int, curr:Int) : Boolean {
    val number = input[curr];
    for (i in curr-pre until curr) {
        for (j in curr - pre until curr) {
            val sum = input[i] + input[j]
            if (sum == number && i != j) {
                return true
            }
        }
    }
    return false
}

fun findFirstInvalid (input: List<Long>, pre:Int) : Long {
    for (i in pre .. input.lastIndex) {
        if (!isValid(input,pre,i)) return input[i];
    }
    throw IllegalStateException("No solution found")
}

fun findRangeAt(input: List<Long>, invalid: Long, start: Int) : Optional<Pair<Int, Int>> {
    var acc = input[start]
    var curr = start+1
    while (acc < invalid) {
        acc += input[curr]
        curr += 1;
    }
    if (acc == invalid) return Optional.of(Pair(start,curr))
    else return Optional.empty()
}

fun findRange (input: List<Long>, invalid: Long) : Pair<Int,Int> {
    for (i in input.indices) {
        val range = findRangeAt(input,invalid,i)
        if (range.isPresent) return range.get()
    }
    throw IllegalStateException("Failed to find range")
}

fun solve(input: List<String>, pre: Int) {
    val numbers = input.map { s -> s.toLong() }
    val invalid = findFirstInvalid(numbers, pre)
    println("Answer 1: ${invalid}")
    val range = findRange (numbers, invalid)
    val values = (range.first until range.second).map {i -> numbers[i]}
    val min : Long = values.reduce { x,y -> min(x,y) };
    val max : Long = values.reduce { x,y -> max(x,y) };
    val answer2 = min + max
    println("Answer 2: $answer2")

}

fun main() {
    try {
        solve(readFile("input.txt"),25);
    } catch (ex: Throwable) {
        println("$ex")
        ex.printStackTrace(System.out)
    }
}

fun readFile(fileName: String): List<String> {
    return Files.lines(Path.of(fileName))
        .collect(Collectors.toUnmodifiableList())
}