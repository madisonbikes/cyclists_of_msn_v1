# Cyclists of Madison
Source code for the Twitter bot [Cyclists of Madison](https://twitter.com/cyclists_of_msn) developed by [Madison Bikes](https://madisonbikes.org).

## Overview
This bot posts one photo each day to a Twitter account from a pool of photographs. There are several configurable features:
* It can be run using a simple cron job and setup completely using command line tools.
* It will do its best to avoid repeating the same photo.
* It will try to pick photos from around the same time of year as the current date through the use of metadata in the images.
* It can introduce a random delay so that the post doesn't happen at the same time every day.
* It will strip out any unnecessary image metadata before it is posted, for privacy.

## Requirements
To run the bot, you should have a computer connected to the internet on regular basis, where you can schedule tasks to run periodically. On UNIX-based systems (e.g. Linux) this is called a `cron` job.

We suggest a starting pool size of around 100 photos, but that is up to you.

It is developed using the Kotlin language and running the bot tools requires that there be a recent Java runtime installed. It is developed and tested on Linux but should run on OS X and/or Windows.

It uses [ImageMagick](https://imagemagick.org/) for some of the image manipulation, so that needs to be pre-installed on whatever server is hosting the repository and running the commands.

You need to have a Twitter developer account in order to create your bot. Go to the [Twitter Developer site](https://developer.twitter.com) for more information about that.

## Build
To build, clone the repository and execute this command (if you trust our included [Gradle](https://gradle.org) wrapper):
```
# ./gradlew assemble
```
or to use your own Gradle 6.3 (or higher) installation:
```
# gradle assemble
```

## Installation
After building, the artifacts will be in `build/distributions` in `.tar.gz` and `.zip` formats (example: `cyclistsofmadisonbot-0.1-SNAPSHOT.tar.gz`). You can move these to your server and extract them to any location you like. The `bin` directory contains scripts that can be used to execute the two primary tasks.

## Usage
The bot consists of two primary commands:
1. The `register` command, which helps establish the link between this installed tool and a Twitter account of your choosing. This is done only one time at setup. You supply the Twitter Consumer API key for your bot and the API secret key, follow on-screen instructions, and a configuration file is created that can be used for the `post` command.
2. The `post` tool, which chooses a photo from the pool, prepares a post and sends it to Twitter. Normally this is run from a cron job, once per day. Use the `--dry-run` argument for testing purposes.

To run the tasks, use the `cyclistsofmadisonbot` command line tool (exact output may not be up-to-date in the README):
```
# ./bin/cyclistsofmadisonbot --help
usage: cyclistsofmadisonbot [-h] {register,post} ...

positional arguments:
  {register,post}

named arguments:
  -h, --help             show this help message and exit
```

## Credits
Harald Kliems (harald@madisonbikes.org) had the original idea and requirements to build this and Ben Sandee (ben@madisonbikes.org) did the initial coding.
