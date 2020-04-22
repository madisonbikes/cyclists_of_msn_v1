/*
 * Copyright (c) 2020 Madison Bikes, Inc.
 */

package org.madisonbikes.cyclistsofmsn.photos

import org.madisonbikes.cyclistsofmsn.common.CommonConfiguration
import java.io.File

interface PhotoConfiguration : CommonConfiguration {
    val photoDirectory: File
    val postsDatabaseFile: File

    val seasonalityWindow: Int
    val minimumRepostInterval: Int
}