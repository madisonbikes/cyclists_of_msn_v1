/*
 * Copyright (c) 2020 Madison Bikes, Inc.
 */

package org.madisonbikes.cyclistsofmsn.twitter

import com.squareup.moshi.JsonClass
import net.sourceforge.argparse4j.inf.Argument
import net.sourceforge.argparse4j.inf.Namespace
import net.sourceforge.argparse4j.inf.Subparser
import okio.buffer
import okio.source
import org.madisonbikes.cyclistsofmsn.common.Json
import java.io.File

/** contains various Twitter configuration details that can be serialized into JSON */
@JsonClass(generateAdapter = true)
data class TwitterConfiguration(
    val consumerApiKey: String,
    val consumerApiSecret: String,
    val accessToken: String? = null,
    val accessTokenSecret: String? = null
)  {
    companion object {
        fun registerArguments(subparser: Subparser): Argument {
            return subparser.addArgument("-c", "--twitter-config")
                .required(true)
                .help("JSON configuration file containing Twitter tokens and keys")
        }

        fun getConfigurationFile(namespace: Namespace): File {
            return requireNotNull(namespace.get("twitter_config")) {
                "should always be supplied"
            }
        }

        private fun fromJsonFile(file: File): TwitterConfiguration? {
            file.source().buffer().use {
                return Json.twitterConfigurationAdapter.fromJson(it)
            }
        }

        fun fromArguments(namespace: Namespace): TwitterConfiguration {
            return requireNotNull(fromJsonFile(getConfigurationFile(namespace))) {
                "configuration should be supplied"
            }
        }
    }
}