/*
 * Copyright (c) 2020 Madison Bikes, Inc.
 */

package org.madisonbikes.cyclistsofmsn.twitter

import net.sourceforge.argparse4j.impl.Arguments
import net.sourceforge.argparse4j.inf.Namespace
import net.sourceforge.argparse4j.inf.Subparser
import net.sourceforge.argparse4j.inf.Subparsers
import okio.buffer
import okio.sink
import org.madisonbikes.cyclistsofmsn.common.Json

/** command that generates a boilerplate JSON configuration file from the supplied information */
class GenerateConfigCommand : BotCommand {

    override fun prepareCommandParser(subparsers: Subparsers): Subparser {
        return subparsers.addParser("generate_configuration").apply {
            defaultHelp(true)
            description("Generate a default configuration file")

            addArgument("consumer-api-key")
                .help("Twitter Consumer API key")
                .required(true)

            addArgument("consumer-api-secret-key")
                .help("Twitter Consumer API secret key")
                .required(true)

            TwitterConfiguration.registerArguments(this)
                .type(Arguments.fileType().verifyCanCreate())
        }
    }

    override fun launch(arguments: Namespace) {
        val newConfig = TwitterConfiguration(
            consumerApiKey = arguments.getString("consumer_api_key"),
            consumerApiSecret = arguments.getString("consumer_api_secret_key")
        )

        val file = TwitterConfiguration.getConfigurationFile(arguments)

        print("Writing new configuration to $file...")
        file.sink().buffer().use {
            Json.twitterConfigurationAdapter.toJson(it, newConfig)
        }
        println("done")
    }
}