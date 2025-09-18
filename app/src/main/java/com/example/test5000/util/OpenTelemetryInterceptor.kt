package com.example.test5000.util

import okhttp3.Interceptor
import okhttp3.Response

class OpenTelemetryInterceptor : Interceptor {
    private val tracer = OpenTelemetryUtil.getTracer()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val span = tracer?.spanBuilder("HTTP ${request.method} ${request.url.encodedPath}")?.startSpan()
        try {
            span?.makeCurrent().use {
                span?.setAttribute("http.method", request.method)
                span?.setAttribute("http.url", request.url.toString())

                val response = chain.proceed(request)

                span?.setAttribute("http.status_code", response.code.toDouble())
                return response
            }
        } catch (e: Exception) {
            span?.recordException(e)
            throw e
        } finally {
            span?.end()
        }
    }
}
