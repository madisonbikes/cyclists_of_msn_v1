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

            addArgument("consumer-api-key")
                .help("Twitter Consumer API key")
                .required(true)

            addArgument("consumer-api-secret-key")
                .help("Twitter Consumer API secret key")
                .required(true)

            TwitterConfiguration.registerArguments(this)
                .type(Arguments.fileType().verifyCanCreate().verifyNotExists())
        }
    }

    override fun launch(arguments: Namespace) {
        val configuration = TwitterConfiguration(
            consumerApiKey = arguments.getString("consumer_api_key"),
            consumerApiSecret = arguments.getString("consumer_api_secret_key")
        )

        val configurationFile = TwitterConfiguration.getConfigurationFile(arguments)

        RegisterImpl(configuration).apply {
            register()

            writeConfiguration(configurationFile)
        }
    }
}