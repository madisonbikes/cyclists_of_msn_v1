/*
 * Copyright (c) 2020 Madison Bikes, Inc.
 */

package org.madisonbikes.cyclistsofmsn.photos

import java.io.File
import kotlin.random.Random

class PhotoSelection(private val postHistory: PostHistory, private val configuration: PhotoConfiguration) {

    class MatchingPhotoRecord(val file: File, val post: PhotoPost)

    fun findMatchingPhoto(files: List<File>): MatchingPhotoRecord? {
        val hashMatcher = HashMatcher(postHistory)

        // these are different criteria that can be combined

        // photos that haven't been posted in the last 180 days (configurable)
        val repostCriteria = RepostCriteria(configuration)

        // photos that have never been posted
        val unpostedCriteria = UnpostedCriteria()

        // photos that meet the seasonality requirements (defaults to 90 day window or 45 days either side of current)
        val seasonalityCriteria = SeasonalityCriteria(configuration)

        /// this is the combination of criteria that will be searched, in order
        val criteriaListRepository = listOf(
            // unposted photos that match seasonality
            listOf(seasonalityCriteria, unpostedCriteria),

            // posted photos outside of repost window (e.g. >180 days) that
            // match seasonality (i.e. last year's photos from this season)
            listOf(seasonalityCriteria, repostCriteria),

            // any unposted photos left in the repository
            listOf(unpostedCriteria),

            // any photos (posted or otherwise) outside of repost window (e.g. >180 days)
            listOf(repostCriteria),

            // fall back to any photo at all
            emptyList()
        )

        // shuffle the photo list so that we have a different look at the photos each time we run
        // ensures we aren't following some sort of filesystem ordering/sorting by accident
        val orderedPhotoList = files.toTypedArray()
            .toMutableList()
            .shuffled(Random.Default)
            .toList()

        // run through complete repo for each criteria list in order
        criteriaListRepository.forEach { activeCriteriaList ->
            // go through each photo in the list, looking for a criteria match
            orderedPhotoList
                .forEach { candidatePhoto ->
                    val localPost = hashMatcher.matchingPost(candidatePhoto)
                    val satisfied = activeCriteriaList
                        .all { it.isSatisfied(candidatePhoto, localPost) }
                    if (satisfied) {
                        return MatchingPhotoRecord(candidatePhoto, localPost)
                    }
                }
        }
        return null
    }

}