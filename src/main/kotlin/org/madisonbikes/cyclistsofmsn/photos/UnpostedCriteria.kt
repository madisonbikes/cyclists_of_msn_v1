package org.madisonbikes.cyclistsofmsn.photos

import java.io.File

/** passes check if photo has never been posted */
class UnpostedCriteria: MatchCriteria {
    override fun isSatisfied(candidatePhoto: File, matchingPost: PhotoPost): Boolean {
        return matchingPost.postDate == null
    }
}