package com.laytonsmith.core;

import static com.laytonsmith.PureUtilities.Common.StringUtils.format;

import org.slf4j.Logger;

/**
 * SLLogger, 6/2/2015 5:44 PM
 *
 * @author jb_aero
 *         <p/>
 *         An SLF4J adapter for the common logging system
 */
public class SLLogger extends AbstractLogger {

	private final Logger logger;

	public SLLogger(Logger logger) {
		this.logger = logger;
	}

	@Override
	public void debug(String msg) {
		logger.debug(msg);
	}

	@Override
	public void debug(String msg, Throwable throwable) {
		logger.debug(msg, throwable);
	}

	@Override
	public void debug(String format, Object arg) {
		logger.debug(format(format, arg));
	}

	@Override
	public void debug(String format, Object arg1, Object arg2) {
		logger.debug(format(format, arg1, arg2));
	}

	@Override
	public void debug(String format, Object... args) {
		logger.debug(format(format, args));
	}

	@Override
	public void error(String msg) {
		logger.error(msg);
	}

	@Override
	public void error(String msg, Throwable throwable) {
		logger.error(msg, throwable);
	}

	@Override
	public void error(String format, Object arg) {
		logger.error(format(format, arg));
	}

	@Override
	public void error(String format, Object arg1, Object arg2) {
		logger.error(format(format, arg1, arg2));
	}

	@Override
	public void error(String format, Object... args) {
		logger.error(format(format, args));
	}

	@Override
	public void info(String msg) {
		logger.info(msg);
	}

	@Override
	public void info(String msg, Throwable throwable) {
		logger.info(msg, throwable);
	}

	@Override
	public void info(String format, Object arg) {
		logger.info(format(format, arg));
	}

	@Override
	public void info(String format, Object arg1, Object arg2) {
		logger.info(format(format, arg1, arg2));
	}

	@Override
	public void info(String format, Object... args) {
		logger.info(format(format, args));
	}

	@Override
	public void verbose(String msg) {
		logger.trace(msg);
	}

	@Override
	public void verbose(String msg, Throwable throwable) {
		logger.trace(msg, throwable);
	}

	@Override
	public void verbose(String format, Object arg) {
		logger.trace(format(format, arg));
	}

	@Override
	public void verbose(String format, Object arg1, Object arg2) {
		logger.trace(format(format, arg1, arg2));
	}

	@Override
	public void verbose(String format, Object... args) {
		logger.trace(format(format, args));
	}

	@Override
	public void warn(String msg) {
		logger.warn(msg);
	}

	@Override
	public void warn(String msg, Throwable throwable) {
		logger.warn(msg, throwable);
	}

	@Override
	public void warn(String format, Object arg) {
		logger.warn(format(format, arg));
	}

	@Override
	public void warn(String format, Object arg1, Object arg2) {
		logger.warn(format(format, arg1, arg2));
	}

	@Override
	public void warn(String format, Object... args) {
		logger.warn(format(format, args));
	}
}
