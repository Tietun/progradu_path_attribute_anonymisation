package utils;

public class Logger {
	
	private LogLevel loglevel;
	
	public Logger(LogLevel loglevel) {
		this.loglevel = loglevel;
	}

	public void trace(String message) {
		if(
				loglevel == LogLevel.TRACE) {
			System.out.println(message);
		}
	}
	
	public void debug(String message) {
		if(
				loglevel == LogLevel.TRACE ||
				loglevel == LogLevel.DEBUG
				) {
			System.out.println(message);
		}
	}
	
	public void info(String message) {
		if(
				loglevel == LogLevel.TRACE ||
				loglevel == LogLevel.DEBUG ||
				loglevel == LogLevel.INFO
				) {
			System.out.println(message);
		}
	}
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
