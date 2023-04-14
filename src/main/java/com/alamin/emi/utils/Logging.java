package com.alamin.emi.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

class ErrorLogger {

}

class MetricsLog {

}

public class Logging {
	static Logger log = Logger.getLogger(Logging.class.getName());
	static Logger errorlog = Logger.getLogger(ErrorLogger.class.getName());
	static Logger metricsLog = Logger.getLogger(MetricsLog.class.getName());

	private String logLevel;

	private Logging() {
		Logging.getInfoLog().info("init method...");
		init();
	}

	public static Logger getInfoLog() {
		return log;
	}

	public static Logger getErrorLog() {
		return errorlog;
	}

	public static Logger getMetricsLog() {
		return metricsLog;
	}

	public static void init() {

		PropertyConfigurator.configure(Logging.class.getResourceAsStream("/log4j.properties"));
		LogManager.getRootLogger().setLevel(Level.INFO);
		Logging.getLogger().fatal("****************************************");
		Logging.getLogger().fatal("        Starting Logger Serivce         ");
		Logging.getLogger().fatal("****************************************");
	}

	public String getLogLevel() {
		return this.logLevel;
	}

	public void setLogLevel(String level) {

		PropertyConfigurator.configure(Logging.class.getResourceAsStream("/log4j.properties"));
		switch (level) {
		case "INFO":
			LogManager.getRootLogger().setLevel(Level.INFO);
			break;
		case "DEBUG":
			LogManager.getRootLogger().setLevel(Level.DEBUG);
			break;
		case "WARN":
			LogManager.getRootLogger().setLevel(Level.WARN);
			break;
		case "ERROR":
			LogManager.getRootLogger().setLevel(Level.ERROR);
			break;
		case "FATAL":
			LogManager.getRootLogger().setLevel(Level.FATAL);
			break;
		}

		Logging.getLogger().fatal("****************************************");
		Logging.getLogger()
				.fatal("   Starting Logger Serivce : Level  - " + LogManager.getRootLogger().getLevel().toString());
		Logging.getLogger().fatal("****************************************");
		this.logLevel = level;
	}

	public static Logger getLogger() {
		return log;
	}

	public synchronized void logDebug(String logStr) {
		log.debug(logStr);
	}

	public synchronized void logInfo(String logStr) {
		log.info(logStr);
	}

	public synchronized void logWarn(String logStr) {
		log.warn(logStr);
	}

	public synchronized void logError(String logStr) {
		log.error(logStr);
	}

	public synchronized void fatal(String logStr) {
		log.fatal(logStr);
	}

	public synchronized void logException(Exception ex) {
		log.fatal(getStackTrace(ex));
	}

	public synchronized static String getStackTrace(final Throwable throwable) {
		final StringWriter sw = new StringWriter();
		final PrintWriter pw = new PrintWriter(sw, true);
		throwable.printStackTrace(pw);
		return sw.getBuffer().toString();
	}
}