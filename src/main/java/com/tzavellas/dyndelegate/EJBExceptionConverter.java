package com.tzavellas.dyndelegate;

import java.rmi.RemoteException;

import javax.ejb.EJBException;


/**
 * A <code>ThrowableConverter</code> implementation suitable for converting
 * Exceptions in Business Delegate implementations.
 * <p>
 * This converter, tries to hide EJB specific exceptions like
 * <code>RemoteException</code> and <code>EJBException</code>.
 * </p>
 * 
 * @see ThrowableConverter
 * 
 * @author Spiros Tzavellas
 */
public class EJBExceptionConverter implements ThrowableConverter {

	/**
	 *  If the specified Throwable is a RemoteException that wraps
	 * an unchecked exception return the wrapped unchecked exception,
	 * else return a RuntimeException wrapping the RemoteException.
	 * 
	 *  If the specified Throwable is an EJBException that wraps
	 * an unchecked exception return the wrapped unchecked exception,
	 * else return the EJBException.
	 * 
	 *  If none of the above conditions apply, return the specified
	 * Throwable without any modification.
	 */
	public Throwable convert(Throwable ex) {
		
		// EJB Remote Access
		if (ex instanceof RemoteException) {
			Throwable cause = ex.getCause();
			if (cause != null && cause instanceof RuntimeException)
				return cause;
			else
				return new RuntimeException(ex);
		}

		// EJB Local Access
		if (ex instanceof EJBException) {
			Throwable cause = ex.getCause();
			if (cause != null && cause instanceof RuntimeException)
				return cause;
			else
				return ex;
		}

		return ex;
	}

}
