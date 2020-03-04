package org.madisonbikes.cyclistsofmsn.twitter

import org.apache.commons.codec.digest.DigestUtils

object Digest {
    val digestUtils by lazy {
        DigestUtils(DigestUtils.getSha256Digest())
    }
}