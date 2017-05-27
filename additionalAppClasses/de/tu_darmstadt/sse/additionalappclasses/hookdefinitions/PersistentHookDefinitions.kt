package de.tu_darmstadt.sse.additionalappclasses.hookdefinitions

import java.lang.reflect.Constructor
import java.lang.reflect.Method
import java.util.Collections
import java.util.HashSet

import android.net.NetworkInfo.DetailedState
import de.tu_darmstadt.sse.additionalappclasses.hooking.FieldHookInfo
import de.tu_darmstadt.sse.additionalappclasses.hooking.HookInfo
import de.tu_darmstadt.sse.additionalappclasses.hooking.MethodHookInfo
import de.tu_darmstadt.sse.sharedclasses.util.Pair


class PersistentHookDefinitions : Hook {

    override fun initializeHooks(): Set<HookInfo> {
        val allPersistentHooks = HashSet<HookInfo>()
        //		allPersistentHooks.addAll(emulatorCheckHooks());
        //		allPersistentHooks.addAll(networkRelatedHooks());
        return allPersistentHooks
    }

    private fun emulatorCheckHooks(): Set<HookInfo> {
        val emulatorHoocks = HashSet<HookInfo>()

        emulatorHoocks.addAll(buildSpecificEmulatorCheckHooks())
        emulatorHoocks.addAll(telephonyManagerSpecificEmulatorCheckHooks())
        emulatorHoocks.addAll(applicationPackageManagerEmulatorCheckHooks())

        return emulatorHoocks
    }

    private fun buildSpecificEmulatorCheckHooks(): Set<HookInfo> {
        val buildSpecificEmulatorCheckHooks = HashSet<HookInfo>()

        val build_abi = FieldHookInfo("<android.os.Build: java.lang.String CPU_ABI>")
        build_abi.persistentHookAfter("armeabi-v7a")
        val build_abi2 = FieldHookInfo("<android.os.Build: java.lang.String CPU_ABI2>")
        build_abi2.persistentHookAfter("armeabi")
        val build_board = FieldHookInfo("<android.os.Build: java.lang.String BOARD>")
        build_board.persistentHookAfter("MAKO")
        val build_brand = FieldHookInfo("<android.os.Build: java.lang.String BRAND>")
        build_brand.persistentHookAfter("google")
        val build_device = FieldHookInfo("<android.os.Build: java.lang.String DEVICE>")
        build_device.persistentHookAfter("mako")
        val build_fingerprint = FieldHookInfo("<android.os.Build: java.lang.String FINGERPRINT>")
        build_fingerprint.persistentHookAfter("google/occam/mako:4.4.2/KOT49H/937116:user/release-keys")
        val build_hardware = FieldHookInfo("<android.os.Build: java.lang.String HARDWARE>")
        build_hardware.persistentHookAfter("mako")
        val build_host = FieldHookInfo("<android.os.Build: java.lang.String HOST>")
        build_host.persistentHookAfter("kpfj3.cbf.corp.google.com")
        val build_id = FieldHookInfo("<android.os.Build: java.lang.String ID>")
        build_id.persistentHookAfter("KOT49H")
        val build_manufacturer = FieldHookInfo("<android.os.Build: java.lang.String MANUFACTURER>")
        build_manufacturer.persistentHookAfter("LGE")
        val build_model = FieldHookInfo("<android.os.Build: java.lang.String MODEL>")
        build_model.persistentHookAfter("Nexus 4")
        val build_product = FieldHookInfo("<android.os.Build: java.lang.String PRODUCT>")
        build_product.persistentHookAfter("occam")
        val build_radio = FieldHookInfo("<android.os.Build: java.lang.String RADIO>")
        build_radio.persistentHookAfter("unknown")
        val build_serial = FieldHookInfo("<android.os.Build: java.lang.String SERIAL>")
        build_serial.persistentHookAfter("016ff0251853784a")
        val build_tags = FieldHookInfo("<android.os.Build: java.lang.String TAGS>")
        build_tags.persistentHookAfter("release-keys")
        val build_user = FieldHookInfo("<android.os.Build: java.lang.String USER>")
        build_user.persistentHookAfter("android-build")


        buildSpecificEmulatorCheckHooks.add(build_abi)
        buildSpecificEmulatorCheckHooks.add(build_abi2)
        buildSpecificEmulatorCheckHooks.add(build_board)
        buildSpecificEmulatorCheckHooks.add(build_brand)
        buildSpecificEmulatorCheckHooks.add(build_device)
        buildSpecificEmulatorCheckHooks.add(build_fingerprint)
        buildSpecificEmulatorCheckHooks.add(build_hardware)
        buildSpecificEmulatorCheckHooks.add(build_host)
        buildSpecificEmulatorCheckHooks.add(build_id)
        buildSpecificEmulatorCheckHooks.add(build_manufacturer)
        buildSpecificEmulatorCheckHooks.add(build_model)
        buildSpecificEmulatorCheckHooks.add(build_product)
        buildSpecificEmulatorCheckHooks.add(build_radio)
        buildSpecificEmulatorCheckHooks.add(build_serial)
        buildSpecificEmulatorCheckHooks.add(build_tags)
        buildSpecificEmulatorCheckHooks.add(build_user)

        return buildSpecificEmulatorCheckHooks
    }


    private fun telephonyManagerSpecificEmulatorCheckHooks(): Set<HookInfo> {
        val telephonyManagerSpecificEmulatorCheckHooks = HashSet<HookInfo>()

        // MethodHookInfo deviceId = new MethodHookInfo("<android.telephony.TelephonyManager: java.lang.String getDeviceId()>");
        // deviceId.persistentHookAfter("353918056991322");
        val line1Number = MethodHookInfo("<android.telephony.TelephonyManager: java.lang.String getLine1Number()>")
        line1Number.persistentHookAfter("")
        val simSerial = MethodHookInfo("<android.telephony.TelephonyManager: java.lang.String getSimSerialNumber()>")
        line1Number.persistentHookAfter("8923440000000000003")
        val softwareVersion = MethodHookInfo("<android.telephony.TelephonyManager: java.lang.String getDeviceSoftwareVersion()>")
        line1Number.persistentHookAfter("57")
        val countryIso = MethodHookInfo("<android.telephony.TelephonyManager: java.lang.String getNetworkCountryIso()>")
        countryIso.persistentHookAfter("de")
        val networkOperator = MethodHookInfo("<android.telephony.TelephonyManager: java.lang.String getNetworkOperator()>")
        networkOperator.persistentHookAfter("26207")
        val simCountryIso = MethodHookInfo("<android.telephony.TelephonyManager: java.lang.String getSimCountryIso()>")
        simCountryIso.persistentHookAfter("de")
        val voiceMail = MethodHookInfo("<android.telephony.TelephonyManager: java.lang.String getVoiceMailNumber()>")
        voiceMail.persistentHookAfter("+491793000333")
        val phoneType = MethodHookInfo("<android.telephony.TelephonyManager: java.lang.String getPhoneType()>")
        phoneType.persistentHookAfter("1")
        val networkType = MethodHookInfo("<android.telephony.TelephonyManager: java.lang.String getNetworkType()>")
        phoneType.persistentHookAfter("1")


        // telephonyManagerSpecificEmulatorCheckHooks.add(deviceId);
        telephonyManagerSpecificEmulatorCheckHooks.add(line1Number)
        telephonyManagerSpecificEmulatorCheckHooks.add(simSerial)
        telephonyManagerSpecificEmulatorCheckHooks.add(softwareVersion)
        telephonyManagerSpecificEmulatorCheckHooks.add(countryIso)
        telephonyManagerSpecificEmulatorCheckHooks.add(networkOperator)
        telephonyManagerSpecificEmulatorCheckHooks.add(simCountryIso)
        telephonyManagerSpecificEmulatorCheckHooks.add(voiceMail)
        telephonyManagerSpecificEmulatorCheckHooks.add(phoneType)
        telephonyManagerSpecificEmulatorCheckHooks.add(networkType)

        return telephonyManagerSpecificEmulatorCheckHooks
    }


    private fun applicationPackageManagerEmulatorCheckHooks(): Set<HookInfo> {
        val appPackageMngHooks = HashSet<HookInfo>()

        val systemFeature = MethodHookInfo("<android.app.ApplicationPackageManager: boolean hasSystemFeature(java.lang.String)>")
        systemFeature.persistentHookAfter(true)

        appPackageMngHooks.add(systemFeature)

        return appPackageMngHooks
    }


    // TIMING BOMBS are covered by a bytecode tranformer (TimingBombTransformer)
    //
    //	private Set<HookInfo> timingBombHooks() {
    //		Set<HookInfo> timingBombHooks = new HashSet<HookInfo>();
    //
    //		MethodHookInfo setRepeating = new MethodHookInfo("<android.app.AlarmManager: void set(int,long,android.app.PendingIntent)>");
    //		Pair<Integer, Object> paramTime = new Pair<Integer, Object>(1, 2000L);
    //		setRepeating.persistentHookBefore(Collections.singleton(paramTime));
    //
    //		//yes, this is not a conditional hook, but it belongs to the cat timingbombs
    //		MethodHookInfo hPostDelayed = new MethodHookInfo("<android.os.Handler: boolean postDelayed(java.lang.Runnable,long)>");
    //		Set<ParameterConditionValueInfo> parameterInfos1 = new HashSet<ParameterConditionValueInfo>();
    //		ParameterConditionValueInfo arg1 = new ParameterConditionValueInfo(1, new Condition() {
    //			@Override
    //			public boolean isConditionSatisfied(MethodHookParam param) {
    //
    //				if(!param.args[0].toString().startsWith("de.tu_darmstadt.sse.additionalappclasses.tracing")) {
    //					return true;
    //				}
    //				return false;
    //			}
    //		}, 2000L);
    //		parameterInfos1.add(arg1);
    //		hPostDelayed.conditionDependentHookBefore(parameterInfos1);
    //
    //
    ////		timingBombHooks.add(setRepeating);
    //		timingBombHooks.add(hPostDelayed);
    //		return timingBombHooks;
    //	}


    private fun networkRelatedHooks(): Set<HookInfo> {
        val networkHooks = HashSet<HookInfo>()
        val getActiveNetworkInfo = MethodHookInfo("<android.net.ConnectivityManager: android.net.NetworkInfo getActiveNetworkInfo()>")
        try {
            val networkInfo = Class.forName("android.net.NetworkInfo")
            val networkInfoParams = arrayOfNulls<Class<*>>(4)
            networkInfoParams[0] = Int::class.javaPrimitiveType
            networkInfoParams[1] = Int::class.javaPrimitiveType
            networkInfoParams[2] = String::class.java
            networkInfoParams[3] = String::class.java
            val init = networkInfo.getConstructor(*networkInfoParams)
            init.isAccessible = true
            val obj = init.newInstance(0, 3, "mobile", "UMTS")
            val booleanParam = arrayOfNulls<Class<*>>(1)
            booleanParam[0] = Boolean::class.javaPrimitiveType
            val setIsAvailable = networkInfo.getMethod("setIsAvailable", *booleanParam)
            setIsAvailable.isAccessible = true
            setIsAvailable.invoke(obj, true)
            val setIsConnectedToProvisioningNetwork = networkInfo.getMethod("setIsConnectedToProvisioningNetwork", *booleanParam)
            setIsConnectedToProvisioningNetwork.isAccessible = true
            setIsConnectedToProvisioningNetwork.invoke(obj, true)
            val setRoaming = networkInfo.getMethod("setRoaming", *booleanParam)
            setRoaming.isAccessible = true
            setRoaming.invoke(obj, true)
            val setDetailedStateParams = arrayOfNulls<Class<*>>(3)
            setDetailedStateParams[0] = DetailedState::class.java
            setDetailedStateParams[1] = String::class.java
            setDetailedStateParams[2] = String::class.java
            val setDetailedState = networkInfo.getMethod("setDetailedState", *setDetailedStateParams)
            setDetailedState.isAccessible = true
            setDetailedState.invoke(obj, DetailedState.CONNECTED, "connected", "epc.tmobile.com")
            getActiveNetworkInfo.persistentHookAfter(obj)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        networkHooks.add(getActiveNetworkInfo)
        return networkHooks
    }
}
