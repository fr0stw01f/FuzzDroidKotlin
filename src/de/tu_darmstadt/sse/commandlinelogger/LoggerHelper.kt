package de.tu_darmstadt.sse.commandlinelogger

import de.tu_darmstadt.sse.FrameworkOptions
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.*
import java.util.logging.Formatter

object LoggerHelper {
    private val log = Logger.getLogger(LoggerHelper::class.java.name)
    private var fh: FileHandler? = null

    fun logInfo(message: String) {
        log.log(Level.INFO, message)
        if (fh != null)
            fh!!.flush()
    }

    fun logWarning(message: String) {
        //		log.log(Level.WARNING, message);
        System.err.println(message)
        if (fh != null)
            fh!!.flush()
    }

    fun logEvent(level: Level, message: String) {
        log.log(level, message)
        if (fh != null)
            fh!!.flush()
    }

    fun initialize(apkPath: String) {
        log.useParentHandlers = false
        val conHdlr = ConsoleHandler()
        conHdlr.formatter = object : Formatter() {
            override fun format(record: LogRecord): String {
                val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(record.millis))
                val sb = StringBuilder()
                sb.append("[")
                sb.append(timestamp + " - " + record.level)
                sb.append("] ")
                sb.append(record.message)
                sb.append("\n")
                return sb.toString()
            }
        }
        log.addHandler(conHdlr)

        //analysis results:
        try {
            val resultsDir = File(FrameworkOptions.resultDir)
            if (!resultsDir.exists())
                resultsDir.mkdir()
            val logFile = String.format("%s/%s.xml", resultsDir.absolutePath, FrameworkOptions.apkMD5)
            fh = FileHandler(logFile)
        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        log.addHandler(fh)
    }
}
