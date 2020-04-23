/*
 * Copyright (c) 2020 Madison Bikes, Inc.
 */

package org.madisonbikes.cyclistsofmsn.common

/**
 *  Common configuration attributes that may be used by multiple tasks.
 */
interface CommonConfiguration {
    /** dry run mode should limit filesystem and network? calls */
    val dryRun: Boolean

    /** prepend a random delay to the task */
    val randomDelay: Long
}