package org.madisonbikes.cyclistsofmsn.twitter

import okio.buffer
import okio.sink
import okio.source

class PostHistory(private val configuration: Configuration) {
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