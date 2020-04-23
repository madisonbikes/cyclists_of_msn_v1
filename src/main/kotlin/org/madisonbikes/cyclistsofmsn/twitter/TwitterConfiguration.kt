/*
 * Copyright (c) 2020 Madison Bikes, Inc.
 */

package org.madisonbikes.cyclistsofmsn.twitter

import net.sourceforge.argparse4j.impl.Arguments
import net.sourceforge.argparse4j.inf.Argument
import net.sourceforge.argparse4j.inf.Subparser
import org.madisonbikes.cyclistsofmsn.photos.PhotoConfiguration
import java.io.File
import java.io.FileInputStream
import java.util.*

class TwitterConfiguration(propertiesFile: File) : PhotoConfiguration {
    companion object {
        const val PROP_CONSUMER_API_KEY = "cyclistsOfMadison.twitter.consumerApiKey"
        const val PROP_CONSUMER_API_SECRET = "cyclistsOfMadison.twitter.consumerApiSecretKey"
        const val PROP_ACCESS_TOKEN = "cyclistsOfMadison.twitter.accessToken"
        const val PROP_ACCESS_TOKEN_SECRET = "cyclistsOfMadison.twitter.accessTokenSecret"

        const val MAXIMUM_IMAGE_WIDTH = 1600
        const val POST_CONTENT = "#cyclistsofmadison"

        fun addTwitterConfiguration(subparser: Subparser): Argument {
            return subparser.addArgument("-c", "--twitter-config")
                .required(true)
                .type(Arguments.fileType().verifyCanRead())
                .help("Configuration file containing Twitter tokens and keys")
        }
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