package io.github.t45k.lvmutator

fun main(args: Array<String>) {
    if(args.isEmpty()){
        println(
            """1 -> Collect functions from target dir
            |2 -> Mutate collected functions
            |3 -> Inject original and mutated functions
            |4 -> Validate clone detection results
            |5 -> Clean up auto-generated files
        """.trimMargin()
        )
        return
    }

    when(args[0]){
        "1" -> Collector().collect(args[1])
        "2" -> Mutator().mutate()
        "3" -> Injector().inject()
        "4" -> Validator().validate(args[1])
        "5" -> Cleaner().clean()
        else -> throw RuntimeException("Specify 1..5")
    }
}
