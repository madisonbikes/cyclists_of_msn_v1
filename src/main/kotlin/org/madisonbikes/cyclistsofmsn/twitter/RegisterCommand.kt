/*
 * Copyright (c) 2020 Madison Bikes, Inc.
 */

package org.madisonbikes.cyclistsofmsn.twitter

import net.sourceforge.argparse4j.impl.Arguments
import net.sourceforge.argparse4j.inf.Namespace
import net.sourceforge.argparse4j.inf.Subparser
import net.sourceforge.argparse4j.inf.Subparsers

/**
 * A command that links this bot installation to a specific twitter bot instance. The result of this action is an updated
 * configuration file that includes the access token/secret.
 */
class RegisterCommand : BotCommand {

    override fun prepareCommandParser(subparsers: Subparsers): Subparser {
        return subparsers.addParser("register").apply {
            defaultHelp(true)
            description("Setup the link to your Twitter bot")

            TwitterConfiguration.registerArguments(this)
                .type(Arguments.fileType().verifyCanWrite())
        }
    }

    override fun launch(arguments: Namespace) {
        val config = TwitterConfiguration.fromArguments(arguments)
        val configurationFile = TwitterConfiguration.getConfigurationFile(arguments)

        RegisterImpl(config).apply {
            register()

            writeConfiguration(configurationFile)
        }
    }
}