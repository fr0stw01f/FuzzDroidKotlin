package de.tu_darmstadt.sse.additionalappclasses.wrapper

import java.lang.reflect.Method
import java.util.Properties

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.util.Log
import de.tu_darmstadt.sse.sharedclasses.SharedClassesSettings

object DummyWrapper {

    fun dummyWrapper_getPackageInfo(manager: PackageManager, packageName: String, flags: Int): PackageInfo? {
        Log.i(SharedClassesSettings.TAG, "Dummy getPackage called")
        try {
            return manager.getPackageInfo(packageName, flags)
        } catch (e: NameNotFoundException) {
            e.printStackTrace()
        }

        return null
    }


    fun dummyWrapper_getProperty(props: Properties, key: String, defaultValue: String): String {
        return props.getProperty(key, defaultValue)
    }


    fun dummyWrapper_getProperty(props: Properties, key: String): String {
        return props.getProperty(key)
    }


    fun dummyWrapper_loadClass(className: String, classLoader: ClassLoader): Class<*>? {
        Log.i(SharedClassesSettings.TAG, "Dummy loadClass() called for " + className)
        var clazz: Class<*>? = null
        try {
            clazz = classLoader.loadClass(className)
            //in case it does not exist, we use our dummy class
        } catch (e: ClassNotFoundException) {
            try {
                clazz = DummyWrapper::class.java.classLoader.loadClass(className)
            } catch (e2: ClassNotFoundException) {
                try {
                    clazz = classLoader.loadClass("de.tu_darmstadt.sse.additionalappclasses.reflections.DummyReflectionClass")
                    Log.i(SharedClassesSettings.TAG, "Dummy class returned for " + className)
                } catch (ex: ClassNotFoundException) {
                    ex.printStackTrace()
                }

            }

        }

        return clazz
    }

    fun dummyWrapper_getMethod(clazz: Class<*>, methodName: String, parameterTypes: Array<Class<*>>): Method? {
        Log.i(SharedClassesSettings.TAG, String.format("Dummy getMethod() called for %s.%s",
                clazz.name, methodName))

        // For some methods, we need to inject our own implementations
        if (clazz.name == "dalvik.system.DexFile" && methodName == "loadClass") {
            try {
                val m = DummyWrapper::class.java.getMethod("dummyWrapper_loadClass",
                        String::class.java, ClassLoader::class.java)
                Log.i(SharedClassesSettings.TAG, "Dummy getMethod() obtained: " + m)
                return m
            } catch (e: NoSuchMethodException) {
                Log.i(SharedClassesSettings.TAG, "Could not get dummy implementation for loadClass(), falling " + "back to original one")
            } catch (e: SecurityException) {
                Log.i(SharedClassesSettings.TAG, "Could not get dummy implementation for loadClass(), falling " + "back to original one")
            }

        }

        var method: Method? = null
        try {
            method = clazz.getMethod(methodName, *parameterTypes)
        } catch (ex: Exception) {
            Log.i(SharedClassesSettings.TAG, "Could not find method, falling back to dummy")
            try {
                val dummyClass = Class.forName("de.tu_darmstadt.sse.additionalappclasses.reflections.DummyReflectionClass")
                method = dummyClass.getMethod("dummyReflectionMethod", null)
            } catch (ex2: Exception) {
                ex2.printStackTrace()
            }

        }

        return method
    }
}
