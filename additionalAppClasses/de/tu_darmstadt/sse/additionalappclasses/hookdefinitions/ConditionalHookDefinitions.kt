package de.tu_darmstadt.sse.additionalappclasses.hookdefinitions

import java.util.HashSet

import android.telephony.PhoneNumberUtils
import android.util.Log

import com.morgoo.hook.zhook.MethodHook.MethodHookParam

import dalvik.system.DexClassLoader
import de.tu_darmstadt.sse.additionalappclasses.hooking.Condition
import de.tu_darmstadt.sse.additionalappclasses.hooking.HookInfo
import de.tu_darmstadt.sse.additionalappclasses.hooking.MethodHookInfo
import de.tu_darmstadt.sse.additionalappclasses.hooking.ParameterConditionValueInfo

class ConditionalHookDefinitions : Hook {

    override fun initializeHooks(): Set<HookInfo> {
        val allConditionalHooks = HashSet<HookInfo>()
        // allConditionalHooks.addAll(fileSpecificEmulatorChecks());
        // allConditionalHooks.addAll(systemPropEmulatorChecks());
        // allConditionalHooks.addAll(appSpecificEmulatorChecks());
        allConditionalHooks.addAll(textMessageCrashPrevention())
        // allConditionalHooks.addAll(reflectionHooks());
        return allConditionalHooks
    }


    private fun textMessageCrashPrevention(): Set<HookInfo> {
        val textMessageHooks = HashSet<HookInfo>()

        val smsManagerSendTextMessage = MethodHookInfo("<android.telephony.SmsManager: void sendTextMessage(java.lang.String, java.lang.String, java.lang.String, android.app.PendingIntent, android.app.PendingIntent)>")
        val parameterInfos1 = HashSet<ParameterConditionValueInfo>()
        val arg1 = ParameterConditionValueInfo(0, Condition { param ->
            if (!PhoneNumberUtils.isGlobalPhoneNumber(param.args[0] as String))
                return@Condition true
            false
        }, "555555")
        parameterInfos1.add(arg1)
        smsManagerSendTextMessage.conditionDependentHookBefore(parameterInfos1)
        textMessageHooks.add(smsManagerSendTextMessage)

        val smsManagerSendMultipartTextMessage = MethodHookInfo("<android.telephony.SmsManager: void sendMultipartTextMessage(java.lang.String, java.lang.String, java.util.ArrayList, java.util.ArrayList, java.util.ArrayList)>")
        val parameterInfos2 = HashSet<ParameterConditionValueInfo>()
        val arg2 = ParameterConditionValueInfo(0, Condition { param ->
            if (!PhoneNumberUtils.isGlobalPhoneNumber(param.args[0] as String))
                return@Condition true
            false
        }, "555555")
        parameterInfos2.add(arg2)
        smsManagerSendMultipartTextMessage.conditionDependentHookBefore(parameterInfos2)
        textMessageHooks.add(smsManagerSendMultipartTextMessage)

        val gsmSendTextMessage = MethodHookInfo("<android.telephony.gsm.SmsManager: void sendTextMessage(java.lang.String, java.lang.String, java.lang.String, android.app.PendingIntent, android.app.PendingIntent)>")
        val parameterInfos3 = HashSet<ParameterConditionValueInfo>()
        val arg3 = ParameterConditionValueInfo(0, Condition { param ->
            if (!PhoneNumberUtils.isGlobalPhoneNumber(param.args[0] as String))
                return@Condition true
            false
        }, "555555")
        parameterInfos3.add(arg3)
        gsmSendTextMessage.conditionDependentHookBefore(parameterInfos3)
        textMessageHooks.add(gsmSendTextMessage)

        val gsmSendMultiTextMessage = MethodHookInfo("<android.telephony.gsm.SmsManager: void sendMultipartTextMessage(java.lang.String, java.lang.String, java.util.ArrayList, java.util.ArrayList, java.util.ArrayList)>")
        val parameterInfos4 = HashSet<ParameterConditionValueInfo>()
        val arg4 = ParameterConditionValueInfo(0, Condition { param ->
            if (!PhoneNumberUtils.isGlobalPhoneNumber(param.args[0] as String))
                return@Condition true
            false
        }, "555555")
        parameterInfos4.add(arg4)
        gsmSendMultiTextMessage.conditionDependentHookBefore(parameterInfos4)
        textMessageHooks.add(gsmSendMultiTextMessage)

        return textMessageHooks
    }


    private fun fileSpecificEmulatorChecks(): Set<HookInfo> {
        val fileHooks = HashSet<HookInfo>()

        val exists = MethodHookInfo("<java.io.File: boolean exists()>")
        //qemu_pipe check
        exists.conditionDependentHookAfter(Condition { param ->
            if (param.thisObject.toString() == "/dev/qemu_pipe")
                return@Condition true
            false
        }, false)
        //qemud check
        exists.conditionDependentHookAfter(Condition { param ->
            if (param.thisObject.toString() == "/dev/socket/qemud")
                return@Condition true
            false
        }, false)
        //goldfish check
        exists.conditionDependentHookAfter(Condition { param ->
            if (param.thisObject.toString() == "/init.goldfish.rc")
                return@Condition true
            false
        }, false)
        //qemu_trace check
        exists.conditionDependentHookAfter(Condition { param ->
            if (param.thisObject.toString() == "/sys/qemu_trace")
                return@Condition true
            false
        }, false)


        fileHooks.add(exists)
        return fileHooks
    }


    private fun systemPropEmulatorChecks(): Set<HookInfo> {
        val systemPropHooks = HashSet<HookInfo>()

        val systemPropGet = MethodHookInfo("<android.os.SystemProperties: java.lang.String get(java.lang.String)>")
        //gsm.sim.operator.alpha check
        systemPropGet.conditionDependentHookAfter(Condition { param ->
            if (param.args[0].toString() == "gsm.sim.operator.alpha")
                return@Condition true
            false
        }, "T-mobile D")
        //gsm.operator.numeric check
        systemPropGet.conditionDependentHookAfter(Condition { param ->
            if (param.args[0].toString() == "gsm.operator.numeric")
                return@Condition true
            false
        }, "26201")
        //gsm.sim.operator.numeric check
        systemPropGet.conditionDependentHookAfter(Condition { param ->
            if (param.args[0].toString() == "gsm.sim.operator.numeric")
                return@Condition true
            false
        }, "8923440000000000003")
        //gsm.version.ril-impl check
        systemPropGet.conditionDependentHookAfter(Condition { param ->
            if (param.args[0].toString() == "gsm.version.ril-impl")
                return@Condition true
            false
        }, "")
        //ro.baseband check
        systemPropGet.conditionDependentHookAfter(Condition { param ->
            if (param.args[0].toString() == "ro.baseband")
                return@Condition true
            false
        }, "")
        //ro.bootloader check
        systemPropGet.conditionDependentHookAfter(Condition { param ->
            if (param.args[0].toString() == "ro.bootloader")
                return@Condition true
            false
        }, "PRIMEMD04")
        //ro.build.description check
        systemPropGet.conditionDependentHookAfter(Condition { param ->
            if (param.args[0].toString() == "ro.build.description")
                return@Condition true
            false
        }, "")
        //ro.build.display.id check
        systemPropGet.conditionDependentHookAfter(Condition { param ->
            if (param.args[0].toString() == "ro.build.display.id")
                return@Condition true
            false
        }, "JWR66Y")
        //ro.build.fingerprint check
        systemPropGet.conditionDependentHookAfter(Condition { param ->
            if (param.args[0].toString() == "ro.build.fingerprint")
                return@Condition true
            false
        }, "google/takju/maguro:4.3/JWR66Y/776638:user/release-keys")
        //ro.build.tags check
        systemPropGet.conditionDependentHookAfter(Condition { param ->
            if (param.args[0].toString() == "ro.build.tags")
                return@Condition true
            false
        }, "release-keys")
        //ro.build.user check
        systemPropGet.conditionDependentHookAfter(Condition { param ->
            if (param.args[0].toString() == "ro.build.user")
                return@Condition true
            false
        }, "android-build")
        //ro.hardware check
        systemPropGet.conditionDependentHookAfter(Condition { param ->
            if (param.args[0].toString() == "ro.hardware")
                return@Condition true
            false
        }, "tuna")
        //ro.product.board check
        systemPropGet.conditionDependentHookAfter(Condition { param ->
            if (param.args[0].toString() == "ro.product.board")
                return@Condition true
            false
        }, "tuna")
        //ro.product.brand check
        systemPropGet.conditionDependentHookAfter(Condition { param ->
            if (param.args[0].toString() == "ro.product.brand")
                return@Condition true
            false
        }, "google")
        //ro.product.device check
        systemPropGet.conditionDependentHookAfter(Condition { param ->
            if (param.args[0].toString() == "ro.product.device")
                return@Condition true
            false
        }, "maguro")
        //ro.product.manufacturer check
        systemPropGet.conditionDependentHookAfter(Condition { param ->
            if (param.args[0].toString() == "ro.product.manufacturer")
                return@Condition true
            false
        }, "samsung")
        //ro.product.name check
        systemPropGet.conditionDependentHookAfter(Condition { param ->
            if (param.args[0].toString() == "ro.product.name")
                return@Condition true
            false
        }, "takju")
        //ro.serialno check
        systemPropGet.conditionDependentHookAfter(Condition { param ->
            if (param.args[0].toString() == "ro.serialno")
                return@Condition true
            false
        }, "0149E08209007013")
        //ro.setupwizard.mode check
        systemPropGet.conditionDependentHookAfter(Condition { param ->
            if (param.args[0].toString() == "ro.setupwizard.mode")
                return@Condition true
            false
        }, "")
        //ro.build.type check
        systemPropGet.conditionDependentHookAfter(Condition { param ->
            if (param.args[0].toString() == "ro.build.type")
                return@Condition true
            false
        }, "user")
        //ARGH check
        systemPropGet.conditionDependentHookAfter(Condition { param ->
            if (param.args[0].toString() == "ARGH")
                return@Condition true
            false
        }, "")
        //init.svc.goldfish-logcat check
        systemPropGet.conditionDependentHookAfter(Condition { param ->
            if (param.args[0].toString() == "init.svc.goldfish-logcat")
                return@Condition true
            false
        }, "")
        //init.svc.goldfish-setup check
        systemPropGet.conditionDependentHookAfter(Condition { param ->
            if (param.args[0].toString() == "init.svc.goldfish-setup")
                return@Condition true
            false
        }, "")
        //init.svc.qemud check
        systemPropGet.conditionDependentHookAfter(Condition { param ->
            if (param.args[0].toString() == "init.svc.qemud")
                return@Condition true
            false
        }, "")
        //qemu.hw.mainkeys check
        systemPropGet.conditionDependentHookAfter(Condition { param ->
            if (param.args[0].toString() == "qemu.hw.mainkeys")
                return@Condition true
            false
        }, "")
        //init.svc.qemu-props check
        systemPropGet.conditionDependentHookAfter(Condition { param ->
            if (param.args[0].toString() == "init.svc.qemu-props")
                return@Condition true
            false
        }, "")
        //qemu.sf.fake_camera check
        systemPropGet.conditionDependentHookAfter(Condition { param ->
            if (param.args[0].toString() == "qemu.sf.fake_camera")
                return@Condition true
            false
        }, "")
        //qemu.sf.lcd_density check
        systemPropGet.conditionDependentHookAfter(Condition { param ->
            if (param.args[0].toString() == "qemu.sf.lcd_density")
                return@Condition true
            false
        }, "")
        //ro.kernel.android.checkjni check
        systemPropGet.conditionDependentHookAfter(Condition { param ->
            if (param.args[0].toString() == "ro.kernel.android.checkjni")
                return@Condition true
            false
        }, "")
        //ro.kernel.android.qemud check
        systemPropGet.conditionDependentHookAfter(Condition { param ->
            if (param.args[0].toString() == "ro.kernel.android.qemud")
                return@Condition true
            false
        }, "")
        //ro.kernel.console check
        systemPropGet.conditionDependentHookAfter(Condition { param ->
            if (param.args[0].toString() == "ro.kernel.console")
                return@Condition true
            false
        }, "")
        //ro.kernel.ndns check
        systemPropGet.conditionDependentHookAfter(Condition { param ->
            if (param.args[0].toString() == "ro.kernel.ndns")
                return@Condition true
            false
        }, "")
        //ro.kernel.qemu.gles check
        systemPropGet.conditionDependentHookAfter(Condition { param ->
            if (param.args[0].toString() == "ro.kernel.qemu.gles")
                return@Condition true
            false
        }, "")
        //ro.kernel.qemu check
        systemPropGet.conditionDependentHookAfter(Condition { param ->
            if (param.args[0].toString() == "ro.kernel.qemu")
                return@Condition true
            false
        }, "")

        systemPropHooks.add(systemPropGet)

        return systemPropHooks
    }


    private fun appSpecificEmulatorChecks(): Set<HookInfo> {
        val appSpecificHooks = HashSet<HookInfo>()

        val addCategory = MethodHookInfo("<android.content.Intent: android.content.Intent addCategory(java.lang.String)>")

        val parameterInfos = HashSet<ParameterConditionValueInfo>()
        val arg1 = ParameterConditionValueInfo(0, Condition { param ->
            if (param.args[0].toString() == "android.intent.category.APP_MARKET")
                return@Condition true
            false
        }, "android.intent.category.LAUNCHER")
        parameterInfos.add(arg1)
        addCategory.conditionDependentHookBefore(parameterInfos)

        appSpecificHooks.add(addCategory)

        return appSpecificHooks
    }

    private fun reflectionHooks(): Set<HookInfo> {
        val reflectionHooks = HashSet<HookInfo>()

        val dexClassLoaderLoadClass = MethodHookInfo("<dalvik.system.DexClassLoader: java.lang.Class loadClass(java.lang.String)>")
        val parameterInfos = HashSet<ParameterConditionValueInfo>()
        val arg0 = ParameterConditionValueInfo(0, Condition { param ->
            //we check if there is a class available
            try {
                Log.i("SSE1", "in loadClass")
                val dcl = param.thisObject as DexClassLoader
                dcl.loadClass(param.args[0] as String)
            } catch (ex: Exception) {
                return@Condition true
            }

            false
        }, "de.tu_darmstadt.sse.additionalappclasses.reflections.DummyReflectionClass")
        parameterInfos.add(arg0)
        dexClassLoaderLoadClass.conditionDependentHookBefore(parameterInfos)
        reflectionHooks.add(dexClassLoaderLoadClass)

        val classGetMethod = MethodHookInfo("<java.lang.Class: java.lang.reflect.Method getMethod(java.lang.String,java.lang.Class[])>")
        val parameterInfosClassGetMethod = HashSet<ParameterConditionValueInfo>()
        val classGetMethodArg0 = ParameterConditionValueInfo(0, Condition { param ->
            //we check if there is a class available
            try {
                Log.i("SSE1", "in getMethod")
                val clazz = param.thisObject as Class<*>
                clazz.getMethod(param.args[0] as String, *param.args[1] as Array<Class<*>>)
            } catch (ex: Exception) {
                return@Condition true
            }

            false
        }, "dummyReflectionMethod")

        val classGetMethodArg1 = ParameterConditionValueInfo(1, Condition { param ->
            //we check if there is a class available
            try {
                val clazz = param.thisObject as Class<*>
                clazz.getMethod(param.args[0] as String, *param.args[1] as Array<Class<*>>)
            } catch (ex: Exception) {
                return@Condition true
            }

            false
        }, null)

        parameterInfosClassGetMethod.add(classGetMethodArg0)
        parameterInfosClassGetMethod.add(classGetMethodArg1)
        classGetMethod.conditionDependentHookBefore(parameterInfosClassGetMethod)
        reflectionHooks.add(classGetMethod)

        return reflectionHooks
    }
}
