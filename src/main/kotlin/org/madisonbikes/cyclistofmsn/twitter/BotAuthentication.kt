package org.madisonbikes.cyclistofmsn.twitter

import twitter4j.TwitterException
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken
import java.io.File

class BotAuthentication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            require(args.size ==1) {
                "Require a single argument of properties file"
            }
            Configuration.init(File(args[0]))
            BotAuthentication().register()
        }
    }

    private fun register() {
        val twitter = TwitterFactory.getSingleton()
        twitter.setOAuthConsumer(Configuration.consumerApiKey, Configuration.consumerApiSecret)
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
        printAccessToken(twitter.verifyCredentials().id, accessToken)
    }

    private fun printAccessToken(id: Long, accessToken: AccessToken) {
        println("id=$id, token=${accessToken.token}, tokenSecret=${accessToken.tokenSecret}")
    }
}