package com.tzavellas.dyndelegate;

/**
 * This is a strategy interface for converting a <code>java.lang.Throwable</code> 
 * to another <code>java.lang.Throwable</code>
 * 
 * @see RuntimeExceptionWrapper
 * @see EJBExceptionConverter
 * 
 * @author Spiros Tzavellas
 */
public interface ThrowableConverter {
	
	/**
	 * Convert the specified Throwable to another Throwable.
	 * 
	 * @param ex the Throwable to convert
	 * @return a Throwable
	 */
	Throwable convert(Throwable ex);

}
