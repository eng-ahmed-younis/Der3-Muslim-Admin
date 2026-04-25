package com.der3.der3admin.utils

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream


suspend fun bitmapToStringSafe(bitmap: Bitmap?): String = withContext(Dispatchers.IO) {
    try {
        bitmap?.let { bmp ->
            // Resize bitmap FIRST (critical!)
            val maxSize = 800 // Max width/height in pixels
            val resizedBitmap = if (bmp.width > maxSize || bmp.height > maxSize) {
                val scale = minOf(maxSize.toFloat() / bmp.width, maxSize.toFloat() / bmp.height)
                val newWidth = (bmp.width * scale).toInt()
                val newHeight = (bmp.height * scale).toInt()
                Bitmap.createScaledBitmap(bmp, newWidth, newHeight, true)
            } else {
                bmp
            }

            // Use JPEG with 70% quality instead of PNG 100%
            val outputStream = ByteArrayOutputStream()
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)

            // Use NO_WRAP to reduce string size
            val result = Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)

            // Recycle bitmap if we created a new one
            if (resizedBitmap != bmp) {
                resizedBitmap.recycle()
            }

            Log.d("ImageToString", "Size: ${result.length / 1024} KB")
            result
        } ?: ""
    } catch (e: Exception) {
        Log.e("ImageToString", "Failed to convert bitmap", e)
        ""
    }
}