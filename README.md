# LVMutator
Large-variance clone mutator and validator

# How to use
- `./gradlew shadowjar`
- `java -jar ./build/libs/LVMutator-all.jar option`

# Option
|Option|Sub-option|Name|Description|
|:--:|:--:|:--:|:--
|`1`|`dirName`|Collection|Collect methods as mutant source under the dir|
|`2`|`-`|Mutation|Mutate the collected methods to generate large-variance clones|
|`3`|`-`|Injection|Inject the large-variance clone pairs into the target system (under `system` dir)|
|`4`|`reseultFile`|Evaluation|Evaluate the clone detection result and measure recall|
