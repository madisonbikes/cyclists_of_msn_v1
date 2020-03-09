package org.madisonbikes.cyclistsofmsn.photos

import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

/** criteria passes if the photo has not been posted within the past N days (typically 180) */
class RepostCriteria(configuration: PhotoConfiguration) : MatchCriteria {
    private val dateThreshold =
        Date(
            System.currentTimeMillis() - TimeUnit.DAYS.toMillis(
                configuration.minimumRepostInterval.toLong()
            )
        )

    override fun isSatisfied(candidatePhoto: File, matchingPost: PhotoPost): Boolean {
        return matchingPost.postDate == null || matchingPost.postDate < dateThreshold
    }
}