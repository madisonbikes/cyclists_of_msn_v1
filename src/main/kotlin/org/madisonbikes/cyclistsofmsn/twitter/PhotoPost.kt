package org.madisonbikes.cyclistsofmsn.twitter

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.File
import java.util.*

@JsonClass(generateAdapter = true)
data class PhotoPost(
    val filename: String,
    val hash: String,
    @Json(name = "posted_on") val postDate: Date
)