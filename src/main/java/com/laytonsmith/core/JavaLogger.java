package com.laytonsmith.core;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JavaLogger, 6/2/2015 5:48 PM
 *
 * @author jb_aero
 */
public class JavaLogger extends AbstractLogger {

	private static Logger logger;

	public JavaLogger(Logger logger) {
		this.logger = logger;
	}

	public static JavaLogger forName(String name) {
		return new JavaLogger(Logger.getLogger(name));
	}

	@Override
	public void debug(String msg) {
		logger.fine(msg);
	}

	@Override
	public void debug(String msg, Throwable throwable) {
		logger.log(Level.FINE, msg, throwable);
	}

	@Override
	public void debug(String format, Object arg) {
		logger.log(Level.FINE, format, arg);
	}

	@Override
	public void debug(String format, Object arg1, Object arg2) {
		debug(format, new Object[]{arg1, arg2});
	}

	@Override
	public void debug(String format, Object... args) {
		logger.log(Level.FINE, format, args);
	}

	@Override
	public void error(String msg) {
		logger.severe(msg);
	}

	@Override
	public void error(String msg, Throwable throwable) {
		logger.log(Level.SEVERE, msg, throwable);
	}

	@Override
	public void error(String format, Object arg) {
		logger.log(Level.SEVERE, format, arg);
	}

	@Override
	public void error(String format, Object arg1, Object arg2) {
		error(format, new Object[]{arg1, arg2});
	}

	@Override
	public void error(String format, Object... args) {
		logger.log(Level.SEVERE, format, args);
	}

	@Override
	public void info(String msg) {
		logger.info(msg);
	}

	@Override
	public void info(String msg, Throwable throwable) {
		logger.log(Level.INFO, msg, throwable);
	}

	@Override
	public void info(String format, Object arg) {
		logger.log(Level.INFO, format, arg);
	}

	@Override
	public void info(String format, Object arg1, Object arg2) {
		info(format, new Object[]{arg1, arg2});
	}

	@Override
	public void info(String format, Object... args) {
		logger.log(Level.INFO, format, args);
	}

	@Override
	public void verbose(String msg) {
		logger.log(Level.ALL, msg);
	}

	@Override
	public void verbose(String msg, Throwable throwable) {
		logger.log(Level.ALL, msg, throwable);
	}

	@Override
	public void verbose(String format, Object arg) {
		logger.log(Level.ALL, format, arg);
	}

	@Override
	public void verbose(String format, Object arg1, Object arg2) {
		verbose(format, new Object[]{arg1, arg2});
	}

	@Override
	public void verbose(String format, Object... args) {
		logger.log(Level.ALL, format, args);
	}

	@Override
	public void warn(String msg) {
		logger.warning(msg);
	}

	@Override
	public void warn(String msg, Throwable throwable) {
		logger.log(Level.WARNING, msg, throwable);
	}

	@Override
	public void warn(String format, Object arg) {
		logger.log(Level.WARNING, format, arg);
	}

	@Override
	public void warn(String format, Object arg1, Object arg2) {
		warn(format, new Object[]{arg1, arg2});
	}

	@Override
	public void warn(String format, Object... args) {
		logger.log(Level.WARNING, format, args);
	}
}
