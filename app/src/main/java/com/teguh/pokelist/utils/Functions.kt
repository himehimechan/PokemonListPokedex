package com.teguh.pokelist.utils

import android.graphics.BitmapFactory
import androidx.palette.graphics.Palette
import java.io.IOException
import java.net.URL

class Functions {
    fun getColorPaletteFromUrl(imageUrl: String, callback: (Palette?) -> Unit) {
        Thread {
            try {
                val url = URL(imageUrl)
                val connection = url.openConnection()
                connection.doInput = true
                connection.connect()

                val inputStream = connection.getInputStream()
                val bitmap = BitmapFactory.decodeStream(inputStream)

                Palette.from(bitmap).generate { palette ->
                    callback(palette)
                }

                inputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
                callback(null)
            }
        }.start()
    }
}