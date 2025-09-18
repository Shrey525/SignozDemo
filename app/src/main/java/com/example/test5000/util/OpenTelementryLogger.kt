package com.example.test5000.util

import android.util.Log

object OpenTelementryLogger {

    /**
     * Send INFO log
     */
    fun i(tag: String, message: String) {
        log(tag, message, io.opentelemetry.api.logs.Severity.INFO)
        Log.i(tag, message) // Optional: also print to Logcat
    }

    /**
     * Send DEBUG log
     */
    fun d(tag: String, message: String) {
        log(tag, message, io.opentelemetry.api.logs.Severity.DEBUG)
        Log.d(tag, message)
    }

    /**
     * Send ERROR log (with optional exception)
     */
    fun e(tag: String, message: String, throwable: Throwable? = null) {
        val errorMessage = if (throwable != null) "$message\n${throwable.stackTraceToString()}" else message
        log(tag, errorMessage, io.opentelemetry.api.logs.Severity.ERROR)
        Log.e(tag, message, throwable)
    }

    /**
     * Internal function to build and emit log record
     */
    private fun log(tag: String, message: String, severity: io.opentelemetry.api.logs.Severity) {
        OpenTelemetryUtil.logger
            ?.logRecordBuilder()
            ?.setBody("[$tag] $message") // 👈 Include tag + message
            ?.setSeverity(severity)
            ?.emit()
    }
}
