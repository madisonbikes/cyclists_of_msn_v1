/*
 * Copyright (c) 2020 Madison Bikes, Inc.
 */

package org.madisonbikes.cyclistsofmsn.twitter

import net.sourceforge.argparse4j.impl.Arguments
import net.sourceforge.argparse4j.inf.Namespace
import net.sourceforge.argparse4j.inf.Subparser
import net.sourceforge.argparse4j.inf.Subparsers
import org.madisonbikes.cyclistsofmsn.common.RandomDelay
import java.util.*

class NewPostCommand : BotCommand {

    override fun prepareCommandParser(subparsers: Subparsers): Subparser {
        return subparsers.addParser("post").apply {
            defaultHelp(true)
            description("Post a new item from the bot")

            TwitterConfiguration.registerArguments(this)
                .type(Arguments.fileType().verifyCanRead())
            NewPostConfiguration.registerArguments(this)
        }
    }

    override fun launch(arguments: Namespace) {
        val twitterConfiguration = TwitterConfiguration.fromArguments(arguments)
        val newPostConfiguration = NewPostConfiguration(arguments)

        NewPostImpl(twitterConfiguration, newPostConfiguration).apply {

            RandomDelay.delay(newPostConfiguration)

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