/*
 * Copyright (c) 2020 Madison Bikes, Inc.
 */

package org.madisonbikes.cyclistsofmsn.photos

import okio.buffer
import okio.sink
import okio.source
import org.madisonbikes.cyclistsofmsn.common.Json

class PostHistory(private val configuration: PhotoConfiguration) {
    private var posts: List<PhotoPost> = emptyList()

    init {
        load()
    }

    fun load() {
        posts = if (configuration.postsDatabaseFile.isFile) {
            configuration.postsDatabaseFile.source().buffer().use {
                Json.photoPostListAdapter.fromJson(it)
                    ?: emptyList()
            }
        } else {
            emptyList()
        }
    }

    fun store() {
        if (configuration.dryRun) {
            println("skipped writing posts db -- dryrun mode")
            return
        }

        configuration.postsDatabaseFile.sink().buffer().use {
            Json.photoPostListAdapter
                .indent("  ")
                .toJson(it, posts)
        }
    }

    fun findPostWithHash(hash: String): PhotoPost? {
        return posts.firstOrNull { it.hash == hash }
    }

    fun removePost(post: PhotoPost) {
        posts = posts - post
    }

    fun addPost(post: PhotoPost) {
        posts = posts + post
    }
}