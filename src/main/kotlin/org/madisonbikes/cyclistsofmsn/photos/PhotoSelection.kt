package org.madisonbikes.cyclistsofmsn.photos

import java.io.File
import kotlin.random.Random

class PhotoSelection(private val postHistory: PostHistory, private val configuration: PhotoConfiguration) {

    class MatchingPhotoRecord(val file: File, val post: PhotoPost)

    fun findMatchingPhoto(files: List<File>): MatchingPhotoRecord? {
        val randomIndex = Random.nextInt(files.size)

        val hashMatcher = HashMatcher(postHistory)
        // list of criteria in descending importance.
        // If we can't find any photos that match seasonality that haven't been posted,
        // fall back to unposted photos (any season), then fall back to unposted photos outside of seasonality requirement

        val repostCriteria = RepostCriteria(configuration)
        val unpostedCriteria = UnpostedCriteria()
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

        // start the photo search at a random place, build this list different each run
        // ensures we aren't following some sort of filesystem ordering/sorting by accident
        val filesArray = files.toTypedArray()
        val orderedPhotoList = filesArray.copyOfRange(randomIndex, files.size) + filesArray.copyOfRange(0, randomIndex)

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