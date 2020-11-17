package utils;

/**
 * Utility class for message logging
 * @author Erkka Nurmi
 *
 */
public class Logger {
	
	private final LogLevel loglevel;
	
	/**
	 * Constructor
	 * @param loglevel Log level used by the logger
	 */
	public Logger(LogLevel loglevel) {
		this.loglevel = loglevel;
	}

	/**
	 * Logs a message, if log level is trace
	 * @param message Message to log
	 */
	public void trace(String message) {
		if(
				loglevel == LogLevel.TRACE) {
			System.out.println(message);
		}
	}
	
	/**
	 * Logs a message, if log level is debug or lower
	 * @param message Message to log
	 */
	public void debug(String message) {
		if(
				loglevel == LogLevel.TRACE ||
				loglevel == LogLevel.DEBUG
				) {
			System.out.println(message);
		}
	}
	
	/**
	 * Logs a message, if log level is info or lower
	 * @param message Message to log
	 */
	public void info(String message) {
		if(
				loglevel == LogLevel.TRACE ||
				loglevel == LogLevel.DEBUG ||
				loglevel == LogLevel.INFO
				) {
			System.out.println(message);
		}
	}
	
	/**
	 * Logs a message, if log level is warning or lower
	 * @param message Message to log
	 */
	public void warning(String message) {
		if(
				loglevel == LogLevel.TRACE ||
				loglevel == LogLevel.DEBUG ||
				loglevel == LogLevel.INFO ||
				loglevel == LogLevel.WARNING
				) {
			System.out.println(message);
		}
	}

	/**
	 * Logs a message and an exception, if log level is error or lower
	 * @param message Message to log
	 * @param e Exception to log
	 */
	public void error(String message, Exception e) {
		if(
				loglevel == LogLevel.TRACE ||
				loglevel == LogLevel.DEBUG ||
				loglevel == LogLevel.INFO ||
				loglevel == LogLevel.WARNING ||
				loglevel == LogLevel.ERROR
				) {
			System.out.println(message);
			e.printStackTrace();
		}
	}

	/**
	 * Logs a message and an exception, if log level is critical or lower
	 * @param message Message to log
	 * @param e Exception to log
	 */
	public void critical(String message, Exception e) {
		if(
				loglevel == LogLevel.TRACE ||
				loglevel == LogLevel.DEBUG ||
				loglevel == LogLevel.INFO ||
				loglevel == LogLevel.WARNING ||
				loglevel == LogLevel.ERROR ||
				loglevel == LogLevel.CRITICAL
				) {
			System.out.println(message);
			e.printStackTrace();
		}
	}
	
	/**
	 * Logs a message and an exception, if log level is error or lower
	 * @param message Message to log
	 */
	public void error(String message) {
		if(
				loglevel == LogLevel.TRACE ||
				loglevel == LogLevel.DEBUG ||
				loglevel == LogLevel.INFO ||
				loglevel == LogLevel.WARNING ||
				loglevel == LogLevel.ERROR
				) {
			System.out.println(message);
		}
	}

	/**
	 * Logs a message and an exception, if log level is critical or lower
	 * @param message Message to log
	 */
	public void critical(String message) {
		if(
				loglevel == LogLevel.TRACE ||
				loglevel == LogLevel.DEBUG ||
				loglevel == LogLevel.INFO ||
				loglevel == LogLevel.WARNING ||
				loglevel == LogLevel.ERROR ||
				loglevel == LogLevel.CRITICAL
				) {
			System.out.println(message);
		}
	}
}
