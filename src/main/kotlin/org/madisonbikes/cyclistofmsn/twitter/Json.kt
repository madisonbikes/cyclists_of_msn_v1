package org.madisonbikes.cyclistofmsn.twitter

import com.squareup.moshi.*
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.util.*

object Json {
    val moshi by lazy {
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