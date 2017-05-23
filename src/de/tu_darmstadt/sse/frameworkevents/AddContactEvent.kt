package de.tu_darmstadt.sse.frameworkevents

import java.util.concurrent.TimeUnit

import com.android.ddmlib.IDevice

import de.tu_darmstadt.sse.appinstrumentation.UtilInstrumenter
import de.tu_darmstadt.sse.commandlinelogger.LoggerHelper
import de.tu_darmstadt.sse.commandlinelogger.MyLevel


class AddContactEvent(private val packageName: String) : FrameworkEvent() {

    override fun onEventReceived(device: IDevice): Any? {
        val shellCmd = String.format("am startservice --es \"task\" \"addContact\" -n %s/%s",
                packageName, UtilInstrumenter.COMPONENT_CALLER_SERVICE_HELPER)
        try {
            device.executeShellCommand(shellCmd, GenericReceiver(), 10000, TimeUnit.MILLISECONDS)
            LoggerHelper.logEvent(MyLevel.ADB_EVENT, adbEventFormat(toString(), "contact added..."))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    override fun toString(): String {
        return "AddContactEvent"
    }
}
