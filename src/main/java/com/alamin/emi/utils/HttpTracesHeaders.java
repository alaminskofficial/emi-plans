package com.alamin.emi.utils;

import java.net.HttpURLConnection;

import javax.net.ssl.HttpsURLConnection;

import brave.Tracer;
import brave.Tracing;
import brave.propagation.TraceContext;

public class HttpTracesHeaders {
	static Tracer tracer = Tracing.currentTracer();

	public static HttpURLConnection addHttpHeaderSleuthTraces(HttpURLConnection conn) {
		TraceContext context = tracer.currentSpan().context();
		conn.setRequestProperty("X-B3-TraceId", context.traceIdString());
		conn.setRequestProperty("X-B3-SpanId", String.valueOf(context.spanId()));
		return conn;
	}

	public static HttpsURLConnection addHttpsHeaderSleuthTraces(HttpsURLConnection conn) {
		TraceContext context = tracer.currentSpan().context();
		conn.setRequestProperty("X-B3-TraceId", context.traceIdString());
		conn.setRequestProperty("X-B3-SpanId", String.valueOf(context.spanId()));
		return conn;
	}

}
