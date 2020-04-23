/*
 * Copyright (c) 2020 Madison Bikes, Inc.
 */

package org.madisonbikes.cyclistsofmsn.twitter

import org.madisonbikes.cyclistsofmsn.photos.PhotoPost
import org.madisonbikes.cyclistsofmsn.photos.PhotoSelection
import org.madisonbikes.cyclistsofmsn.photos.PostHistory
import org.madisonbikes.cyclistsofmsn.photos.ResizePhoto
import twitter4j.StatusUpdate
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken
import java.io.File
import java.util.*

class NewPostImpl(private val twitterConfiguration: TwitterConfiguration, private val newPostConfiguration: NewPostConfiguration) {

    private val postHistory = PostHistory(newPostConfiguration)

    fun buildResizedPhoto(input: File): File {
        val tempPicture = File.createTempFile("cyclistsofmadison", ".${input.extension}")
        tempPicture.deleteOnExit()
        ResizePhoto.withMaximumWidth(input, tempPicture, NewPostConfiguration.MAXIMUM_IMAGE_WIDTH)
        return tempPicture
    }

    fun selectRandomPhoto(): File {
        val photoDirectory = newPostConfiguration.photoDirectory
        val files = requireNotNull(photoDirectory.listFiles()) {
            "photo directory $photoDirectory does not exist"
        }
        require(files.isNotEmpty()) {
            "photo directory is empty"
        }
        val photoSelection = PhotoSelection(postHistory, newPostConfiguration)
        val matchingPhoto = photoSelection.findMatchingPhoto(files.toList())
        requireNotNull(matchingPhoto) {
            "no photos remaining"
        }
        if (!matchingPhoto.post.notFromStore) {
            // remove existing post record
            postHistory.removePost(matchingPhoto.post)
        }
        val newPost = PhotoPost(
            filename = matchingPhoto.post.filename,
            hash = matchingPhoto.post.hash,
            postDate = Date()
        )
        postHistory.addPost(newPost)
        postHistory.store()
        return matchingPhoto.file
    }

    fun post(image: File) {
        if (newPostConfiguration.dryRun) {
            println("skipped posting to twitter -- dryrun mode")
            return
        }

        val twitter = TwitterFactory.getSingleton()
        val token = AccessToken(twitterConfiguration.accessToken, twitterConfiguration.accessTokenSecret)
        twitter.setOAuthConsumer(twitterConfiguration.consumerApiKey, twitterConfiguration.consumerApiSecret)
        twitter.oAuthAccessToken = token

        val status = StatusUpdate(NewPostConfiguration.POST_CONTENT)
        status.media(image)
        twitter.updateStatus(status)
    }
}