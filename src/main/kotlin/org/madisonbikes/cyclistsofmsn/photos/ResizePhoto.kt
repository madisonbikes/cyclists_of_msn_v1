package org.madisonbikes.cyclistsofmsn.photos

import java.io.File
import java.util.concurrent.TimeUnit

object ResizePhoto {
    /** resizes and strips the photo of metadata */
    fun withMaximumWidth(input: File, outputFile: File, maximumWidth: Int) {
        val args = arrayOf(
            "convert",
            input.absolutePath,
            "-resize",
            "${maximumWidth}x>",
            "-strip",
            outputFile.absolutePath
        )
        val process = Runtime.getRuntime().exec(args)
        check(process.waitFor(10, TimeUnit.SECONDS)) {
            throw Exception("Conversion timed out args=$args")
        }
        val exitValue = process.exitValue()
        check(process.exitValue() == 0) {
            "Conversion failed, exitValue=$exitValue args=$args"
        }
    }
}