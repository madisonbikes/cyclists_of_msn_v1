/*
 * Copyright (c) 2020 Madison Bikes, Inc.
 */

package org.madisonbikes.cyclistsofmsn.twitter

import net.sourceforge.argparse4j.impl.Arguments
import net.sourceforge.argparse4j.inf.Namespace
import net.sourceforge.argparse4j.inf.Subparser
import net.sourceforge.argparse4j.inf.Subparsers
import org.madisonbikes.cyclistsofmsn.common.RandomDelay
import java.io.File
import java.util.*

class NewPostCommand : BotCommand {

    override fun prepareCommandParser(subparsers: Subparsers): Subparser {
        return subparsers.addParser("post").apply {
            defaultHelp(true)
            description("Post a new item from the bot")

            TwitterConfiguration.addTwitterConfiguration(this)

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

    override fun launch(arguments: Namespace) {

        val config = TwitterConfiguration(arguments.get("config"))
        config.randomDelay = arguments.getLong("random_delay")
        config.dryRun = arguments.getBoolean("dry_run")
        config.seasonalityWindow = arguments.get("seasonality_window")
        config.minimumRepostInterval = arguments.get("minimum_repost_interval")
        config.baseDirectory = arguments.get<File?>("base_directory") ?: File(".")

        NewPostImpl(config).apply {

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
    }
}