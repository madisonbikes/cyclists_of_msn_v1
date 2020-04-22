/*
 * Copyright (c) 2020 Madison Bikes, Inc.
 */

package org.madisonbikes.cyclistsofmsn.common

import com.squareup.moshi.*
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.jetbrains.annotations.NotNull
import org.madisonbikes.cyclistsofmsn.photos.PhotoPost
import java.util.*

object Json {
    val moshi: Moshi by lazy {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .add(Date::class.java, Rfc3339DateJsonAdapter())
            .build()
    }

    val photoPostListAdapter by lazy {
        val type = Types.newParameterizedType(List::class.java, PhotoPost::class.java)
        moshi.adapter<List<PhotoPost>>(type)
    }
}