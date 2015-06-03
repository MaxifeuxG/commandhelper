package com.laytonsmith.commandhelper;

import org.slf4j.Logger;

/**
 * SLLogger, 6/2/2015 5:44 PM
 *
 * @author jb_aero
 */
public class SLLogger extends AbstractLogger {

	private final Logger logger;

	public SLLogger(Logger logger) {
		this.logger = logger;
	}

	@Override
	public void debug(String msg, Throwable throwable) {
		logger.debug(msg, throwable);
	}

	@Override
	public void debug(String format, Object... args) {
		logger.debug(format, args);
	}

	@Override
	public void error(String msg, Throwable throwable) {
		logger.error(msg, throwable);
	}

	@Override
	public void error(String format, Object... args) {
		logger.error(format, args);
	}

	@Override
	public void info(String msg, Throwable throwable) {
		logger.info(msg, throwable);
	}

	@Override
	public void info(String format, Object... args) {
		logger.info(format, args);
	}

	@Override
	public void verbose(String msg, Throwable throwable) {
		logger.trace(msg, throwable);
	}

	@Override
	public void verbose(String format, Object... args) {
		logger.trace(format, args);
	}

	@Override
	public void warn(String msg, Throwable throwable) {
		logger.warn(msg, throwable);
	}

	@Override
	public void warn(String format, Object... args) {
		logger.warn(format, args);
	}
}
