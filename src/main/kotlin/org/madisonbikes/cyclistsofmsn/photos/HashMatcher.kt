/*
 * Copyright (c) 2020 Madison Bikes, Inc.
 */

package org.madisonbikes.cyclistsofmsn.photos

import org.madisonbikes.cyclistsofmsn.common.Digest
import java.io.File

/** finds matching photopost from either cache or file, based on hashcode */
class HashMatcher(private val postHistory: PostHistory) {
    private val hashCache = mutableMapOf<File, String>()

    fun matchingPost(candidatePhoto: File): PhotoPost {
        val hash = getHash(candidatePhoto)
        var retval = postHistory.findPostWithHash(hash)
        if (retval == null) {
            // create empty placeholder
            retval = PhotoPost(
                candidatePhoto.name,
                hash,
                notFromStore = true
            )
        }
        return retval
    }

    private fun getHash(file: File): String {
        var hash = hashCache[file]
        if (hash == null) {
            hash = Digest.digestUtils.digestAsHex(file)
            hashCache[file] = hash
        }
        return checkNotNull(hash)
    }
}