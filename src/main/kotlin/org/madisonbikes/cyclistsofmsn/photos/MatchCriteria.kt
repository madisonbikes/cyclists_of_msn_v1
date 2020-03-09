package org.madisonbikes.cyclistsofmsn.photos

import java.io.File

/** returns whether or not the photo matches a specific criteria */
interface MatchCriteria {
    fun isSatisfied(candidatePhoto: File, matchingPost: PhotoPost): Boolean
}