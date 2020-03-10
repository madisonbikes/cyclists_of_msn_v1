package org.madisonbikes.cyclistsofmsn.photos

import com.drew.imaging.ImageMetadataReader
import com.drew.metadata.Metadata
import com.drew.metadata.exif.ExifSubIFDDirectory
import java.io.File
import java.util.*
import kotlin.math.abs

/**
 * passes if the given photo matches the seasonality requirement of being
 * taken within N days of the current date (year excluded)<br>
 * TODO maybe cache the metadata if the lookups take too long, since we can go through the list at least four times
 */
class SeasonalityCriteria(private val photoConfiguration: PhotoConfiguration) : MatchCriteria {
    private val calendar by lazy { Calendar.getInstance() }

    override fun isSatisfied(candidatePhoto: File, matchingPost: PhotoPost): Boolean {
        if (photoConfiguration.seasonalityWindow == 0) {
            return true
        }
        val metadata = ImageMetadataReader.readMetadata(candidatePhoto)
        val dir = metadata.getDirectoriesOfType(ExifSubIFDDirectory::class.java)
        val date = dir.firstOrNull { it.dateOriginal != null }?.dateOriginal
        if (date == null) {
            println("$candidatePhoto has no created-on date")
            dumpTags(metadata)
            return false
        }

        if (dayDifferenceBetweenDates(date, Date()) <= photoConfiguration.seasonalityWindow) {
            println("matched photo with date $date")
            return true
        }
        return false
    }

    /**
     * Simple algorithm that calculates the shortest distance
     * between two days-of-year, which is close enough for our purposes */
    private fun dayDifferenceBetweenDates(d1: Date, d2: Date): Int {
        val dd1 = dayOfYear(d1)
        val dd2 = dayOfYear(d2)
        val diff = listOf(
            dd1 - dd2,
            dd1 + 366 - dd2
        )
            .map { abs(it) }
            .min()
        return checkNotNull(diff)
    }

    private fun dayOfYear(date: Date): Int {
        calendar.time = date
        return calendar.get(Calendar.DAY_OF_YEAR)
    }

    private fun dumpTags(metadata: Metadata) {
        metadata.directories.forEach { d ->
            println(d.toString())
            d.tags.forEach { tag ->
                println("  $tag")
            }
        }

    }
}