package de.tu_darmstadt.sse.additionalappclasses

import android.app.Application
import android.content.Context
import de.tu_darmstadt.sse.additionalappclasses.hooking.Hooker

class HookingHelperApplication : Application() {

    override fun attachBaseContext(context: Context) {
        super.attachBaseContext(context)
        Hooker.initializeHooking(context)
    }

}
