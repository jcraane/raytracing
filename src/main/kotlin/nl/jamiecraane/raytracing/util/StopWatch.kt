package nl.jamiecraane.raytracing.util

import java.time.Duration

object StopWatch {
    /**
     * Measures and returns the execution time of the given codeblock.
     */
    fun timeIt(block: () -> Unit): Duration {
        val start = System.currentTimeMillis()
        block()
        return Duration.ofMillis(System.currentTimeMillis() - start)
    }
}