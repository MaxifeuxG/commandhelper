package com.laytonsmith.commandhelper;

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
	public void debug(String msg, Throwable throwable) {
		logger.log(Level.FINE, msg, throwable);
	}

	@Override
	public void debug(String format, Object... args) {
		logger.log(Level.FINE, format, args);
	}

	@Override
	public void error(String msg, Throwable throwable) {
		logger.log(Level.SEVERE, msg, throwable);
	}

	@Override
	public void error(String format, Object... args) {
		logger.log(Level.SEVERE, format, args);
	}

	@Override
	public void info(String msg, Throwable throwable) {
		logger.log(Level.INFO, msg, throwable);
	}

	@Override
	public void info(String format, Object... args) {
		logger.log(Level.INFO, format, args);
	}

	@Override
	public void verbose(String msg, Throwable throwable) {
		logger.log(Level.ALL, msg, throwable);
	}

	@Override
	public void verbose(String format, Object... args) {
		logger.log(Level.ALL, format, args);
	}

	@Override
	public void warn(String msg, Throwable throwable) {
		logger.log(Level.WARNING, msg, throwable);
	}

	@Override
	public void warn(String format, Object... args) {
		logger.log(Level.WARNING, format, args);
	}
}
