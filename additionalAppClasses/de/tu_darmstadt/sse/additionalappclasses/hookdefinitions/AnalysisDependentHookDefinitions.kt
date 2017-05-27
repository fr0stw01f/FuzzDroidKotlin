package de.tu_darmstadt.sse.additionalappclasses.hookdefinitions

import java.util.HashSet

import de.tu_darmstadt.sse.additionalappclasses.hooking.HookInfo
import de.tu_darmstadt.sse.additionalappclasses.hooking.MethodHookInfo

class AnalysisDependentHookDefinitions : Hook {

    override fun initializeHooks(): Set<HookInfo> {
        val allAnalysisDependentHooks = HashSet<HookInfo>()
        allAnalysisDependentHooks.addAll(telephonyManagerHooks())
        // allAnalysisDependentHooks.addAll(stringOperationHooks());
        // allAnalysisDependentHooks.addAll(applicationInfoHooks());
        allAnalysisDependentHooks.addAll(smsMessageHooks())
        allAnalysisDependentHooks.addAll(sharedPreferencesHooks())
        // allAnalysisDependentHooks.addAll(networkRelatedHooks());
        allAnalysisDependentHooks.addAll(integrityRelatedHooks())
        allAnalysisDependentHooks.addAll(fileRelatedHooks())
        allAnalysisDependentHooks.addAll(reflectionHooks())
        allAnalysisDependentHooks.addAll(deviceAdminHooks())
        // allAnalysisDependentHooks.addAll(intentBasedHooks());
        allAnalysisDependentHooks.addAll(xmlBasedHooks())
        allAnalysisDependentHooks.addAll(foregroundActivityCheckHooks())
        return allAnalysisDependentHooks
    }


    private fun telephonyManagerHooks(): Set<HookInfo> {
        val tmHoocks = HashSet<HookInfo>()

        val deviceId = MethodHookInfo("<android.telephony.TelephonyManager: java.lang.String getDeviceId()>")
        deviceId.analysisDependentHookAfter()
        val countryIso = MethodHookInfo("<android.telephony.TelephonyManager: java.lang.String getNetworkCountryIso()>")
        countryIso.analysisDependentHookAfter()
        val networkOperator = MethodHookInfo("<android.telephony.TelephonyManager: java.lang.String getNetworkOperator()>")
        networkOperator.analysisDependentHookAfter()
        val simCountryIso = MethodHookInfo("<android.telephony.TelephonyManager: java.lang.String getSimCountryIso()>")
        simCountryIso.analysisDependentHookAfter()
        val simSerial = MethodHookInfo("<android.telephony.TelephonyManager: java.lang.String getSimSerialNumber()>")
        simSerial.analysisDependentHookAfter()
        val voiceMail = MethodHookInfo("<android.telephony.TelephonyManager: java.lang.String getVoiceMailNumber()>")
        voiceMail.analysisDependentHookAfter()
        val phoneType = MethodHookInfo("<android.telephony.TelephonyManager: java.lang.String getPhoneType()>")
        phoneType.analysisDependentHookAfter()
        val networkType = MethodHookInfo("<android.telephony.TelephonyManager: java.lang.String getNetworkType()>")
        phoneType.analysisDependentHookAfter()
        val simOperator = MethodHookInfo("<android.telephony.TelephonyManager: java.lang.String getSimOperator()>")
        simOperator.analysisDependentHookAfter()
        val simOperatorName = MethodHookInfo("<android.telephony.TelephonyManager: java.lang.String getSimOperatorName()>")
        simOperatorName.analysisDependentHookAfter()

        tmHoocks.add(deviceId)
        tmHoocks.add(countryIso)
        tmHoocks.add(networkOperator)
        tmHoocks.add(simCountryIso)
        tmHoocks.add(simSerial)
        tmHoocks.add(voiceMail)
        tmHoocks.add(phoneType)
        tmHoocks.add(networkType)
        tmHoocks.add(simOperator)
        tmHoocks.add(simOperatorName)

        return tmHoocks
    }

    private fun intentBasedHooks(): Set<HookInfo> {
        val intentBasedHooks = HashSet<HookInfo>()

        val stringExtra = MethodHookInfo("<android.content.Intent: java.lang.String getStringExtra(java.lang.String)>")
        stringExtra.analysisDependentHookAfter()

        intentBasedHooks.add(stringExtra)
        return intentBasedHooks
    }

    private fun xmlBasedHooks(): Set<HookInfo> {
        val xmlBasedHooks = HashSet<HookInfo>()

        val attributeValue = MethodHookInfo("<org.xmlpull.v1.XmlPullParser: java.lang.String getAttributeValue(int)>")
        attributeValue.analysisDependentHookAfter()

        xmlBasedHooks.add(attributeValue)

        return xmlBasedHooks
    }


    private fun deviceAdminHooks(): Set<HookInfo> {
        val deviceAdminHoocks = HashSet<HookInfo>()

        val isAdminActive = MethodHookInfo("<android.app.admin.DevicePolicyManager: boolean isAdminActive(android.content.ComponentName)>")
        isAdminActive.analysisDependentHookAfter()

        deviceAdminHoocks.add(isAdminActive)

        return deviceAdminHoocks
    }


    private fun stringOperationHooks(): Set<HookInfo> {
        val stringOpHoocks = HashSet<HookInfo>()

        val charAt = MethodHookInfo("<java.lang.String: char charAt(int)>")
        charAt.analysisDependentHookAfter()
        val codePointAt = MethodHookInfo("<java.lang.String: int codePointAt(int)>")
        codePointAt.analysisDependentHookAfter()
        val codePointBefore = MethodHookInfo("<java.lang.String: int codePointBefore(int)>")
        codePointBefore.analysisDependentHookAfter()
        val codePointCount = MethodHookInfo("<java.lang.String: int codePointCount(int, int)>")
        codePointCount.analysisDependentHookAfter()
        val compareTo = MethodHookInfo("<java.lang.String: int compareTo(java.lang.String)>")
        compareTo.analysisDependentHookAfter()
        val compareToIgnoreCase = MethodHookInfo("<java.lang.String: int compareToIgnoreCase(java.lang.String)>")
        compareToIgnoreCase.analysisDependentHookAfter()
        val concat = MethodHookInfo("<java.lang.String: java.lang.String concat(java.lang.String)>")
        concat.analysisDependentHookAfter()
        val containsCharSequence = MethodHookInfo("<java.lang.String: boolean contains(java.lang.CharSequence)>")
        containsCharSequence.analysisDependentHookAfter()
        val contentEqualsCharSequence = MethodHookInfo("<java.lang.String: boolean contentEquals(java.lang.CharSequence)>")
        contentEqualsCharSequence.analysisDependentHookAfter()
        val contentEqualsStringBuffer = MethodHookInfo("<java.lang.String: boolean contentEquals(java.lang.StringBuffer)>")
        contentEqualsStringBuffer.analysisDependentHookAfter()
        val copyValueOf1 = MethodHookInfo("<java.lang.String: java.lang.String copyValueOf(char[], int, int)>")
        copyValueOf1.analysisDependentHookAfter()
        val copyValueOf2 = MethodHookInfo("<java.lang.String: java.lang.String copyValueOf(char[])>")
        copyValueOf2.analysisDependentHookAfter()
        val endsWith = MethodHookInfo("<java.lang.String: boolean endsWith(java.lang.String)>")
        endsWith.analysisDependentHookAfter()
        val equals = MethodHookInfo("<java.lang.String: boolean equals(java.lang.Object)>")
        equals.analysisDependentHookAfter()
        val equalsIgnoreCase = MethodHookInfo("<java.lang.String: boolean equalsIgnoreCase(java.lang.String)>")
        equalsIgnoreCase.analysisDependentHookAfter()
        val format1 = MethodHookInfo("<java.lang.String: java.lang.String format(java.util.Locale, java.lang.String, java.lang.Object[])>")
        format1.analysisDependentHookAfter()
        val format2 = MethodHookInfo("<java.lang.String: java.lang.String format(java.lang.String, java.lang.Object[])>")
        format2.analysisDependentHookAfter()
        val getBytes1 = MethodHookInfo("<java.lang.String: void getBytes(int, int, byte[], int)>")
        getBytes1.analysisDependentHookAfter()
        val getBytes2 = MethodHookInfo("<java.lang.String: byte[] getBytes(java.lang.String)>")
        getBytes2.analysisDependentHookAfter()
        val getBytes3 = MethodHookInfo("<java.lang.String: byte[] getBytes(java.nio.charset.Charset)>")
        getBytes3.analysisDependentHookAfter()
        val getBytes4 = MethodHookInfo("<java.lang.String: byte[] getBytes()>")
        getBytes4.analysisDependentHookAfter()
        val getChars = MethodHookInfo("<java.lang.String: void getChars(int, int, char[], int)>")
        getChars.analysisDependentHookAfter()
        val hashCode = MethodHookInfo("<java.lang.String: int hashCode()>")
        hashCode.analysisDependentHookAfter()
        val indexOf1 = MethodHookInfo("<java.lang.String: int indexOf(int)>")
        indexOf1.analysisDependentHookAfter()
        val indexOf2 = MethodHookInfo("<java.lang.String: int indexOf(int, int)>")
        indexOf2.analysisDependentHookAfter()
        val indexOf3 = MethodHookInfo("<java.lang.String: int indexOf(java.lang.String, int)>")
        indexOf3.analysisDependentHookAfter()
        val indexOf4 = MethodHookInfo("<java.lang.String: int indexOf(java.lang.String)>")
        indexOf4.analysisDependentHookAfter()
        val intern = MethodHookInfo("<java.lang.String: java.lang.String intern()>")
        intern.analysisDependentHookAfter()
        val isEmpty = MethodHookInfo("<java.lang.String: boolean isEmpty()>")
        isEmpty.analysisDependentHookAfter()
        val lastIndexOf1 = MethodHookInfo("<java.lang.String: int lastIndexOf(java.lang.String)>")
        lastIndexOf1.analysisDependentHookAfter()
        val lastIndexOf2 = MethodHookInfo("<java.lang.String: int lastIndexOf(int, int)>")
        lastIndexOf2.analysisDependentHookAfter()
        val lastIndexOf3 = MethodHookInfo("<java.lang.String: int lastIndexOf(int)>")
        lastIndexOf3.analysisDependentHookAfter()
        val lastIndexOf4 = MethodHookInfo("<java.lang.String: int lastIndexOf(java.lang.String, int)>")
        lastIndexOf4.analysisDependentHookAfter()
        val length = MethodHookInfo("<java.lang.String: int length()>")
        length.analysisDependentHookAfter()
        val matches = MethodHookInfo("<java.lang.String: boolean matches(java.lang.String)>")
        matches.analysisDependentHookAfter()
        val offsetByCodePoints = MethodHookInfo("<java.lang.String: int offsetByCodePoints(int, int)>")
        offsetByCodePoints.analysisDependentHookAfter()
        val regionMatches1 = MethodHookInfo("<java.lang.String: boolean regionMatches(boolean, int, java.lang.String, int, int)>")
        regionMatches1.analysisDependentHookAfter()
        val regionMatches2 = MethodHookInfo("<java.lang.String: boolean regionMatches(int, java.lang.String, int, int)>")
        regionMatches2.analysisDependentHookAfter()
        val replace1 = MethodHookInfo("<java.lang.String: java.lang.String replace(java.lang.CharSequence, java.lang.CharSequence)>")
        replace1.analysisDependentHookAfter()
        val replace2 = MethodHookInfo("<java.lang.String: java.lang.String replace(char, char)>")
        replace2.analysisDependentHookAfter()
        val split1 = MethodHookInfo("<java.lang.String: java.lang.String[] split(java.lang.String)>")
        split1.analysisDependentHookAfter()
        val split2 = MethodHookInfo("<java.lang.String: java.lang.String[] split(java.lang.String, int)>")
        split2.analysisDependentHookAfter()
        val startsWith1 = MethodHookInfo("<java.lang.String: boolean startsWith(java.lang.String)>")
        startsWith1.analysisDependentHookAfter()
        val startsWith2 = MethodHookInfo("<java.lang.String: boolean startsWith(java.lang.String, int)>")
        startsWith2.analysisDependentHookAfter()
        val subSequence = MethodHookInfo("<java.lang.String: java.lang.CharSequence subSequence(int, int)>")
        subSequence.analysisDependentHookAfter()
        val substring1 = MethodHookInfo("<java.lang.String: java.lang.String substring(int)>")
        substring1.analysisDependentHookAfter()
        val substring2 = MethodHookInfo("<java.lang.String: java.lang.String substring(int, int)>")
        substring2.analysisDependentHookAfter()
        val toCharArray = MethodHookInfo("<java.lang.String: char[] toCharArray()>")
        toCharArray.analysisDependentHookAfter()
        val toLowerCase1 = MethodHookInfo("<java.lang.String: java.lang.String toLowerCase(java.util.Locale)>")
        toLowerCase1.analysisDependentHookAfter()
        val toLowerCase2 = MethodHookInfo("<java.lang.String: java.lang.String toLowerCase()>")
        toLowerCase2.analysisDependentHookAfter()
        val toString = MethodHookInfo("<java.lang.String: java.lang.String toString()>")
        toString.analysisDependentHookAfter()
        val toUpperCase1 = MethodHookInfo("<java.lang.String: java.lang.String toUpperCase(java.util.Locale)>")
        toUpperCase1.analysisDependentHookAfter()
        val toUpperCase2 = MethodHookInfo("<java.lang.String: java.lang.String toUpperCase()>")
        toUpperCase2.analysisDependentHookAfter()
        val trim = MethodHookInfo("<java.lang.String: java.lang.String trim()>")
        trim.analysisDependentHookAfter()
        val valueOf1 = MethodHookInfo("<java.lang.String: java.lang.String valueOf(long)>")
        valueOf1.analysisDependentHookAfter()
        val valueOf2 = MethodHookInfo("<java.lang.String: java.lang.String valueOf(java.lang.Object)>")
        valueOf2.analysisDependentHookAfter()
        val valueOf3 = MethodHookInfo("<java.lang.String: java.lang.String valueOf(char[])>")
        valueOf3.analysisDependentHookAfter()
        val valueOf4 = MethodHookInfo("<java.lang.String: java.lang.String valueOf(double)>")
        valueOf4.analysisDependentHookAfter()
        val valueOf5 = MethodHookInfo("<java.lang.String: java.lang.String valueOf(int)>")
        valueOf5.analysisDependentHookAfter()
        val valueOf6 = MethodHookInfo("<java.lang.String: java.lang.String valueOf(float)>")
        valueOf6.analysisDependentHookAfter()
        val valueOf7 = MethodHookInfo("<java.lang.String: java.lang.String valueOf(char[], int, int)>")
        valueOf7.analysisDependentHookAfter()
        val valueOf8 = MethodHookInfo("<java.lang.String: java.lang.String valueOf(boolean)>")
        valueOf8.analysisDependentHookAfter()
        val valueOf9 = MethodHookInfo("<java.lang.String: java.lang.String valueOf(char)>")
        valueOf9.analysisDependentHookAfter()

        //		stringOpHoocks.add(charAt);
        //		stringOpHoocks.add(codePointAt);
        //		stringOpHoocks.add(codePointBefore);
        //		stringOpHoocks.add(codePointCount);
        //		stringOpHoocks.add(compareTo);
        //		stringOpHoocks.add(compareToIgnoreCase);
        //		stringOpHoocks.add(concat);
        //		stringOpHoocks.add(containsCharSequence);
        //		stringOpHoocks.add(contentEqualsCharSequence);
        //		stringOpHoocks.add(contentEqualsStringBuffer);
        //		stringOpHoocks.add(copyValueOf1);
        //		stringOpHoocks.add(copyValueOf2);
        //		stringOpHoocks.add(endsWith);
        //		stringOpHoocks.add(equals);
        //		stringOpHoocks.add(equalsIgnoreCase);
        //		stringOpHoocks.add(format1);
        //		stringOpHoocks.add(format2);
        //		stringOpHoocks.add(getBytes1);
        //		stringOpHoocks.add(getBytes2);
        //		stringOpHoocks.add(getBytes3);
        //		stringOpHoocks.add(getBytes4);
        //		stringOpHoocks.add(getChars);
        //		stringOpHoocks.add(hashCode);
        //		stringOpHoocks.add(indexOf1);
        //		stringOpHoocks.add(indexOf2);
        //		stringOpHoocks.add(indexOf3);
        //		stringOpHoocks.add(indexOf4);
        //		stringOpHoocks.add(intern);
        //		stringOpHoocks.add(isEmpty);
        //		stringOpHoocks.add(lastIndexOf1);
        //		stringOpHoocks.add(lastIndexOf2);
        //		stringOpHoocks.add(lastIndexOf3);
        //		stringOpHoocks.add(lastIndexOf4);
        //		stringOpHoocks.add(length);
        //		stringOpHoocks.add(matches);
        //		stringOpHoocks.add(offsetByCodePoints);
        //		stringOpHoocks.add(regionMatches1);
        //		stringOpHoocks.add(regionMatches2);
        //		stringOpHoocks.add(replace1);
        //		stringOpHoocks.add(replace2);
        //		stringOpHoocks.add(split1);
        //		stringOpHoocks.add(split2);
        //		stringOpHoocks.add(startsWith1);
        //		stringOpHoocks.add(startsWith2);
        //		stringOpHoocks.add(subSequence);
        //		stringOpHoocks.add(substring1);
        //		stringOpHoocks.add(substring2);
        //		stringOpHoocks.add(toCharArray);
        //		stringOpHoocks.add(toLowerCase1);
        //		stringOpHoocks.add(toLowerCase2);
        //		stringOpHoocks.add(toString);
        //		stringOpHoocks.add(toUpperCase1);
        //		stringOpHoocks.add(toUpperCase2);
        //		stringOpHoocks.add(trim);
        //		stringOpHoocks.add(valueOf1);
        //		stringOpHoocks.add(valueOf2);
        //		stringOpHoocks.add(valueOf3);
        //		stringOpHoocks.add(valueOf4);
        //		stringOpHoocks.add(valueOf5);
        //		stringOpHoocks.add(valueOf6);
        //		stringOpHoocks.add(valueOf7);
        //		stringOpHoocks.add(valueOf8);
        //		stringOpHoocks.add(valueOf9);


        return stringOpHoocks
    }


    private fun applicationInfoHooks(): Set<HookInfo> {
        val appInfoHooks = HashSet<HookInfo>()

        val loadLabel = MethodHookInfo("<android.content.pm.PackageItemInfo: java.lang.CharSequence loadLabel(android.content.pm.PackageManager)>")
        loadLabel.analysisDependentHookAfter()

        appInfoHooks.add(loadLabel)

        return appInfoHooks
    }


    private fun smsMessageHooks(): Set<HookInfo> {
        val smsMsgHooks = HashSet<HookInfo>()

        val displayOriginatingAddress1 = MethodHookInfo("<android.telephony.SmsMessage: java.lang.String getDisplayOriginatingAddress()>")
        displayOriginatingAddress1.analysisDependentHookAfter()
        val displayOriginatingAddress2 = MethodHookInfo("<android.telephony.SmsMessage: java.lang.String getOriginatingAddress()>")
        displayOriginatingAddress2.analysisDependentHookAfter()
        val displayOriginatingAddress3 = MethodHookInfo("<android.telephony.gsm.SmsMessage: java.lang.String getOriginatingAddress()>")
        displayOriginatingAddress3.analysisDependentHookAfter()
        val displayMessageBody1 = MethodHookInfo("<android.telephony.SmsMessage: java.lang.String getDisplayMessageBody()>")
        displayMessageBody1.analysisDependentHookAfter()
        val displayMessageBody2 = MethodHookInfo("<android.telephony.SmsMessage: java.lang.String getMessageBody()>")
        displayMessageBody2.analysisDependentHookAfter()
        val displayMessageBody3 = MethodHookInfo("<android.telephony.gsm.SmsMessage: java.lang.String getDisplayMessageBody()>")
        displayMessageBody3.analysisDependentHookAfter()


        smsMsgHooks.add(displayOriginatingAddress1)
        smsMsgHooks.add(displayOriginatingAddress2)
        smsMsgHooks.add(displayOriginatingAddress3)
        smsMsgHooks.add(displayMessageBody1)
        smsMsgHooks.add(displayMessageBody2)
        smsMsgHooks.add(displayMessageBody3)

        return smsMsgHooks
    }


    private fun sharedPreferencesHooks(): Set<HookInfo> {
        val sharedPrefHooks = HashSet<HookInfo>()

        val getInt = MethodHookInfo("<android.app.SharedPreferencesImpl: int getInt(java.lang.String, int)>")
        getInt.analysisDependentHookAfter()
        val getBoolean = MethodHookInfo("<android.app.SharedPreferencesImpl: boolean getBoolean(java.lang.String, boolean)>")
        getBoolean.analysisDependentHookAfter()
        val getFloat = MethodHookInfo("<android.app.SharedPreferencesImpl: float getFloat(java.lang.String, float)>")
        getFloat.analysisDependentHookAfter()
        val getLong = MethodHookInfo("<android.app.SharedPreferencesImpl: long getLong(java.lang.String, long)>")
        getLong.analysisDependentHookAfter()
        val getString = MethodHookInfo("<android.app.SharedPreferencesImpl: java.lang.String getString(java.lang.String, java.lang.String)>")
        getString.analysisDependentHookAfter()
        val getStringSet = MethodHookInfo("<android.app.SharedPreferencesImpl: java.util.Set getStringSet(java.lang.String, java.util.Set)>")
        getStringSet.analysisDependentHookAfter()

        sharedPrefHooks.add(getInt)
        sharedPrefHooks.add(getBoolean)
        sharedPrefHooks.add(getFloat)
        sharedPrefHooks.add(getLong)
        sharedPrefHooks.add(getString)
        sharedPrefHooks.add(getStringSet)

        return sharedPrefHooks
    }


    private fun networkRelatedHooks(): Set<HookInfo> {
        val networkHooks = HashSet<HookInfo>()

        val getByName = MethodHookInfo("<java.net.InetAddress: java.net.InetAddress getByName(java.lang.String)>")
        getByName.analysisDependentHookAfter()
        val getHostAddress = MethodHookInfo("<java.net.InetAddress: java.lang.String getHostAddress()>")
        getHostAddress.analysisDependentHookAfter()
        val urlConstructor = MethodHookInfo("<java.net.URL: void <init>(java.lang.String)>")
        urlConstructor.analysisDependentHookBefore()
        val getResponseCode = MethodHookInfo("<java.net.HttpURLConnection: int getResponseCode()>")
        getResponseCode.analysisDependentHookAfter()
        val getInputStream = MethodHookInfo("<com.android.okhttp.internal.http.HttpURLConnectionImpl: java.io.InputStream getInputStream()>")
        getInputStream.analysisDependentHookAfter()

        //		networkHooks.add(getByName);
        //		networkHooks.add(getHostAddress);
        networkHooks.add(urlConstructor)
        networkHooks.add(getResponseCode)
        networkHooks.add(getInputStream)

        return networkHooks
    }

    private fun foregroundActivityCheckHooks(): Set<HookInfo> {
        val foregroundHooks = HashSet<HookInfo>()

        val getByName = MethodHookInfo("<android.content.ComponentName: java.lang.String getPackageName()>")
        getByName.analysisDependentHookAfter()

        return foregroundHooks
    }

    private fun integrityRelatedHooks(): Set<HookInfo> {
        val integrityHooks = HashSet<HookInfo>()


        val integrity1 = MethodHookInfo("<de.tu_darmstadt.sse.additionalappclasses.wrapper.DummyWrapper: android.content.pm.PackageInfo dummyWrapper_getPackageInfo(android.content.pm.PackageManager,java.lang.String,int)>")
        integrity1.analysisDependentHookAfter()

        integrityHooks.add(integrity1)

        return integrityHooks
    }

    private fun fileRelatedHooks(): Set<HookInfo> {
        val fileHooks = HashSet<HookInfo>()

        val contextOpenFile = MethodHookInfo("<android.app.ContextImpl: java.io.FileInputStream openFileInput(java.lang.String)>")
        contextOpenFile.analysisDependentHookBefore()

        val propertyString1 = MethodHookInfo("<de.tu_darmstadt.sse.additionalappclasses.wrapper.DummyWrapper: java.lang.String dummyWrapper_getProperty(java.util.Properties,java.lang.String)>")
        propertyString1.analysisDependentHookAfter()
        val propertyString2 = MethodHookInfo("<de.tu_darmstadt.sse.additionalappclasses.wrapper.DummyWrapper: java.lang.String dummyWrapper_getProperty(java.util.Properties,java.lang.String,java.lang.String)>")
        propertyString2.analysisDependentHookAfter()

        fileHooks.add(contextOpenFile)
        fileHooks.add(propertyString1)
        fileHooks.add(propertyString2)

        return fileHooks
    }

    private fun reflectionHooks(): Set<HookInfo> {
        val reflectionHooks = HashSet<HookInfo>()

        val getMethodHookAfter = MethodHookInfo("<de.tu_darmstadt.sse.additionalappclasses.wrapper.DummyWrapper: java.lang.reflect.Method dummyWrapper_getMethod(java.lang.Class,java.lang.String,java.lang.Class[])>")
        getMethodHookAfter.analysisDependentHookAfter()

        reflectionHooks.add(getMethodHookAfter)

        return reflectionHooks
    }
}
