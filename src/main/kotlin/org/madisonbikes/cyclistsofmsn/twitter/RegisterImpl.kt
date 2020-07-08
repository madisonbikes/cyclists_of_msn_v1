/*
 * Copyright (c) 2020 Madison Bikes, Inc.
 */

package org.madisonbikes.cyclistsofmsn.twitter

import okio.buffer
import okio.sink
import org.madisonbikes.cyclistsofmsn.common.Json
import twitter4j.TwitterException
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken
import java.io.File

class RegisterImpl(private val configuration: TwitterConfiguration) {
    private var accessToken: AccessToken? = null

    /** register with twitter using the supplied api key/secret */
    fun register() {
        val twitter = TwitterFactory.getSingleton()
        twitter.setOAuthConsumer(configuration.consumerApiKey, configuration.consumerApiSecret)
        val requestToken = twitter.oAuthRequestToken
        var at: AccessToken? = null

        // keep trying until it works?
        while (at == null) {
            println("Open the following URL and grant access to your account:")
            println(requestToken.authorizationURL)
            print("Enter the PIN (if available) or just hit enter. [PIN]:")
            val pin = requireNotNull(readLine())
            try {
                at = if (pin.isNotEmpty()) {
                    twitter.getOAuthAccessToken(requestToken, pin)
                } else {
                    twitter.oAuthAccessToken
                }
            } catch (te: TwitterException) {
                if (401 == te.statusCode) {
                    println("Unable to get the access token.")
                } else {
                    te.printStackTrace()
                }
            }
        }
        twitter.verifyCredentials()
        accessToken = at
    }

    /** write new configuration to the specified file */
    fun writeConfiguration(file: File) {
        val at = checkNotNull(accessToken) {
            "Should be set from previous method call"
        }
        val newConfiguration =
            configuration.copy(accessToken = at.token, accessTokenSecret = at.tokenSecret)
        println("Writing new configuration to $file...")
        file.sink().buffer().use {
            Json.twitterConfigurationAdapter
                .indent("  ")
                .toJson(it, newConfiguration)
        }
        print("done.")
    }
}