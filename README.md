# Cyclists of Madison
Source code for the Twitter bot [Cyclists of Madison](https://twitter.com/cyclists_of_msn) developed by [Madison Bikes](https://madisonbikes.org).

## Overview
This bot is designed to post a photo a day to a Twitter account from a pool of photographs. There are several configurable features:
* It can be run using a simple cron job and setup completely using command line tools.
* It will do its best to avoid repeating the same photo.
* It will try to pick photos from around the same time of year as the current date.
* It has a capability to introduce a random delay so that the post is not made at the same time every day.
* It will strip out any unnecessary image metadata before it is posted, for privacy.

## Usage
We suggest a starting pool size of around 100 photos, but that is up to you.

TODO
