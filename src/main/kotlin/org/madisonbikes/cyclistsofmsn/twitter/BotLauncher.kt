/*
 * Copyright (c) 2020 Madison Bikes, Inc.
 */

package org.madisonbikes.cyclistsofmsn.twitter

class BotLauncher {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            if (args.isEmpty()) {
                showError()
                return
            } else {
                val trimmedArgs = mutableListOf(*args)
                trimmedArgs.removeAt(0)
                when (args[0]) {
                    "BotNewPost" -> {
                        BotNewPost.main(trimmedArgs.toTypedArray())
                    }
                    "BotAuthentication" -> {
                        BotAuthentication.main(trimmedArgs.toTypedArray())
                    }
                    else -> {
                        showError()
                    }
                }
            }
        }

        private fun showError() {
            System.err.println("Supported tasks are BotNewPost and BotAuthentication")
        }
    }
}