package org.madisonbikes.cyclistsofmsn.twitter

import net.sourceforge.argparse4j.ArgumentParsers
import net.sourceforge.argparse4j.impl.Arguments
import net.sourceforge.argparse4j.inf.ArgumentParserException
import org.madisonbikes.cyclistsofmsn.common.RandomDelay
import org.madisonbikes.cyclistsofmsn.photos.*
import twitter4j.StatusUpdate
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken
import java.io.File
import java.util.*

class BotNewPost(private val configuration: TwitterConfiguration) {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val argumentParser = ArgumentParsers.newFor("BotNewPost")
                .fromFilePrefix("@")
                .build()
                .defaultHelp(true)
                .description("Post a new item from the bot")

            argumentParser.addArgument("-c", "--config")
                .required(true)
                .type(Arguments.fileType().verifyCanRead())
                .help("Configuration file contains twitter tokens")

            argumentParser.addArgument("--base-directory")
                .type(Arguments.fileType().verifyIsDirectory())
                .help("Base directory for photos and posts database. Defaults to cwd.")

            argumentParser.addArgument("--dry-run")
                .action(Arguments.storeTrue())
                .help("Dry run, doesn't change anything in posts database or post anything to twitter.")

            argumentParser.addArgument("--random-delay")
                .type(Long::class.java)
                .setDefault(0L)
                .help("Apply a random delay between zero and the supplied value in seconds before posting occurs")

            argumentParser.addArgument("--minimum-repost-interval")
                .type(Int::class.java)
                .setDefault(180)
                .help("Target minimum interval (in days) before a photo will be reposted")

            argumentParser.addArgument("--seasonality-window")
                .type(Int::class.java)
                .setDefault(45)
                .help("Size of window (in days on either side of current date) that we will search for photos that are in-season")

            try {
                val namespace = argumentParser.parseArgs(args)
                val config = TwitterConfiguration(namespace.get("config"))
                config.randomDelay = namespace.getLong("random_delay")
                config.dryRun = namespace.getBoolean("dry_run")
                config.seasonalityWindow = namespace.get("seasonality_window")
                config.minimumRepostInterval = namespace.get("minimum_repost_interval")
                config.baseDirectory = (namespace.attrs["base_directory"] as File?) ?: File(".")

                BotNewPost(config).apply {

                    RandomDelay.delay(configuration)

                    val randomPhoto = selectRandomPhoto()
                    println("Selected $randomPhoto")
                    println("resizing...")
                    val resizedPhoto = buildResizedPhoto(randomPhoto)
                    val currentDate = Date()
                    println("posting $randomPhoto at $currentDate...")
                    post(resizedPhoto)
                    println("success.")
                }
            } catch (e: ArgumentParserException) {
                argumentParser.handleError(e)
            }
        }
    }

    private val postHistory = PostHistory(configuration)

    fun buildResizedPhoto(input: File): File {
        val tempPicture = File.createTempFile("cyclistsofmadison", ".${input.extension}")
        tempPicture.deleteOnExit()
        ResizePhoto.withMaximumWidth(input, tempPicture, TwitterConfiguration.MAXIMUM_IMAGE_WIDTH)
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
        val photoSelection = PhotoSelection(postHistory, configuration)
        val matchingPhoto = photoSelection.findMatchingPhoto(files.toList())
        requireNotNull(matchingPhoto) {
            "no photos remaining"
        }
        if (!matchingPhoto.post.notFromStore) {
            // remove existing post record
            postHistory.removePost(matchingPhoto.post)
        }
        val newPost = PhotoPost(
            filename = matchingPhoto.post.filename,
            hash = matchingPhoto.post.hash,
            postDate = Date()
        )
        postHistory.addPost(newPost)
        postHistory.store()
        return matchingPhoto.file
    }

    fun post(image: File) {
        if (configuration.dryRun) {
            println("skipped posting to twitter -- dryrun mode")
            return
        }

        val twitter = TwitterFactory.getSingleton()
        val token = AccessToken(configuration.token, configuration.tokenSecret)
        twitter.setOAuthConsumer(configuration.consumerApiKey, configuration.consumerApiSecret)
        twitter.oAuthAccessToken = token

        val status = StatusUpdate(TwitterConfiguration.POST_CONTENT)
        status.media(image)
        twitter.updateStatus(status)
    }
}