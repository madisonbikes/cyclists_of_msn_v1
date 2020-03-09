package org.madisonbikes.cyclistsofmsn.twitter

import twitter4j.TwitterException
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken
import java.io.File

class BotAuthentication(private val configuration: TwitterConfiguration) {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            require(args.size == 1) {
                "Require a single argument of properties file"
            }
            val bot = BotAuthentication(
                TwitterConfiguration(File(args[0]))
            )
            bot.register()
        }
    }

    private fun register() {
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