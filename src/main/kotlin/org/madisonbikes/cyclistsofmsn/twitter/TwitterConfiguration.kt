package org.madisonbikes.cyclistsofmsn.twitter

import org.madisonbikes.cyclistsofmsn.photos.PhotoConfiguration
import java.io.File
import java.io.FileInputStream
import java.util.*
import java.util.concurrent.TimeUnit

class TwitterConfiguration(propertiesFile: File) : PhotoConfiguration {
    companion object {
        const val PROP_CONSUMER_API_KEY = "cyclistsOfMadison.twitter.consumerApiKey"
        const val PROP_CONSUMER_API_SECRET = "cyclistsOfMadison.twitter.consumerApiSecretKey"
        const val PROP_ACCESS_TOKEN = "cyclistsOfMadison.twitter.accessToken"
        const val PROP_ACCESS_TOKEN_SECRET = "cyclistsOfMadison.twitter.accessTokenSecret"

        const val MAXIMUM_IMAGE_WIDTH = 1600
        const val POST_CONTENT = "#cyclistsofmadison"
    }

    private val props = Properties()

    val consumerApiKey by lazy { requireNotNull(props.getProperty(PROP_CONSUMER_API_KEY)) }
    val consumerApiSecret by lazy { requireNotNull(props.getProperty(PROP_CONSUMER_API_SECRET)) }
    val token by lazy { requireNotNull(props.getProperty(PROP_ACCESS_TOKEN)) }
    val tokenSecret by lazy { requireNotNull(props.getProperty(PROP_ACCESS_TOKEN_SECRET)) }

    lateinit var baseDirectory: File

    override val photoDirectory by lazy { File(baseDirectory, "photos") }
    override val postsDatabaseFile by lazy { File(baseDirectory, "cyclistsOfMadisonPosts.json") }

    // ATTENTION THESE DEFAULTS ARE OVERRIDDEN BY THE COMMAND LINE ARGUMENT CLASS!!
    override var randomDelay = 0L
    override var dryRun = false
    override var seasonalityWindow = 0
    override var minimumRepostInterval = 0

    init {
        FileInputStream(propertiesFile).use {
            props.load(it)
        }
    }

}