package io.github.t45k.lvmutator

import java.io.File

class Cleaner {
    fun clean() {
        deleteFiles("fragment")
        deleteFiles("mutantfragment")
        deleteFiles("system")
    }

    private fun deleteFiles(dirName: String) {
        File(dirName).walk()
            .filterNot { it.name == ".gitkeep" }
            .forEach { it.delete() }
    }
}
