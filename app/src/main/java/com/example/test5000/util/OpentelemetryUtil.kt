package com.example.test5000.util
import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.api.common.AttributeKey
import io.opentelemetry.api.common.Attributes
import io.opentelemetry.api.trace.Tracer
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator
import io.opentelemetry.context.propagation.ContextPropagators
import io.opentelemetry.exporter.logging.LoggingSpanExporter
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter
import io.opentelemetry.sdk.OpenTelemetrySdk
import io.opentelemetry.sdk.resources.Resource
import io.opentelemetry.sdk.trace.SdkTracerProvider
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor
import io.opentelemetry.sdk.logs.SdkLoggerProvider
import io.opentelemetry.exporter.logging.SystemOutLogRecordExporter
import io.opentelemetry.exporter.otlp.logs.OtlpGrpcLogRecordExporter
import io.opentelemetry.api.logs.Logger
import io.opentelemetry.sdk.logs.export.SimpleLogRecordProcessor


class OpenTelemetryUtil {
    companion object {
        private var tracer: Tracer? = null
        private lateinit var loggerProvider: SdkLoggerProvider
        var logger: Logger? = null

        @JvmStatic
        fun init() {
            // Define common resource attributes (service name, environment, etc.)
            val otelResource = Resource.getDefault().merge(
                Resource.create(
                    Attributes.of(
                        AttributeKey.stringKey("service.name"), "demo_android_app",
                        AttributeKey.stringKey("deployment.environment"), "dev"
                    )
                )
            )

            // ----- Traces -----
            val sdkTracerProvider = SdkTracerProvider.builder()
                .addSpanProcessor(SimpleSpanProcessor.create(LoggingSpanExporter.create()))
                .addSpanProcessor(
                    BatchSpanProcessor.builder(
                        OtlpGrpcSpanExporter.builder()
                            .setEndpoint("https://ingest.in.signoz.cloud:443/v1/traces")
                            .addHeader("signoz-ingestion-key", "d793128e-5bd7-4121-9a45-f92c51c80600")
                            .build()
                    ).build()
                )
                .setResource(otelResource)
                .build()

            // ----- Logs -----
            val logExporter = OtlpGrpcLogRecordExporter.builder()
                .setEndpoint("https://ingest.in.signoz.cloud:443/v1/logs")
                .addHeader("signoz-ingestion-key", "d793128e-5bd7-4121-9a45-f92c51c80600")
                .build()

            val consoleExporter = SystemOutLogRecordExporter.create()

            // Use Simple processor (immediate export) instead of batching
            loggerProvider = SdkLoggerProvider.builder()
                .addLogRecordProcessor(SimpleLogRecordProcessor.create(logExporter))
                .addLogRecordProcessor(SimpleLogRecordProcessor.create(consoleExporter)) // print to console
                .setResource(otelResource)
                .build()

            // ----- OpenTelemetry Global -----
            val openTelemetry: OpenTelemetry = OpenTelemetrySdk.builder()
                .setTracerProvider(sdkTracerProvider)
                .setLoggerProvider(loggerProvider)
                .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
                .buildAndRegisterGlobal()

            // Expose tracer + logger
            tracer = openTelemetry.getTracer("android-tracer", "1.0.0")
            logger = loggerProvider.get("com.example.myapp")
        }

        @JvmStatic
        fun getTracer(): Tracer? = tracer
    }
}





