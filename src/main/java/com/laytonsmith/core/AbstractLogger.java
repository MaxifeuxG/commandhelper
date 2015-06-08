package com.laytonsmith.core;

/**
 * AbstractLogger, 6/2/2015 5:34 PM
 *
 * @author jb_aero
 */
public abstract class AbstractLogger {

	public void log(LogLevel level, String msg, Throwable throwable) {
		switch (level) {
			case DEBUG:
				debug(msg, throwable);
				break;
			case ERROR:
				error(msg, throwable);
				break;
			case INFO:
				info(msg, throwable);
				break;
			case VERBOSE:
				verbose(msg, throwable);
				break;
			case WARNING:
				warn(msg, throwable);
				break;
		}
	}

	public void log(LogLevel level, String format, Object... args) {
		switch (level) {
			case DEBUG:
				debug(format, args);
				break;
			case ERROR:
				error(format, args);
				break;
			case INFO:
				info(format, args);
				break;
			case VERBOSE:
				verbose(format, args);
				break;
			case WARNING:
				warn(format, args);
				break;
		}
	}

	public abstract void debug(String msg);

	public abstract void debug(String msg, Throwable throwable);

	public abstract void debug(String format, Object arg);

	public abstract void debug(String format, Object arg1, Object arg2);

	public abstract void debug(String format, Object... args);

	public abstract void error(String msg);

	public abstract void error(String msg, Throwable throwable);

	public abstract void error(String format, Object arg);

	public abstract void error(String format, Object arg1, Object arg2);

	public abstract void error(String format, Object... args);

	public abstract void info(String msg);

	public abstract void info(String msg, Throwable throwable);

	public abstract void info(String format, Object arg);

	public abstract void info(String format, Object arg1, Object arg2);

	public abstract void info(String format, Object... args);

	public abstract void verbose(String msg);

	public abstract void verbose(String msg, Throwable throwable);

	public abstract void verbose(String format, Object arg);

	public abstract void verbose(String format, Object arg1, Object arg2);

	public abstract void verbose(String format, Object... args);

	public abstract void warn(String msg);

	public abstract void warn(String msg, Throwable throwable);

	public abstract void warn(String format, Object arg);

	public abstract void warn(String format, Object arg1, Object arg2);

	public abstract void warn(String format, Object... args);
}
