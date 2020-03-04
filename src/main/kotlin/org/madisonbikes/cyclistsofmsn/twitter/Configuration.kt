package org.madisonbikes.cyclistsofmsn.twitter

import java.io.File
import java.io.FileInputStream
import java.util.*
import java.util.concurrent.TimeUnit

class Configuration(propertiesFile: File) {
    companion object {
        const val CONSUMER_API_KEY = "cyclistsOfMadison.twitter.consumerApiKey"
        const val CONSUMER_API_SECRET = "cyclistsOfMadison.twitter.consumerApiSecretKey"
        const val ACCESS_TOKEN = "cyclistsOfMadison.twitter.accessToken"
        const val ACCESS_TOKEN_SECRET = "cyclistsOfMadison.twitter.accessTokenSecret"
        const val BASE_DIRECTORY = "cyclistsOfMadison.baseDirectory"

        const val MAXIMUM_IMAGE_WIDTH = 1600
        const val POST_CONTENT = "#cyclistsofmadison"

        val MINIMUM_REPOST_INTERVAL_MILLIS by lazy { TimeUnit.DAYS.toMillis(180L) }
    }

    private val props = Properties()

    init {
        FileInputStream(propertiesFile).use {
            props.load(it)
        }
    }

    val consumerApiKey by lazy { requireNotNull(props.getProperty(CONSUMER_API_KEY)) }
    val consumerApiSecret by lazy { requireNotNull(props.getProperty(CONSUMER_API_SECRET)) }
    val token by lazy { requireNotNull(props.getProperty(ACCESS_TOKEN)) }
    val tokenSecret by lazy { requireNotNull(props.getProperty(ACCESS_TOKEN_SECRET)) }
    val baseDirectory by lazy { File(props.getProperty(BASE_DIRECTORY, ".")) }
    val photoDirectory by lazy { File(baseDirectory, "photos") }
    val postsDatabaseFile by lazy { File(baseDirectory, "cyclistsOfMadisonPosts.json") }
}