package org.madisonbikes.cyclistofmsn.twitter

import twitter4j.StatusUpdate
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken
import java.io.File
import kotlin.random.Random

class BotNewPost {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            require(args.size == 1) {
                "Must supply configuration file argument"
            }
            Configuration.init(File(args[0]))
            val bot = BotNewPost()

            val randomPhoto = bot.selectRandomPhoto()
            println("Selected $randomPhoto")
            println("resizing...")
            val resizedPhoto = bot.buildResizedPhoto(randomPhoto)
            println("posting...")
            BotNewPost().post(resizedPhoto)
            println("done.")
        }
    }

    fun buildResizedPhoto(input: File): File {
        val tempPicture = File.createTempFile("cyclistsofMadison", ".${input.extension}")
        tempPicture.deleteOnExit()
        val process = Runtime.getRuntime().exec(arrayOf("convert", input.absolutePath, "-resize", "1600x>", tempPicture.absolutePath))
        process.waitFor()
        tempPicture.deleteOnExit()
        return tempPicture
    }

    fun selectRandomPhoto(): File {
        val photoDirectory = Configuration.photoDirectory
        val files = requireNotNull(photoDirectory.listFiles()) {
            "photo directory $photoDirectory does not exist"
        }
        val randomIndex = Random.nextInt(files.size)
        return files[randomIndex]
    }

    fun post(image: File) {
        val twitter = TwitterFactory.getSingleton()
        val token = loadAccessToken()
        twitter.setOAuthConsumer(Configuration.consumerApiKey, Configuration.consumerApiSecret)
        twitter.oAuthAccessToken = token

        val status = StatusUpdate("#cyclistsofmadison")
        status.media(image)
        twitter.updateStatus(status)
    }

    private fun loadAccessToken(): AccessToken {
        return AccessToken(Configuration.token, Configuration.tokenSecret)
    }
}