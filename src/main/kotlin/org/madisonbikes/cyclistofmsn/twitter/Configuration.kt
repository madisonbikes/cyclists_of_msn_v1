package org.madisonbikes.cyclistofmsn.twitter

import java.io.File
import java.io.FileInputStream
import java.util.*

class Configuration(propertiesFile: File) {
    companion object {
        const val CONSUMER_API_KEY = "cyclistsOfMadison.twitter.consumerApiKey"
        const val CONSUMER_API_SECRET = "cyclistsOfMadison.twitter.consumerApiSecretKey"
        const val ACCESS_TOKEN = "cyclistsOfMadison.twitter.accessToken"
        const val ACCESS_TOKEN_SECRET = "cyclistsOfMadison.twitter.accessTokenSecret"
        const val PHOTO_DIRECTORY = "cyclistsOfMadison.photoDirectory"

        const val MAXIMUM_IMAGE_WIDTH = 1600
        const val POST_CONTENT = "#cyclistsofmadison"
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
    val photoDirectory by lazy { File(props.getProperty(PHOTO_DIRECTORY, "photos")) }
}