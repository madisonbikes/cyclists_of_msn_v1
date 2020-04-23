/*
 * Copyright (c) 2020 Madison Bikes, Inc.
 */

package org.madisonbikes.cyclistsofmsn.twitter

import net.sourceforge.argparse4j.inf.Namespace
import net.sourceforge.argparse4j.inf.Subparser
import net.sourceforge.argparse4j.inf.Subparsers

interface BotCommand {
    fun prepareCommandParser(subparsers: Subparsers): Subparser
    fun launch(arguments: Namespace)
}