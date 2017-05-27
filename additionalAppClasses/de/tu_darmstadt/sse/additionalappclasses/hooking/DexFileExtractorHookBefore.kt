package de.tu_darmstadt.sse.additionalappclasses.hooking

import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException

import android.util.Log
import de.tu_darmstadt.sse.additionalappclasses.tracing.BytecodeLogger
import de.tu_darmstadt.sse.sharedclasses.SharedClassesSettings
import de.tu_darmstadt.sse.sharedclasses.util.Pair


class DexFileExtractorHookBefore(private val methodSignature: String, val argumentPosition: Int) : AbstractMethodHookBefore() {


    fun sendDexFileToServer(dexFilePath: String) {
        val dexFile = File(dexFilePath)
        val dexFileBytes = convertFileToByteArray(dexFile)
        BytecodeLogger.sendDexFileToServer(dexFilePath, dexFileBytes!!)
        Log.i(SharedClassesSettings.TAG, "dex file sent to client")
    }

    override fun getParamValuesToReplace(): Set<Pair<Int, Any>>? {
        // there is no need to replace any parameters
        return null
    }

    override fun isValueReplacementNecessary(): Boolean {
        //there is no need to replace anything
        return false
    }

    private fun convertFileToByteArray(dexFile: File): ByteArray? {
        var fin: FileInputStream? = null
        var fileContent: ByteArray? = null
        try {
            fin = FileInputStream(dexFile)
            fileContent = ByteArray(dexFile.length().toInt())
            fin.read(fileContent)
        } catch (e: FileNotFoundException) {
            Log.e(SharedClassesSettings.TAG, "File not found" + e)
        } catch (ioe: IOException) {
            Log.e(SharedClassesSettings.TAG, "Exception while reading file " + ioe)
        } finally {
            try {
                if (fin != null) {
                    fin.close()
                }
            } catch (ioe: IOException) {
                Log.e(SharedClassesSettings.TAG, "Error while closing stream: " + ioe)
            }

        }

        return fileContent
    }
}
