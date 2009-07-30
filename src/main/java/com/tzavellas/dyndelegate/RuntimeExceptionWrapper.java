package com.tzavellas.dyndelegate;

/**
 * Wrap any <code>java.lang.Throwable</code> inside a
 * <code>java.lang.RuntimeException</code>.
 * 
 * @see ThrowableConverter
 * @author Spiros Tzavellas
 *
 */
public class RuntimeExceptionWrapper implements ThrowableConverter {
	
	/**
	 * Wrap the specified Throwable to a RuntimeException
	 * 
	 * @param ex the Throwable to be wrapped
	 * @return a RuntimeException
	 */
	public Throwable convert(Throwable ex) {
		return new RuntimeException(ex);
	}

}
