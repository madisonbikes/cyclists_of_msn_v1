/*
 * Copyright (c) 2020 Madison Bikes, Inc.
 */

package org.madisonbikes.cyclistsofmsn.twitter

import net.sourceforge.argparse4j.inf.Namespace
import net.sourceforge.argparse4j.inf.Subparser
import net.sourceforge.argparse4j.inf.Subparsers

class SetupCommand : BotCommand {

    override fun prepareCommandParser(subparsers: Subparsers): Subparser {
        return subparsers.addParser("register").apply {
            TwitterConfiguration.addTwitterConfiguration(this)
        }
    }

    override fun launch(arguments: Namespace) {
        val config = TwitterConfiguration(arguments.get("config"))
        SetupImpl(config).apply {
            register()
        }
    }
}