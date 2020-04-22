/*
 * Copyright (c) 2020 Madison Bikes, Inc.
 */

package org.madisonbikes.cyclistsofmsn.common

import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.random.Random

object RandomDelay {
    fun delay(configuration: CommonConfiguration) {
        if (configuration.randomDelay > 0) {
            val randomDelay = Random.nextLong(configuration.randomDelay)
            val postTime = Date(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(randomDelay))
            println("Scheduling for post at $postTime, a delay of $randomDelay seconds...")
            Thread.sleep(TimeUnit.SECONDS.toMillis(randomDelay))
            println("go!")
        }
    }
}