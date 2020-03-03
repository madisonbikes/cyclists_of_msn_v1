package org.madisonbikes.cyclistofmsn.twitter

import twitter4j.StatusUpdate
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class BotNewPost(private val configuration: Configuration) {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            require(args.size == 1) {
                "Must supply configuration file argument"
            }
            val bot = BotNewPost(Configuration(File(args[0])))

            val randomPhoto = bot.selectRandomPhoto()
            println("Selected $randomPhoto")
            println("resizing...")
            val resizedPhoto = bot.buildResizedPhoto(randomPhoto)
            println("posting...")
            bot.post(resizedPhoto)
            println("done.")
        }
    }

    fun buildResizedPhoto(input: File): File {
        @Suppress("SpellCheckingInspection") val tempPicture =
            File.createTempFile("cyclistsofmadison", ".${input.extension}")
        tempPicture.deleteOnExit()
        val args = arrayOf(
            "convert",
            input.absolutePath,
            "-resize",
            "${Configuration.MAXIMUM_IMAGE_WIDTH}x>",
            tempPicture.absolutePath
        )
        val process = Runtime.getRuntime().exec(args)
        check(process.waitFor(10, TimeUnit.SECONDS)) {
            throw Exception("Conversion timed out args=$args")
        }
        val exitValue = process.exitValue()
        check(process.exitValue() == 0) {
            "Conversion failed, exitValue=$exitValue args=$args"
        }
        return tempPicture
    }

    fun selectRandomPhoto(): File {
        val photoDirectory = configuration.photoDirectory
        val files = requireNotNull(photoDirectory.listFiles()) {
            "photo directory $photoDirectory does not exist"
        }
        val randomIndex = Random.nextInt(files.size)
        return files[randomIndex]
    }

    fun post(image: File) {
        val twitter = TwitterFactory.getSingleton()
        val token = AccessToken(configuration.token, configuration.tokenSecret)
        twitter.setOAuthConsumer(configuration.consumerApiKey, configuration.consumerApiSecret)
        twitter.oAuthAccessToken = token

        val status = StatusUpdate(Configuration.POST_CONTENT)
        status.media(image)
        twitter.updateStatus(status)
    }
}