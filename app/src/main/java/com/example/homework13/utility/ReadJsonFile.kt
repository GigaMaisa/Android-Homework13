package com.example.homework13.utility

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception

class ReadJsonFile {
    fun readFile(context: Context, path: String): String? {
        val file = context.assets.open(path)
        val bufferedReader = BufferedReader(InputStreamReader(file))

        try {
            val stringBuilder = StringBuilder()
            bufferedReader.useLines { lines ->
                lines.forEach {
                    stringBuilder.append(it)
                }
            }
            return stringBuilder.toString()

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            bufferedReader.close()
            file.close()
        }
        return null
    }
}