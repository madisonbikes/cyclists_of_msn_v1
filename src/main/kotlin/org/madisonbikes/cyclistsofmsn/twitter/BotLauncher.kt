/*
 * Copyright (c) 2020 Madison Bikes, Inc.
 */

package org.madisonbikes.cyclistsofmsn.twitter

import net.sourceforge.argparse4j.ArgumentParsers
import net.sourceforge.argparse4j.inf.ArgumentParserException

class BotLauncher {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val argumentParser = ArgumentParsers.newFor("cyclistsofmadisonbot")
                .fromFilePrefix("@")
                .build()

            try {
                val commands = arrayOf(GenerateConfigCommand(), RegisterCommand(), NewPostCommand())
                commands.forEach {
                    val subparser = it.prepareCommandParser(argumentParser.addSubparsers())
                    subparser.setDefault("func", it)
                }
                val namespace = argumentParser.parseArgs(args)
                val func = namespace.get<BotCommand>("func")
                func.launch(namespace)
            } catch (e: ArgumentParserException) {
                argumentParser.handleError(e)
            }
        }
    }
}