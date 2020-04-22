# Cyclists of Madison
Source code for the Twitter bot [Cyclists of Madison](https://twitter.com/cyclists_of_msn) developed by [Madison Bikes](https://madisonbikes.org).

## Overview
This bot posts one photo each day to a Twitter account from a pool of photographs. There are several configurable features:
* It can be run using a simple cron job and setup completely using command line tools.
* It will do its best to avoid repeating the same photo.
* It will try to pick photos from around the same time of year as the current date.
* It can introduce a random delay so that the post doesn't happen at the same time every day.
* It will strip out any unnecessary image metadata before it is posted, for privacy.

## Requirements
It is developed using Kotlin and requires a Java runtime. It uses [ImageMagick](https://imagemagick.org/) for some of the image manipulation, so that needs to be pre-installed on whatever server is hosting the repository and running the commands.

## Build
To build, clone the repository and execute this command (if you trust our included [Gradle](https://gradle.org) wrapper):
```
# ./gradlew assemble
```
or to use your own Gradle installation:
```
# gradle assemble
```

## Installation
The build artifacts are in the `build/distributions` in `.tar.gz` and `.zip` formats (example: `cyclistsofmadison-0.1-SNAPSHOT.tar.gz`). You can move these to your server and extract them to any location you like. The `bin` directory contains launch scripts for 

## Usage
The bot consists of two primary components:
1. The `BotAuthentication` tool, which helps establish the link between this installed tool and a Twitter account of your choosing.
2. The `BotNewPost` tool, which chooses a photo from the pool, prepares a post and sends it to Twitter.

We suggest a starting pool size of around 100 photos, but that is up to you.

## Credits
Harald Kliems (harald@madisonbikes.org) had the original idea and requirements to build this and Ben Sandee (ben@madisonbikes.org) did the initial coding.
