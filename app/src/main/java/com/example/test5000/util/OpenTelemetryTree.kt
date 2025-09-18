package com.example.test5000.util

import io.opentelemetry.api.common.AttributeKey
import timber.log.Timber
import io.opentelemetry.api.logs.Severity

class OpenTelemetryTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {

        android.util.Log.i("OTelTree", "Intercepted: $message")


        // Map priority to severity + shorthand type
        val (severity, logType) = when (priority) {
            android.util.Log.VERBOSE -> Severity.TRACE to "v"
            android.util.Log.DEBUG -> Severity.DEBUG to "d"
            android.util.Log.INFO -> Severity.INFO to "i"
            android.util.Log.WARN -> Severity.WARN to "w"
            android.util.Log.ERROR, android.util.Log.ASSERT -> Severity.ERROR to "e"
            else -> Severity.UNDEFINED_SEVERITY_NUMBER to "u"
        }

        OpenTelemetryUtil.logger?.logRecordBuilder()
            ?.setSeverity(severity)
            ?.setBody(message)
            ?.setAttribute(AttributeKey.stringKey("log.type"), logType)
            ?.setAttribute(AttributeKey.stringKey("tag"), tag ?: "App")
            ?.setAttribute(AttributeKey.stringKey("class.name"), inferClassName())
            ?.setAttribute(AttributeKey.stringKey("thread"), Thread.currentThread().name)
            ?.apply {
                if (t != null) {
                    setAttribute(AttributeKey.stringKey("exception"), t.stackTraceToString())
                }
            }
            ?.emit()
    }

    // Infer caller class name (skip Timber + this Tree)
    private fun inferClassName(): String {
        val stackTrace = Throwable().stackTrace
        for (element in stackTrace) {
            if (!element.className.contains("Timber") &&
                !element.className.contains("OpenTelemetryTree")
            ) {
                return element.className
            }
        }
        return "UnknownClass"
    }
}



