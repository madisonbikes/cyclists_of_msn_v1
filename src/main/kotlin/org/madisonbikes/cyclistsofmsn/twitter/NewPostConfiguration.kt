/*
 * Copyright (c) 2020 Madison Bikes, Inc.
 */

package org.madisonbikes.cyclistsofmsn.twitter

import net.sourceforge.argparse4j.impl.Arguments
import net.sourceforge.argparse4j.inf.Namespace
import net.sourceforge.argparse4j.inf.Subparser
import org.madisonbikes.cyclistsofmsn.photos.PhotoConfiguration
import java.io.File

class NewPostConfiguration(arguments: Namespace) : PhotoConfiguration {
    companion object {
        const val MAXIMUM_IMAGE_WIDTH = 1600
        const val POST_CONTENT = "#cyclistsofmadison"

        fun registerArguments(subparser: Subparser) {
            subparser.apply {
                addArgument("--base-directory")
                    .type(Arguments.fileType().verifyIsDirectory())
                    .help("Base directory for photos and posts database. Defaults to cwd.")

                addArgument("--dry-run")
                    .action(Arguments.storeTrue())
                    .help("Dry run, doesn't change anything in posts database or post anything to twitter.")

                addArgument("--random-delay")
                    .type(Long::class.java)
                    .setDefault(0L)
                    .help("Apply a random delay between zero and the supplied value in seconds before posting occurs")

                addArgument("--minimum-repost-interval")
                    .type(Int::class.java)
                    .setDefault(180)
                    .help("Target minimum interval (in days) before a photo will be reposted")

                addArgument("--seasonality-window")
                    .type(Int::class.java)
                    .setDefault(45)
                    .help("Size of window (in days on either side of current date) that we will search for photos that are in-season")
            }
        }
    }

    private val baseDirectory by lazy {
        arguments.get<File?>("base_directory") ?: File(".")
    }

    override val photoDirectory by lazy { File(baseDirectory, "photos") }
    override val postsDatabaseFile by lazy { File(baseDirectory, "cyclistsOfMadisonPosts.json") }

    // ATTENTION THESE DEFAULTS ARE OVERRIDDEN BY THE COMMAND LINE ARGUMENT CLASS!!
    override val randomDelay: Long by lazy {
        arguments.getLong("random_delay")
    }

    override val dryRun: Boolean by lazy {
        arguments.getBoolean("dry_run")
    }

    override val seasonalityWindow: Int by lazy {
        arguments.getInt("seasonality_window")
    }

    override val minimumRepostInterval: Int by lazy {
        arguments.getInt("minimum_repost_interval")
    }
}