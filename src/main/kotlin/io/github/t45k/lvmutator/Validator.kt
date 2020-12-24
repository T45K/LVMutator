package io.github.t45k.lvmutator

import java.io.File

class Validator {
    private val alreadyValidatedClonePair: MutableSet<Int> = mutableSetOf()

    fun validate(resultFileName: String) {
        val variance = Array(21) { 0 }
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
                    variance[calc(fileB, fileA)]++
                } else {
                    variance[calc(fileA, fileB)]++
                }
            }
        (1..20).forEach { println("$it ${variance[it]}") }
    }

    private fun calc(base: String, mutant: String): Int {
        val baseNumber = getNumber(base)
        val mutantNumber = getNumber(mutant)
        return if (mutantNumber > (baseNumber - 1) * 20 && mutantNumber <= baseNumber * 20
            && !alreadyValidatedClonePair.contains(mutantNumber)
        ) {
            alreadyValidatedClonePair.add(mutantNumber)
            mutantNumber - (baseNumber - 1) * 20
        } else {
            0
        }
    }

    private fun getNumber(file: String): Int = file.substringAfter("Fragment").substringBefore(".").toInt()
}
