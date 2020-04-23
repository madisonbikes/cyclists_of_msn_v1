/*
 * Copyright (c) 2020 Madison Bikes, Inc.
 */

package org.madisonbikes.cyclistsofmsn.common

import org.apache.commons.codec.digest.DigestUtils

/** helper for doing SHA digest */
object Digest {
    val digestUtils by lazy {
        DigestUtils(DigestUtils.getSha256Digest())
    }
}