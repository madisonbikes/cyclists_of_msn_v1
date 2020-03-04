package org.madisonbikes.cyclistsofmsn.twitter

import twitter4j.StatusUpdate
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken
import java.io.File
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class BotNewPost(private val configuration: Configuration) {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            require(args.size == 1) {
                "Must supply configuration file argument"
            }
            BotNewPost(
                Configuration(
                    File(args[0])
                )
            ).apply {
                randomDelay()

                val randomPhoto = selectRandomPhoto()
                println("Selected $randomPhoto")
                println("resizing...")
                val resizedPhoto = buildResizedPhoto(randomPhoto)
                println("posting...")
                post(resizedPhoto)
                println("done.")
            }
        }
    }

    private val postHistory = PostHistory(configuration)

    fun randomDelay() {
        if(configuration.postRandomDelay > 0L) {
            val randomDelay = Random.nextLong(configuration.postRandomDelay)
            println("Sleeping for $randomDelay seconds out of possible ${configuration.postRandomDelay}")
            Thread.sleep(TimeUnit.SECONDS.toMillis(randomDelay))
        }
    }

    fun buildResizedPhoto(input: File): File {
        val tempPicture = File.createTempFile("cyclistsofmadison", ".${input.extension}")
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
        require(files.isNotEmpty()) {
            "photo directory is empty"
        }
        val randomIndex = Random.nextInt(files.size)

        val dateThreshold = Date(System.currentTimeMillis() - Configuration.MINIMUM_REPOST_INTERVAL_MILLIS)

        val newFiles = files.copyOfRange(randomIndex, files.size) + files.copyOfRange(0, randomIndex)
        for (f in newFiles) {
            val hash = Digest.digestUtils.digestAsHex(f)
            val matchingPost = postHistory.findPostWithHash(hash)
            if (matchingPost == null || matchingPost.postDate < dateThreshold) {
                if(matchingPost != null) {
                    postHistory.removePost(matchingPost)
                }
                val newPost = PhotoPost(f.name, hash, Date())
                postHistory.addPost(newPost)
                postHistory.store()
                return f
            }
        }
        throw IllegalStateException("no photos remaining")
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