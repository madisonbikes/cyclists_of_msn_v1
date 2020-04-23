/*
 * Copyright (c) 2020 Madison Bikes, Inc.
 */

package org.madisonbikes.cyclistsofmsn.twitter

import twitter4j.TwitterException
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken

class SetupImpl(private val configuration: TwitterConfiguration) {
    fun register() {
        val twitter = TwitterFactory.getSingleton()
        twitter.setOAuthConsumer(configuration.consumerApiKey, configuration.consumerApiSecret)
        val requestToken = twitter.oAuthRequestToken
        var accessToken: AccessToken? = null

        // keep trying until it works?
        while (accessToken == null) {
            println("Open the following URL and grant access to your account:")
            println(requestToken.authorizationURL)
            print("Enter the PIN (if available) or just hit enter. [PIN]:")
            val pin = requireNotNull(readLine())
            try {
                accessToken = if (pin.isNotEmpty()) {
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
        val id = twitter.verifyCredentials()
        printAccessToken(accessToken)
    }

    private fun printAccessToken(accessToken: AccessToken) {
        println("${TwitterConfiguration.PROP_ACCESS_TOKEN}=${accessToken.token}")
        println("${TwitterConfiguration.PROP_ACCESS_TOKEN_SECRET}=${accessToken.tokenSecret}")
    }
}