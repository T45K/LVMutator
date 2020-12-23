package io.github.t45k.lvmutator

fun main(args: Array<String>) {
    when(args[0]){
        "1" -> Collector().collect(args[1])
        "2" -> Mutator().mutate()
        "3" -> Injector().inject()
        "4" -> Validator().validate(args[1])
        else -> throw RuntimeException("Pleas specify 1..4")
    }
}
