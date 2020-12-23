package io.github.t45k.lvmutator

import java.io.File

class Injector {
    fun inject() {
        File("fragment").walk()
            .filter { it.isFile && it.name != ".gitkeep" }
            .forEach {
                val contents = "class Main {\n" + it.readText() + "\n}"
                File("system", "Fragment" + it.name + ".java").writeText(contents)
            }

        File("mutantfragment").walk()
            .filter { it.isFile && it.name != ".gitkeep" }
            .forEach {
                val contents = "class Main {\n" + it.readText() + "\n}"
                File("system", "MutantFragment" + it.name + ".java").writeText(contents)
            }
    }
}
