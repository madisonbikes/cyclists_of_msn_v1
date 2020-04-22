/*
 * Copyright (c) 2020 Madison Bikes, Inc.
 */

package org.madisonbikes.cyclistsofmsn.photos

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

/** houses a photo post record */
@JsonClass(generateAdapter = true)
data class PhotoPost(
    val filename: String,
    val hash: String,

    /** when the photo was posted to the twitter feed */
    @Json(name = "posted_on") val postDate: Date? = null,

    /** used to tag photos that aren't yet in store, will be false from json always */
    @Transient val notFromStore: Boolean = false
)