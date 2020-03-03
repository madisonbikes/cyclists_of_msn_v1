package org.madisonbikes.cyclistofmsn.twitter

import java.io.File
import java.io.FileInputStream
import java.util.*

object Configuration {

    private lateinit var props: Properties

    fun init(properties: File) {
        FileInputStream(properties).use {
            props = Properties()
            props.load(it)
        }
    }

    val consumerApiKey by lazy { requireNotNull(props.getProperty("cyclistsOfMadison.twitter.consumerApiKey")) }
    val consumerApiSecret by lazy { requireNotNull(props.getProperty("cyclistsOfMadison.twitter.consumerApiSecretKey")) }
    val token by lazy { requireNotNull(props.getProperty("cyclistsOfMadison.twitter.accessToken")) }
    val tokenSecret by lazy { requireNotNull(props.getProperty("cyclistsOfMadison.twitter.accessTokenSecret")) }
    val photoDirectory by lazy { File(props.getProperty("cyclistsOfMadison.photoDirectory", "photos")) }
}