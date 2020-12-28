package io.github.t45k.lvmutator

import java.io.File

class Validator {
    private val alreadyValidatedClonePair: MutableSet<Int> = mutableSetOf()

    fun validate(resultFileName: String) {
        val size = File("mutantfragment").list().size / File("fragment").list().size
        val variance = Array(size + 1) { 0 }
        File(resultFileName).readLines()
            .forEach {
                val elements = it.split(",")
                val fileA = elements[0]
                val fileB = elements[3]
                if (fileA.contains("Mutant") && fileB.contains("Mutant")
                    || !fileA.contains("Mutant") && !fileB.contains("Mutant")
                ) {
                    return@forEach
                }

                if (fileA.contains("Mutant")) {
                    variance[calc(fileB, fileA, size)]++
                } else {
                    variance[calc(fileA, fileB, size)]++
                }
            }
        println("Recall")
        (1..size).forEach { println("$it ${variance[it]}") }

        println("\nOverlooked")
        alreadyValidatedClonePair
            .map { "${(it + size) / size} $it" }
            .forEach { println(it) }
    }

    private fun calc(base: String, mutant: String, size: Int = 20): Int {
        val baseNumber = getNumber(base)
        val mutantNumber = getNumber(mutant)
        return if (mutantNumber > (baseNumber - 1) * size && mutantNumber <= baseNumber * size
            && !alreadyValidatedClonePair.contains(mutantNumber)
        ) {
            alreadyValidatedClonePair.add(mutantNumber)
            mutantNumber - (baseNumber - 1) * size
        } else {
            0
        }
    }

    private fun getNumber(file: String): Int = file.substringAfter("Fragment").substringBefore(".").toInt()
}
